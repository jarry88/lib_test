package com.ftofs.twant.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.Util;

/**
 * 啟動頁面
 * @author zwm
 */
public class SplashActivity extends BaseActivity {
    private static final int SPLASH_DURATION = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_acivity);

        TextView tvCopyRight = findViewById(R.id.copyright);
        String copyright = getResources().getString(R.string.copyright);
        // 获取年份
        copyright = String.format(copyright, Time.date("Y"));

        tvCopyRight.setText(copyright);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DURATION);
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
}
