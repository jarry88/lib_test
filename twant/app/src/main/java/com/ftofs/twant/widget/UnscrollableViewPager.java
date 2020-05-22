package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * 不可滾動的ViewPager
 * 參考
 * ViewPager+Fragment的实现及禁止滑动   https://blog.csdn.net/lanrenxiaowen/article/details/72528422
 */
public class UnscrollableViewPager extends ViewPager {
    private boolean isCanScroll = true;
    public UnscrollableViewPager(@NonNull Context context) {
        super(context);
    }

    public UnscrollableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置其是否能滑动换页
     * @param isCanScroll false 不能换页， true 可以滑动换页
     */
    public void setCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onTouchEvent(ev);

    }
}
