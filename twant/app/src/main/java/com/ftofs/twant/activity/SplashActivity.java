package com.ftofs.twant.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.SoftInputInfo;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.base.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.base.BaseActivity;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

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

        SLog.info("Launch performance...");

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

                        /*
                        判斷是否是推廣頁面過來的
                        landingPage=home&source=TW100000&medium=ADV
                         */
                        String source = uri.getQueryParameter("source");
                        String landingPage = uri.getQueryParameter("landingPage");
                        String medium = uri.getQueryParameter("medium");
                        SLog.info("source[%s], landingPage[%s], medium[%s]", source, landingPage, medium);
                        if (!StringUtil.isEmpty(source)) { // 如果是，則統計請求
                            Util.promotionStatsRequest(this, source, medium, landingPage);
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
                    } else if ("activityindex".equals(host) || "home".equals(host) || "crossCity".equals(host)) { // 7 購物專場 8 商城首页
                        launchAppParams = EasyJSONObject.generate(
                                "host", host);
                    } else if ("bargain".equals(host)) {
                        String bargainId = uri.getQueryParameter("bargainId");
                        String commonId = uri.getQueryParameter("commonId");
                        String goodsId = uri.getQueryParameter("goodsId");
                        launchAppParams = EasyJSONObject.generate(
                                "host", host,
                                "bargainId", Integer.valueOf(bargainId),
                                "commonId", Integer.valueOf(commonId),
                                "goodsId", Integer.valueOf(goodsId)
                        );
                    } else if ("bargainShare".equals(host)) {
                        String openId = uri.getQueryParameter("openId");
                        String commonId = uri.getQueryParameter("commonId");
                        String goodsId = uri.getQueryParameter("goodsId");
                        launchAppParams = EasyJSONObject.generate(
                                "host", host,
                                "openId", Integer.valueOf(openId),
                                "commonId", Integer.valueOf(commonId),
                                "goodsId", Integer.valueOf(goodsId)
                        );
                    } else if ("activityindexNew".equals(host)) {
                        String zoneId = uri.getQueryParameter("zoneId");
                        launchAppParams = EasyJSONObject.generate(
                                "host", host,
                                "zoneId", Integer.valueOf(zoneId)
                        );
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

        loadAppGuideData();

        EasyJSONObject params = EasyJSONObject.generate("version", "1.0.0.0");
        SLog.info("params[%s]", params);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Util.enterFullScreen(this);

        Util.getStatusbarHeight(this); // 为了初始化一下状态栏高度的保存
    }

    /**
     * 加载是否显示引导页的数据
     */
    private void loadAppGuideData() {
        Api.getUI(Api.PATH_APP_GUIDE, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(SplashActivity.this, e);
                startTargetActivity(MainActivity.class, null); // 出错的话，直接显示MainActivity
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(SplashActivity.this, responseObj)) {
                        startTargetActivity(MainActivity.class, null); // 出错的话，直接显示MainActivity
                        return;
                    }

                    int isOpenGuide = Integer.parseInt(responseObj.getSafeString("datas.isOpenGuide"));
                    if (isOpenGuide != Constant.TRUE_INT) { // 如果不开启引导页，直接显示MainActivity
                        startTargetActivity(MainActivity.class, null);
                        return;
                    }

                    // 引導頁顯示頻率 1-每天首次啟動顯示 2-每次啟動顯示 3-每次更新後顯示
                    int guideFrequency = Integer.parseInt(responseObj.getSafeString("datas.guideFrequency"));
                    if (guideFrequency == 1) { // 1-每天首次啟動顯示
                        // 判斷是否需要啟動App引導頁(每天需要顯示一次引導頁)
                        String lastShowAppGuideDate = Hawk.get(SPField.FIELD_SHOW_APP_GUIDE_DATE);
                        String today = new Jarbon().toDateString();

                        // lastShowAppGuideDate = null;
                        SLog.info("lastShowAppGuideDate[%s], today[%s]", lastShowAppGuideDate, today);

                        if (today.equals(lastShowAppGuideDate)) { // 不需要顯示App引導頁，直接顯示MainActivity
                            SLog.info("今天【不】需要顯示App引導頁");
                            startTargetActivity(MainActivity.class, null);
                            return;
                        }
                    } else if (guideFrequency == 3) { // 3-每次更新後顯示
                        int lastShowAppGuideVersion = Hawk.get(SPField.FIELD_SHOW_APP_GUIDE_VERSION, 0);
                        SLog.info("lastShowAppGuideVersion[%d], version[%s]", lastShowAppGuideVersion, BuildConfig.VERSION_CODE);

                        if (lastShowAppGuideVersion == BuildConfig.VERSION_CODE) {
                            SLog.info("當前版本【不】需要顯示App引導頁");
                            startTargetActivity(MainActivity.class, null);
                            return;
                        }
                    }

                    // 記錄最近一次顯示APP引導頁的日期和版本号
                    Hawk.put(SPField.FIELD_SHOW_APP_GUIDE_DATE, new Jarbon().toDateString());
                    Hawk.put(SPField.FIELD_SHOW_APP_GUIDE_VERSION, BuildConfig.VERSION_CODE);

                    int isOpenGuideFrame = Integer.parseInt(responseObj.getSafeString("datas.isOpenGuideFrame"));

                    EasyJSONArray appGuideImageArray = responseObj.getSafeArray("datas.appGuideImage");
                    EasyJSONArray imageList = EasyJSONArray.generate();
                    for (Object object : appGuideImageArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        String url = easyJSONObject.getSafeString("guideImage");
                        imageList.append(url);
                    }

                    EasyJSONObject appGuideDataObj = EasyJSONObject.generate(
                            "showFramework", isOpenGuideFrame == Constant.TRUE_INT,
                            "imageList", imageList
                    );

                    startTargetActivity(AppGuideActivity.class, appGuideDataObj.toString());
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    startTargetActivity(MainActivity.class, null); // 出错的话，直接显示MainActivity
                }
            }
        });
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
     * @param dataStr 传递给目标Activity的参数
     */
    private void startTargetActivity(Class<?> cls, String dataStr) {
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
                if (dataStr != null) {
                    intent.putExtra("dataStr", dataStr);
                }
                startActivity(intent);
                finish();
            }
        }, delayMillis);
    }
}
