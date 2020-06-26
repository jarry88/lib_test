package com.ftofs.twant.fragment;

import android.net.http.SslError;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.AppGuideActivity;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.activity.SplashActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.handler.NativeJsBridge;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.PathUtil;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.orhanobut.hawk.Hawk;
import com.yanzhenjie.permission.runtime.Permission;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * H5游戲頁面
 * @author zwm
 */
public class H5GameFragment extends BaseFragment implements View.OnClickListener {
    BasePopupView loadingPopup;
    String url;
    String customTitle; // 显示自定义的标题

    public static final int ARTICLE_ID_INVALID = -1; // -1表示無效的文章Id
    public static final int ARTICLE_ID_TERMS_OF_SERVICE = 0; // 服務協議
    public static final int ARTICLE_ID_TERMS_OF_PRIVATE = 3; // 隱私協議

    int articleId = ARTICLE_ID_INVALID;  // 文章Id

    private WebView mWebView;

    boolean ignoreSslError = true; // 忽略ssl错误
    boolean reloadOnVisible;  // fragment重新可见时，是否刷新浏览器

    boolean isFirstVisible = true;

    TextView tvFragmentTitle;


    public static H5GameFragment newInstance(String url, boolean ignoreSslError, boolean reloadOnVisible, String title, int articleId) {
        Bundle args = new Bundle();

        args.putString("url", url);
        args.putBoolean("ignoreSslError", ignoreSslError);
        args.putBoolean("reloadOnVisible", reloadOnVisible);
        args.putString("title", title);

        H5GameFragment fragment = new H5GameFragment();
        fragment.setArguments(args);
        fragment.articleId = articleId;

        return fragment;
    }

    public static H5GameFragment newInstance(String url, boolean ignoreSslError) {
        return newInstance(url, ignoreSslError, false, null, ARTICLE_ID_INVALID);
    }

    public static H5GameFragment newInstance(String url, String title) {
        return newInstance(url, true, false, title, ARTICLE_ID_INVALID);
    }

    /**
     * 加載指定Id的文章
     * @param articleId
     * @param title
     * @return
     */
    public static H5GameFragment newInstance(int articleId, String title) {
        return newInstance(null, true, false, title, articleId);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_h5_game, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        url = StringUtil.normalizeImageUrl(args.getString("url"));
        ignoreSslError = args.getBoolean("ignoreSslError");
        reloadOnVisible = args.getBoolean("reloadOnVisible");
        customTitle = args.getString("title");

        SLog.info("url[%s], ignoreSslError[%s], reloadOnVisible[%s]", url, ignoreSslError, reloadOnVisible);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        tvFragmentTitle.setSelected(true);
        Util.setOnClickListener(view, R.id.btn_back, this);

        mWebView=view.findViewById(R.id.x5_web_view);
        loadingPopup = Util.createLoadingPopup(_mActivity).show();
        SLog.info("mwebView url[%s]",url);
        if (articleId == ARTICLE_ID_INVALID) {
            mWebView.loadUrl(url);
        } else {
            loadPageContent();
        }

        //webView = view.findViewById(R.id.web_view);
        //webView.getSettings().setJavaScriptEnabled(true);
        NativeJsBridge nativeJsBridge = new NativeJsBridge(_mActivity, this);
        mWebView.addJavascriptInterface(nativeJsBridge, "android");
        // webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);  // WebView自动开始播放音乐要加上这个设定
        mWebView.getSettings().setDomStorageEnabled(true);  // 啟用localStorage
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                loadingPopup.dismiss();
                String title;
                if (!StringUtil.isEmpty(customTitle)) {
                    title = customTitle;
                } else {
                    title = view.getTitle();
                }

                if (title == null) {
                    title = "";
                }
                title = title.trim();
                if (!StringUtil.isEmpty(title)) {
                    tvFragmentTitle.setText(title);
                }
            }

