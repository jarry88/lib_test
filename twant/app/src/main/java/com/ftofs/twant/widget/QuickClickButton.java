package com.ftofs.twant.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gzp.lib_common.utils.SLog;

public class QuickClickButton extends androidx.appcompat.widget.AppCompatTextView {
    public interface OnQuickClickListener {
        void onQuickClick(View view);
    }

    OnQuickClickListener onQuickClickListener;
    long lastClickTime = 0; // 最近一次的點擊時間
    int quickClickCount = 0;  // 目前為止，快速點擊了多少次

    /**
     * 連續快速點擊多少次才觸發快速點擊事件
     */
    public static final int QUICK_CLICK_TRIGGER_COUNT = 3;
    /**
     * 相鄰兩次的點擊時間相隔多少毫秒才算是快速點擊
     */
    public static final int QUICK_CLICK_TIME_INTERVAL = 300;


    public QuickClickButton(Context context) {
        this(context, null);
    }

    public QuickClickButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickClickButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            SLog.info("onTouchEvent.ACTION_UP");
            long now = System.currentTimeMillis();

            SLog.info("now[%s], lastClickTime[%s]", now, lastClickTime);
            if (now - lastClickTime < QUICK_CLICK_TIME_INTERVAL) {
                quickClickCount++;
            } else {
                quickClickCount = 1;  // 如果間隔太久時間沒有點擊，當作第一次點擊
            }
            lastClickTime = now;

            SLog.info("quickClickCount[%d]", quickClickCount);
            if (quickClickCount >= QUICK_CLICK_TRIGGER_COUNT && onQuickClickListener != null) {
                onQuickClickListener.onQuickClick(this);
                quickClickCount = 0;
                lastClickTime = 0;
            }
        }

        return true; // 需要返回true，才會有后續的ACTION_UP事件
    }

    public void setOnQuickClickListener(OnQuickClickListener onQuickClickListener) {
        this.onQuickClickListener = onQuickClickListener;
    }
}
