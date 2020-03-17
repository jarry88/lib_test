package com.ftofs.twant.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AppGuidePagerAdapter;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.HwLoadingView;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * App啟動引導頁
 * 每天最多顯示一次
 * @author zwm
 */
public class AppGuideActivity extends BaseActivity implements SimpleCallback {
    public static final int EVENT_TYPE_START_MAIN_ACTIVITY = 1; // 啟動MainActivity

    boolean lastPage = false; // 標記當前是否處於最後一頁

    private boolean mIsScrolled;
    ViewPager vpAppGuide;
    int currPage = 0;

    HwLoadingView vwLoading;

    boolean showFramework;  // 是否显示引导页框架
    List<String> imageList = new ArrayList<>(); // 引导页背景图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        vwLoading = findViewById(R.id.vw_loading);
        vwLoading.setVisibility(View.VISIBLE);
        vpAppGuide = findViewById(R.id.vp_app_guide);

        Intent intent = getIntent();

        String dataStr = intent.getStringExtra("dataStr");
        SLog.info("dataStr[%s]", dataStr);
        try {
            EasyJSONObject dataObj = EasyJSONObject.parse(dataStr);
            showFramework = dataObj.getBoolean("showFramework");

            for (Object object : dataObj.getArray("imageList")) {
                String imageUrl = (String) object;
                imageList.add(imageUrl);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        showAppGuide(dataStr);
    }


    private void showAppGuide(String dataStr) {
        AppGuidePagerAdapter adapter = new AppGuidePagerAdapter(this, showFramework, imageList, this);
        vpAppGuide.setAdapter(adapter);
        vpAppGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currPage = i;
                SLog.info("currPage[%d]", currPage);
                if (currPage == imageList.size() - 1) {
                    lastPage = true;

                    vpAppGuide.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (lastPage) {
                                startMainActivity();
                            }
                        }
                    }, 5000);
                } else {
                    lastPage = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                /*
                实现ViewPager最后一页向后滑动监听
                https://www.jianshu.com/p/2d03d06a6adf
                当滑动ViewPager的时候，如果处于第一页，继续向前滑动的时候不会产生SCROLL_STATE_SETTLING状态，
                但是如果向下一页方向滑动，则必然会产生SCROLL_STATE_SETTLING状态；如果处于最后一页，继续往后滑动，
                不会产生SCROLL_STATE_SETTLING状态，但是往前一页滑动，也必然会产生SCROLL_STATE_SETTLING状态。
                 */

                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mIsScrolled = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        mIsScrolled = true;
                        break;

                    case ViewPager.SCROLL_STATE_IDLE:
                        if (!mIsScrolled ) {
                            // TODO     你想要实现的操作
                            if (currPage > 0) {
                                startMainActivity();
                            }
                        }
                        mIsScrolled = true;
                        break;
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        Util.enterFullScreen(this);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSimpleCall(Object data) {
        EasyJSONObject dataObj = (EasyJSONObject) data;

        try {
            int eventType = dataObj.getInt("event_type");
            if (eventType == EVENT_TYPE_START_MAIN_ACTIVITY) {
                startMainActivity();
            }

        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
}