            // 如果忽略Ssl校驗錯誤
            // 一個提示不安全的網址   https://inv-veri.chinatax.gov.cn/
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                if (ignoreSslError) {
                    handler.proceed();//忽略证书的错误继续加载页面内容，不会变成空白页面
                } else {
                    handler.cancel();// super中默认的处理方式，WebView变成空白页
                    // super.onReceivedSslError(view, handler, error);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);

                // SLog.info("url[%s]", url);
                // view.loadUrl(url);
                // return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                SLog.info("title[%s]", title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                SLog.info("url[%s], message[%s]", url, message);
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                SLog.info("url[%s], message[%s]", url, message);
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                SLog.info("url[%s], message[%s]", url, message);
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });

        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
                SLog.info("type__[%s]", hitTestResult.getType());
                if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                        hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    String imageUrlOrig = hitTestResult.getExtra(); // 获取图片
                    SLog.info("imageUrlOrig[%s]", imageUrlOrig);

                    if (imageUrlOrig.contains("?")) {
                        String[] imageUrlArr = imageUrlOrig.split("\\?");
                        imageUrlOrig = imageUrlArr[0];
                        SLog.info("imageUrl[%s]", imageUrlOrig);
                    }

                    // 防止點擊其他圖片
                    if (imageUrlOrig.contains("/web/static/img/")) {
                        return false;
                    }

                    String imageUrl = imageUrlOrig;
                    new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                            // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                            .setPopupCallback(new XPopupCallback() {
                                @Override
                                public void onShow() {
                                }
                                @Override
                                public void onDismiss() {
                                }
                            }).asCustom(new TwConfirmPopup(_mActivity, "保存至手機相冊?", null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");
                            saveImageToGallery(imageUrl);
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    }))
                            .show();
                }
                return false;
            }
        });
    }

    private void loadPageContent() {
        if (articleId == ARTICLE_ID_INVALID) {
            return;
        }
        String url = Api.PATH_ARTICLE_DETAIL + "/" + articleId;
        SLog.info("url[%s]", url);
        Api.getUI(url, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    String htmlContent = responseObj.getSafeString("datas.articleInfo.content");
                    mWebView.loadData(htmlContent, "text/html", "utf-8");
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        SLog.info("onSupportVisible");
        // 不是首次可见，并且需要刷新时，刷新WebView
        if (!isFirstVisible && reloadOnVisible) {
            SLog.info("reload, url[%s]", mWebView.getUrl());
            mWebView.reload();
        }
        isFirstVisible = false;
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mWebView != null) {
            SLog.info("onDestroyView HERE");
            mWebView.clearHistory();
            mWebView.destroy();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(false);
        }
    }

    private void saveImageToGallery(String imageUrl) {
        PermissionUtil.actionWithPermission(getContext(), new String[] {Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_EXTERNAL_STORAGE}, "保存圖片需要授予", new CommonCallback() {

            @Override
            public String onSuccess(@Nullable String data) {
                if (imageUrl.startsWith(Util.DATA_IMAGE_PNG_PREFIX)) { // base64图片
                    String base64Code = imageUrl.substring(22);
                    byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
                    String filename = FileUtil.getAppDataRoot() + "/download/" + System.currentTimeMillis() + ".jpg";
                    if (!Util.makeParentDirectory(filename)) {
                        SLog.info("Error!創建文件[%s]的父目錄失敗", filename);
                        return null;
                    }
                    String directory = PathUtil.getDirectory(filename);
                    File dirFile = new File(directory);
                    if (!dirFile.exists()) { // 判斷目錄是否存在，如果不存在，則創建
                        dirFile.mkdirs();
                    }

                    SLog.info("filename[%s]", filename);

                    try {
                        FileOutputStream out = new FileOutputStream(filename);
                        out.write(buffer);
                        out.close();

                        Util.addImageToGallery(_mActivity, new File(filename));

                        ToastUtil.success(_mActivity, "保存成功");
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                        ToastUtil.success(_mActivity, "保存失敗");
                    }
                } else { // 常规图片
                    doSaveImageToGallery(imageUrl);
                }
                return null;
            }

            @Override
            public String onFailure(@Nullable String data) {

                return null;
            }
        });
    }

    private void doSaveImageToGallery(String imageUrl) {
        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                boolean success = (boolean) message;
                if (success){
                    ToastUtil.success(_mActivity, "保存成功");
                } else {
                    ToastUtil.success(_mActivity, "保存失敗");
                }
            }
        };

        TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                boolean success;
                String ext = PathUtil.getExtension(imageUrl);
                String filename = FileUtil.getAppDataRoot() + "/download/" + System.currentTimeMillis() + "." + ext;
                if (!Util.makeParentDirectory(filename)) {
                    SLog.info("Error!創建文件[%s]的父目錄失敗", filename);
                    return null;
                }
                SLog.info("filename[%s]", filename);
                File file = new File(filename);
                File dir = file.getParentFile();
                if (!dir.exists()) {
                    success = dir.mkdirs();
                    if (!success) {
                        SLog.info("HERE");
                        return false;
                    }
                }
                try {
                    success = Api.syncDownloadFile(imageUrl, file);
                    SLog.info("success[%s]", success);
                    if (success) {
                        Util.addImageToGallery(_mActivity, file);
                    }
                    return success;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }

                return false;
            }
        });
    }

    public String getUrl() {
        return url;
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public void clearCache() {
        mWebView.clearCache(true);
        mWebView.clearFormData();
        mWebView.clearHistory();
    }
}
