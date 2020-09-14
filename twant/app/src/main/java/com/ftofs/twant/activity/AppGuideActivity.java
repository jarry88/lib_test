package com.ftofs.twant.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AppGuidePagerAdapter;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.HwLoadingView;
import com.gzp.lib_common.base.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * App啟動引導頁
 * 每天最多顯示一次
 * @author zwm
 */
public class AppGuideActivity extends BaseActivity implements View.OnClickListener, SimpleCallback {
    public static final int EVENT_TYPE_START_MAIN_ACTIVITY = 1; // 啟動MainActivity
    public static final int EVENT_TYPE_GOTO_NEXT_PAGE = 2; // 轉到下一頁

    boolean lastPage = false; // 標記當前是否處於最後一頁

    private boolean mIsScrolled;
    ViewPager vpAppGuide;
    int currPage = 0;

    HwLoadingView vwLoading;

    boolean showFramework;  // 是否显示引导页框架
    List<String> imageList = new ArrayList<>(); // 引导页背景图

    // 最近一次操作的時間戳
    long lastActionTimestamp = System.currentTimeMillis() + 1000; // 加1000毫秒是為了考慮加載圖片所需的耗時
    Timer timer;
    TimerHandler timerHandler;
    static class TimerHandler extends Handler {
        WeakReference<AppGuideActivity> weakReference;

        public TimerHandler(AppGuideActivity appGuideActivity) {
            weakReference = new WeakReference<>(appGuideActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            AppGuideActivity appGuideActivity = weakReference.get();
            if (appGuideActivity != null) {
                appGuideActivity.doTimerAction();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);

        vwLoading = findViewById(R.id.vw_loading);
        vwLoading.setVisibility(View.VISIBLE);
        vpAppGuide = findViewById(R.id.vp_app_guide);

        timerHandler = new TimerHandler(this);

        findViewById(R.id.btn_skip_app_guide).setOnClickListener(this);

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
        showAppGuide();
    }


    private void showAppGuide() {
        AppGuidePagerAdapter adapter = new AppGuidePagerAdapter(this, showFramework, imageList, this);
        vpAppGuide.setAdapter(adapter);
        vpAppGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currPage = i;
                lastActionTimestamp = System.currentTimeMillis();
                SLog.info("____currPage[%d]", currPage);
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

    public void doTimerAction() {
        long now = System.currentTimeMillis();

        if (now - lastActionTimestamp > 3000) {
            SLog.info("時間到達");
            if (lastPage) {
                SLog.info("跳轉到首頁");
                startMainActivity();
            } else {
                SLog.info("跳轉到下一頁");
                gotoNextPage();
            }
        }
    }


    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }

        // 定时服务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long threadId = Thread.currentThread().getId();
                SLog.info("threadId[%d]", threadId);
                Message message = new Message();

                if (timerHandler != null) {
                    timerHandler.sendMessage(message);
                }
            }
        }, 500, 500);  // 0.5秒后启动，每隔0.5秒运行一次
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Util.enterFullScreen(this);

        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopTimer();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoNextPage() {
        vpAppGuide.setCurrentItem(vpAppGuide.getCurrentItem() + 1);
    }


    @Override
    public void onSimpleCall(Object data) {
        EasyJSONObject dataObj = (EasyJSONObject) data;

        try {
            int eventType = dataObj.getInt("event_type");
            if (eventType == EVENT_TYPE_START_MAIN_ACTIVITY) {
                startMainActivity();
            } else if (eventType == EVENT_TYPE_GOTO_NEXT_PAGE) {
                gotoNextPage();
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_skip_app_guide) {
            startMainActivity();
        }
    }
}
