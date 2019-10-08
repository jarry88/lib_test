package com.ftofs.twant.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AppGuidePagerAdapter;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

/**
 * App啟動引導頁
 * 每天最多顯示一次
 * @author zwm
 */
public class AppGuideActivity extends BaseActivity implements OnSelectedListener {
    ViewPager vpAppGuide;
    List<String> imageList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        // 記錄最近一次顯示APP引導頁的日期
        Hawk.put(SPField.FIELD_SHOW_APP_GUIDE_DATE, new Jarbon().toDateString());

        vpAppGuide = findViewById(R.id.vp_app_guide);
        imageList.add("image/57/76/5776e2ca8a1403530fca6e4bc097ff39.png");
        imageList.add("image/2c/a4/2ca495d4b0b55074ab110e1767288b8b.png");
        imageList.add("image/05/3c/053cd519a2feaa1cc40a4bde2a58a625.png");

        AppGuidePagerAdapter adapter = new AppGuidePagerAdapter(this, imageList, this);
        vpAppGuide.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Util.enterFullScreen(this);
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        // 開始體驗
        SLog.info("開始體驗");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
