package com.ftofs.twant.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.entity.SoftInputInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.PathUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 啟動頁面
 * @author zwm
 */
public class SplashActivity extends BaseActivity {
    /**
     * 啟動頁需要顯示的時間（毫秒）
     */
    private static final int SPLASH_DURATION = 2000;
    Jarbon jarbon = new Jarbon();
    long appStartTime = System.currentTimeMillis();

    ImageView splashBackground;

    RelativeLayout rlContainer;
    EditText etStub; // 用於顯示軟鍵盤以獲取軟鍵盤的高度

    SoftInputInfo softInputInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 檢測是否從瀏覽器網頁拉起APP
        Intent intent =getIntent();
        if (intent != null) {
            /*
            参考
            Android 使用Scheme实现从网页启动APP
            https://blog.csdn.net/dpl12/article/details/82703780
             */
            String appUriScheme = getString(R.string.app_uri_scheme);
            String scheme = intent.getScheme();
            SLog.info("scheme[%s]", scheme);
            if (appUriScheme.equals(scheme)) {
                Uri uri =intent.getData();
                if (uri != null) {
                    SLog.info("scheme[%s]", uri.getScheme());
                    String host = uri.getHost();
                    SLog.info("host[%s]", host);
                    SLog.info("port[%s]", uri.getPort());
                    SLog.info("path[%s]", uri.getPath());
                    SLog.info("queryString[%s]", uri.getQuery());
                    // SLog.info("queryParameter: "+uri.getQueryParameter("key"));

                    EasyJSONObject launchAppParams = null;

                    if ("weburl".equals(host)) { // 1 跳轉網頁
                        // url=xx&isNeedLogin=yy&title=zz
                        String url = uri.getQueryParameter("url");
                        int isNeedLogin = 0;
                        String isNeedLoginStr = uri.getQueryParameter("isNeedLogin");
                        if (isNeedLoginStr != null) {
                            isNeedLogin = Integer.valueOf(isNeedLoginStr);
                        }
                        String title = "";
                        String htmlTitle = uri.getQueryParameter("title");
                        if (htmlTitle != null) {
                            title = htmlTitle;
                        }

                        launchAppParams = EasyJSONObject.generate(
                                "host", host,
                                "url", url,
                                "isNeedLogin", isNeedLogin,
                                "title", title);
                    } else if ("store".equals(host)) { // 2 跳轉店鋪首頁
                        String storeId = uri.getQueryParameter("storeId");
                        if (storeId != null) {
                            launchAppParams = EasyJSONObject.generate(
                                    "host", host,
                                    "storeId", Integer.valueOf(storeId));
                        }
                    } else if ("goods".equals(host)) { // 3 跳轉商品詳情
                        String commonId = uri.getQueryParameter("commonId");
                        if (commonId != null) {
                            launchAppParams = EasyJSONObject.generate(
                                    "host", host,
                                    "commonId", Integer.valueOf(commonId));
                        }
                    } else if ("memberinfo".equals(host)) { // 4 个人名片页
                        String memberName = uri.getQueryParameter("memberName");
                        if (memberName == null) {
                            memberName = "";
                        }

                        launchAppParams = EasyJSONObject.generate(
                                "host", host,
                                "memberName", memberName);
                    } else if ("postinfo".equals(host)) { // 5 贴文詳情页
                        String postId = uri.getQueryParameter("postId");
                        if (postId != null) {
                            launchAppParams = EasyJSONObject.generate(
                                    "host", host,
                                    "postId", Integer.valueOf(postId));
                        }
                    } else if ("recruitinfo".equals(host)) { // 6 招聘詳情页
                        String postId = uri.getQueryParameter("postId");
                        if (postId != null) {
                            launchAppParams = EasyJSONObject.generate(
                                    "host", host,
                                    "postId", Integer.valueOf(postId));
                        }
                    } else if ("activityindex".equals(host) || "home".equals(host)) { // 7 購物專場 8 商城首页
                        launchAppParams = EasyJSONObject.generate(
                                "host", host);
                    }

                    if (launchAppParams != null) {
                        SLog.info("launchAppParams[%s]", launchAppParams.toString());
                        Hawk.put(SPField.FIELD_LAUNCH_APP_PARAMS, launchAppParams.toString());
                    }

                }
            }
        } else {
            SLog.info("intent_is_null");
        }


