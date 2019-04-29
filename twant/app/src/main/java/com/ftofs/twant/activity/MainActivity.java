package com.ftofs.twant.activity;


import android.os.Bundle;

import com.ftofs.twant.R;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.util.Util;

/**
 * 主Activity
 * @author zwm
 */
public class MainActivity extends BaseActivity {
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
}
