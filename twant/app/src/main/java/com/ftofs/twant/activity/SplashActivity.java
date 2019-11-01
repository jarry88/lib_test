package com.ftofs.twant.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.MobileZone;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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

        // 下載App引導頁圖片任務
        TwantApplication.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String responseStr = Api.syncGet(Api.PATH_APP_GUIDE, null);
                    SLog.info("responseStr[%s]", responseStr);
                    if (StringUtil.isEmpty(responseStr)) {
                        return;
                    }

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (responseObj == null) {
                        return;
                    }

                    EasyJSONArray appGuideImageArray = responseObj.getArray("datas.appGuideImage");
                    for (Object object : appGuideImageArray) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        String url = easyJSONObject.getString("guideImage");
                        File appGuideImageFile = FileUtil.getCacheFile(SplashActivity.this, url);
                        if (!appGuideImageFile.exists()) { // 如果引導圖片不存在，則下載
                            Api.syncDownloadFile(StringUtil.normalizeImageUrl(url), appGuideImageFile);
                        }
                    }
                } catch (Exception e) {

                }
            }
        });
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
