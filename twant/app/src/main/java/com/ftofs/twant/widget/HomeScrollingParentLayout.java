package com.ftofs.twant.widget;


import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.NestedScrollingCallback;
import com.ftofs.twant.interfaces.StickyStatusCallback;
import com.ftofs.twant.log.SLog;

/**
 * 商城首頁滾動頁面
 * @author zwm
 */
public class HomeScrollingParentLayout extends LinearLayout implements NestedScrollingParent2 {
    private View mTopView;
    private ViewGroup mStickyViewContainer;
    private View mStickyView;
    private View mCarouselView;
    private View mContentView;

    int mTotalHeight;

    int mStickyViewHeight;

    private int mTopViewHeight;

    int containerViewHeight; // 就是自己的高度

    boolean stickyViewFloat;

    StickyStatusCallback stickyStatusCallback;
    NestedScrollingCallback nestedScrollingCallback;

    public void setStickyStatusCallback(StickyStatusCallback stickyStatusCallback) {
        this.stickyStatusCallback = stickyStatusCallback;
    }

    public void setNestedScrollingCallback(NestedScrollingCallback nestedScrollingCallback) {
        this.nestedScrollingCallback = nestedScrollingCallback;
    }

    private NestedScrollingParentHelper mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

    public HomeScrollingParentLayout(Context context) {
        this(context, null);
    }

    public HomeScrollingParentLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeScrollingParentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        // SLog.info("onStartNestedScroll");
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {

        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    /**
     * 在嵌套滑动的子View未滑动之前，判断父view是否优先与子view处理(也就是父view可以先消耗，然后给子view消耗）
     *
     * @param target   具体嵌套滑动的那个子类
     * @param dx       水平方向嵌套滑动的子View想要变化的距离
     * @param dy       垂直方向嵌套滑动的子View想要变化的距离 dy<0向下滑动 dy>0 向上滑动
     * @param consumed 这个参数要我们在实现这个函数的时候指定，回头告诉子View当前父View消耗的距离
     *                 consumed[0] 水平消耗的距离，consumed[1] 垂直消耗的距离 好让子view做出相应的调整
     * @param type     滑动类型，ViewCompat.TYPE_NON_TOUCH fling效果,ViewCompat.TYPE_TOUCH 手势滑动
     */
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        //这里不管手势滚动还是fling都处理
        /*
        boolean hideTop = dy > 0 && getScrollY() < mTopViewHeight;
        boolean showTop = dy < 0 && getScrollY() >= 0 && !target.canScrollVertically(-1);
        if (hideTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
         */

        if (dy == 0) {
            return;
        }

        if (nestedScrollingCallback != null) {
            nestedScrollingCallback.onCbStartNestedScroll();
        }

        int consumedY = 0;
        int scrollY = getScrollY();

        stickyViewFloat = isStickyViewFloat();
        if (dy > 0) { // dy > 0, 向上滾動
            if (!stickyViewFloat && scrollY + dy > mTopViewHeight) {
                mStickyViewContainer.removeView(mStickyView);
                stickyStatusCallback.changeStickyStatus(true, mStickyView);
            }

            if (scrollY + dy > getMaxHeight()) {
                consumedY = getMaxHeight() - scrollY;
                if (consumedY < 0) {
                    consumedY = 0;
                }
            } else {
                consumedY = dy;
            }
        } else { // dy < 0, 向下滾動
            if (stickyViewFloat && scrollY + dy <= mTopViewHeight) {
                stickyStatusCallback.changeStickyStatus(false, null);
                mStickyViewContainer.addView(mStickyView);

                consumedY = mTopViewHeight - scrollY;
            }
        }

        // SLog.info("consumedY[%d]", consumedY);

        if (consumedY != 0) {
            scrollBy(0, consumedY);
            consumed[1] = consumedY;
        }
    }


    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //当子控件处理完后，交给父控件进行处理。
        if (dyUnconsumed < 0) {//表示已经向下滑动到头
            scrollBy(0, dyUnconsumed);
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {

        return false;
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        // SLog.info("onStopNestedScroll");
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            System.out.println("onStopNestedScroll");
        }

        mNestedScrollingParentHelper.onStopNestedScroll(target, type);

        if (nestedScrollingCallback != null) {
            nestedScrollingCallback.onCbStopNestedScroll();
        }
    }


    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //ViewPager修改后的高度= 总高度-导航栏高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        containerViewHeight = getMeasuredHeight();

        ViewGroup.LayoutParams layoutParams = mContentView.getLayoutParams();
        layoutParams.height = containerViewHeight - mStickyView.getMeasuredHeight();
        mContentView.setLayoutParams(layoutParams);

        mTotalHeight = mTopView.getMeasuredHeight() + mStickyViewContainer.getMeasuredHeight() + mCarouselView.getMeasuredHeight();
        // SLog.info("mTotalHeight[%d], carouselViewHeight[%d]", mTotalHeight,  + mCarouselView.getMeasuredHeight());

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = findViewById(R.id.top_view);
        mStickyViewContainer = findViewById(R.id.sticky_view_container);
        mStickyView = findViewById(R.id.sticky_view);
        mCarouselView = findViewById(R.id.carousel_view);
        mContentView = findViewById(R.id.content_view);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
        mStickyViewHeight = mStickyView.getMeasuredHeight();
        SLog.info("mTopViewHeight[%d], mStickyViewHeight[%d]", mTopViewHeight, mStickyViewHeight);
    }

    @Override
    public void scrollTo(int x, int y) {
        SLog.info("here");
        if (y < 0) {
            y = 0;
        }

        stickyViewFloat = isStickyViewFloat();
        SLog.info("y[%d], maxHeight[%d], totalHeight[%d], stickyViewFloat[%s]",
                y, getMaxHeight(), mTotalHeight, stickyViewFloat);
        if (y > getMaxHeight()) {
            y = getMaxHeight();
            // SLog.info("in sticky status");
        } else {
            // SLog.info("out sticky status");
        }

        if (y == 0 && stickyViewFloat) { // 用于处理返顶的功能
            stickyStatusCallback.changeStickyStatus(false, null);
            mStickyViewContainer.addView(mStickyView);
        }
        super.scrollTo(x, y);
    }

    public int getMaxHeight() {
        return mTotalHeight - mStickyViewHeight;
    }

    public boolean isStickyViewFloat() {
        return mStickyViewContainer.getChildCount() == 0;
    }
}

