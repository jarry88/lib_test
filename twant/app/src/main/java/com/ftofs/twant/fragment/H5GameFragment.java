package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.handler.NativeJsBridge;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.PathUtil;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.yanzhenjie.permission.runtime.Permission;
import com.ycbjie.webviewlib.InterWebListener;
import com.ycbjie.webviewlib.WebProgress;
import com.ycbjie.webviewlib.X5WebUtils;
import com.ycbjie.webviewlib.X5WebView;
import com.ycbjie.webviewlib.X5WebViewClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * H5游戲頁面
 * @author zwm
 */
public class H5GameFragment extends BaseFragment implements View.OnClickListener {
    BasePopupView loadingPopup;
    String url;
    String customTitle; // 显示自定义的标题
    private X5WebView mWebView;
    private WebProgress progress;

    boolean ignoreSslError = true; // 忽略ssl错误
    boolean reloadOnVisible;  // fragment重新可见时，是否刷新浏览器

    boolean isFirstVisible = true;

    TextView tvFragmentTitle;

    public static H5GameFragment newInstance(String url, boolean ignoreSslError, boolean reloadOnVisible, String title) {
        Bundle args = new Bundle();

        args.putString("url", url);
        args.putBoolean("ignoreSslError", ignoreSslError);
        args.putBoolean("reloadOnVisible", reloadOnVisible);
        args.putString("title", title);

        H5GameFragment fragment = new H5GameFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static H5GameFragment newInstance(String url, boolean ignoreSslError) {
        return newInstance(url, ignoreSslError, false, null);
    }

    public static H5GameFragment newInstance(String url, String title) {
        return newInstance(url, true, false, title);
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
        progress = view.findViewById(R.id.progress);
        progress.show();
        progress.setColor(getResources().getColor(R.color.colorAccent,null),this.getResources().getColor(R.color.colorPrimaryDark,null));

        loadingPopup = Util.createLoadingPopup(_mActivity).show();
        SLog.info("mwebView url[%s]",url);
        mWebView.loadUrl(url);
        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
        mWebView.getX5WebViewClient().setWebListener(interWebListener);
        //webView = view.findViewById(R.id.web_view);
        //webView.getSettings().setJavaScriptEnabled(true);
        NativeJsBridge nativeJsBridge = new NativeJsBridge(_mActivity, this);
        mWebView.addJavascriptInterface(nativeJsBridge, "android");
        // webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);  // WebView自动开始播放音乐要加上这个设定
        mWebView.getSettings().setDomStorageEnabled(true);  // 啟用localStorage
        mWebView.setWebViewClient(new X5WebViewClient(mWebView, _mActivity) {
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
                    //handler.cancel();// super中默认的处理方式，WebView变成空白页
                    super.onReceivedSslError(view, handler, error);
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
    private InterWebListener interWebListener = new InterWebListener() {
        @Override
        public void hindProgressBar() {
            progress.hide();
        }

        @Override
        public void showErrorView(@X5WebUtils.ErrorType int type) {
            switch (type){
                //没有网络
                case X5WebUtils.ErrorMode.NO_NET:
                    break;
                //404，网页无法打开
                case X5WebUtils.ErrorMode.STATE_404:

                    break;
                //onReceivedError，请求网络出现error
                case X5WebUtils.ErrorMode.RECEIVED_ERROR:

                    break;
                //在加载资源时通知主机应用程序发生SSL错误
                case X5WebUtils.ErrorMode.SSL_ERROR:

                    break;
                default:
                    break;
            }
        }

        @Override
        public void startProgress(int newProgress) {
            progress.setWebProgress(newProgress);
        }

        @Override
        public void showTitle(String title) {

        }
    };

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
