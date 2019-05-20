package com.ftofs.twant.activity;


import android.os.Bundle;
import android.widget.Toast;

import com.ftofs.twant.R;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

/**
 * 主Activity
 * @author zwm
 */
public class MainActivity extends BaseActivity {
    long lastBackPressedTime;
    MainFragment mainFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = findFragment(MainFragment.class);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            loadRootFragment(R.id.main_fragment_container, mainFragment);
        }

        int color = getResources().getColor(R.color.tw_red, null);
        Util.setWindowStatusBarColor(this, color);
    }

    @Override
    public void onBackPressedSupport() {
        long now = Time.timestampMillis();
        // 兩次按返回鍵的時間差
        long diff = now - lastBackPressedTime;
        lastBackPressedTime = now;

        if (diff >= 2000) {
            ToastUtil.show(this, "再按一次退出應用");
        } else {
            // 在時間差內兩次按下返回鍵，退出
            finish();
        }
    }
}