        TextView tvCopyRight = findViewById(R.id.copyright);
        String copyright = getResources().getString(R.string.copyright);
        // 获取年份
        copyright = String.format(copyright, jarbon.getYear());

        tvCopyRight.setText(copyright);

        // 判斷是否需要啟動App引導頁(每天需要顯示一次引導頁)
        String lastShowAppGuideDate = Hawk.get(SPField.FIELD_SHOW_APP_GUIDE_DATE);
        String today = new Jarbon().toDateString();

        // lastShowAppGuideDate = null;
        SLog.info("lastShowAppGuideDate[%s], today[%s]", lastShowAppGuideDate, today);

        if (!today.equals(lastShowAppGuideDate)) { // 需要顯示App引導頁
            SLog.info("需要顯示App引導頁");
            startTargetActivity(AppGuideActivity.class);
        } else {
            SLog.info("【不】需要顯示App引導頁");
            startTargetActivity(MainActivity.class);
        }

        splashBackground = findViewById(R.id.splash_bg);

        // 獲取軟鍵盤的高度
        etStub = findViewById(R.id.et_stub);
        int softInputHeight = Hawk.get(SPField.FIELD_SOFT_INPUT_HEIGHT, SoftInputInfo.INVALID_HEIGHT);
        SLog.info("_softInputHeight[%d]", softInputHeight);
        if (softInputHeight == SoftInputInfo.INVALID_HEIGHT) { // 如果未用軟鍵盤的高度，則獲取
            softInputInfo = new SoftInputInfo();


            rlContainer = findViewById(R.id.rl_container);
            rlContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int height = rlContainer.getHeight();
                    SLog.info("rlContainer.height[%d]", height);
                    softInputInfo.setHeight(height);

                    int softInputHeight = softInputInfo.getSoftInputHeight();
                    if (softInputHeight != SoftInputInfo.INVALID_HEIGHT) {
                        Hawk.put(SPField.FIELD_SOFT_INPUT_HEIGHT, softInputHeight);
                    }
                }
            });
            rlContainer.post(new Runnable() {
                @Override
                public void run() {
                    Util.showSoftInput(SplashActivity.this, etStub);
                    rlContainer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Util.hideSoftInput(SplashActivity.this, etStub);
                        }
                    }, 500);
                }
            });
        } else { // 如果已经有保存软键盘高度，则隐藏etStub，否则软键盘还会自动弹出
            etStub.setVisibility(View.GONE);
        }


        // 下載App引導頁圖片任務
//        TwantApplication.getThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    String responseStr = Api.syncGet(Api.PATH_APP_GUIDE, null);
//                    SLog.info("responseStr[%s]", responseStr);
//                    if (StringUtil.isEmpty(responseStr)) {
//                        return;
//                    }
//
//                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
//                    if (responseObj == null) {
//                        return;
//                    }
//
//                    EasyJSONArray appGuideImageArray = responseObj.getSafeArray("datas.appGuideImage");
//                    for (Object object : appGuideImageArray) {
//                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
//                        String url = easyJSONObject.getSafeString("guideImage");
//                        File appGuideImageFile = FileUtil.getCacheFile(SplashActivity.this, url);
//                        if (!appGuideImageFile.exists()) { // 如果引導圖片不存在，則下載
//                            Api.syncDownloadFile(StringUtil.normalizeImageUrl(url), appGuideImageFile);
//                        }
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Util.enterFullScreen(this);
    }

    /**
     * 禁用返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 啟動指定的Activity
     * @param cls
     */
    private void startTargetActivity(Class<?> cls) {
        // delayMillis為啟動頁需要顯示的時間減去App啟動所耗的時間
        long delayMillis = SPLASH_DURATION - (System.currentTimeMillis() - appStartTime);
        if (delayMillis < 0) {
            delayMillis = 0;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, cls);
                startActivity(intent);
                finish();
            }
        }, delayMillis);
    }
}
