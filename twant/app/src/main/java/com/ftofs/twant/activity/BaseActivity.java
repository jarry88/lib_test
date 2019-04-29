package com.ftofs.twant.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * 所有Activity的公共基類
 * @author zwm
 */
public class BaseActivity extends SupportActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全部activity禁用横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
