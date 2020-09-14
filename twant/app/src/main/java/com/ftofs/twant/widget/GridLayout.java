package com.ftofs.twant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.gzp.lib_common.utils.SLog;

/**
 * 網格布局
 * !!!!!!注意：
 * 1. 暫不支持設置Padding
 * 2. GridLayout的寬度不能為 WRAP_CONTENT, 也就是說，只能設置為 MATCH_PARENT 或指定具體的寬度
 * 3. 所有子元素的寬度只能設置為 MATCH_PARENT，高度設置為 WRAP_CONTENT或指定具體的高度
 * @author zwm
 */
public class GridLayout extends ViewGroup {
    /**
     * 每行多少列
     */
    private int columnCount;

    public GridLayout(Context context) {
        this(context, null);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GridLayout, defStyleAttr, 0);
        columnCount = array.getInteger(R.styleable.GridLayout_gl_column_count, 1);
        SLog.info("columnCount[%d]", columnCount);
        array.recycle();
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /*
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();


        int itemWidthOrig = sizeWidth / columnCount;

        int height = 0; // 計算出容器的高度


        int index = 0;
        int maxRowChildHeight = 0; // 每一行的子View所占據的最大高度
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }


            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();

            int itemWidth = itemWidthOrig - marginLayoutParams.leftMargin - marginLayoutParams.rightMargin;
            int itemHeight = sizeHeight - marginLayoutParams.topMargin - marginLayoutParams.bottomMargin;
            // SLog.info("itemWidth[%d], itemHeight[%d]", itemWidth, itemHeight);

            // 计算出所有的childView的宽和高
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.AT_MOST);

            measureChild(childView, childWidthMeasureSpec, childHeightMeasureSpec);
            itemHeight = childView.getMeasuredHeight();
            // SLog.info("itemHeight[%d]", itemHeight);

            if (maxRowChildHeight < marginLayoutParams.topMargin + itemHeight + marginLayoutParams.bottomMargin) {
                maxRowChildHeight = marginLayoutParams.topMargin + itemHeight + marginLayoutParams.bottomMargin;
            }

            if (index % columnCount == columnCount - 1) {
                height += maxRowChildHeight;
                maxRowChildHeight = 0;
            }

            index++;
        }

        // 如果結尾不是完整的一行，還要再加上結尾的處理
        if (maxRowChildHeight > 0) {
            height += maxRowChildHeight;
            maxRowChildHeight = 0;
        }

        /*
        SLog.info("onMeasure, childCount[%d], sizeWidth[%d], sizeHeight[%d], itemWidth[%d], itemHeight[%d]",
                childCount, sizeWidth, sizeHeight, itemWidthOrig, itemHeightOrig);
                */

        setMeasuredDimension(sizeWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        int x = 0;
        int y = 0;
        int index = 0;

        int maxRowChildHeight = 0; // 每一行的子View所占據的最大高度
        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }

            if (index % columnCount == 0) {
                x = 0;
                y += maxRowChildHeight;
                maxRowChildHeight = 0;
            }

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();


            int left = x + marginLayoutParams.leftMargin;
            int top = y + marginLayoutParams.topMargin;
            int right = left + childWidth;
            int bottom = top + childHeight;
            // SLog.info("left[%d], top[%d], right[%d], bottom[%d]", left, top, right, bottom);
            childView.layout(left, top, right, bottom);

            x = right + marginLayoutParams.rightMargin;
            if (maxRowChildHeight < marginLayoutParams.topMargin + childHeight + marginLayoutParams.bottomMargin) {
                maxRowChildHeight = marginLayoutParams.topMargin + childHeight + marginLayoutParams.bottomMargin;
            }

            index++;
        }
    }

    /**
     * 獲取列數
     * @return
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * 設置列數
     * @param columnCount
     */
    public void setColumnCount(int columnCount) {
        if (columnCount < 1) {
            columnCount = 1;
        }

        if (this.columnCount != columnCount) {
            // 請求重新布局
            this.columnCount = columnCount;
            requestLayout();
        }
    }
}
