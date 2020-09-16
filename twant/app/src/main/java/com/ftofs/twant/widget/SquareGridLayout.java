package com.ftofs.twant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.fragment.GifFragment;
import com.ftofs.twant.fragment.ImageFragment;
import com.ftofs.twant.fragment.ImageViewerFragment;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 正方形Grid布局
 * !!!!!!注意：
 * 1. 暫不支持設置Padding
 * 2. SquareGridLayout的寬度不能為 WRAP_CONTENT, 也就是說，只能設置為 MATCH_PARENT 或指定具體的寬度
 * 3. 所有子元素的寬度和高度都只能設置為 MATCH_PARENT
 */
public class SquareGridLayout extends ViewGroup {
    public List<String> imageList;
    /**
     * 每行多少列
     */
    private int columnCount;

    public SquareGridLayout(Context context) {
        this(context, null);
    }

    public SquareGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SquareGridLayout, defStyleAttr, 0);
        columnCount = array.getInteger(R.styleable.SquareGridLayout_sgl_column_count, 1);
        array.recycle();
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        /*
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        int nonGoneChildCount = 0;


        int itemWidthOrig = sizeWidth / columnCount;
        int itemHeightOrig = itemWidthOrig;


        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                ++nonGoneChildCount;
            }
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();

            int itemWidth = itemWidthOrig - marginLayoutParams.leftMargin - marginLayoutParams.rightMargin;
            int itemHeight = itemHeightOrig - marginLayoutParams.topMargin - marginLayoutParams.bottomMargin;
            // SLog.info("itemWidth[%d], itemHeight[%d]", itemWidth, itemHeight);

            // 计算出所有的childView的宽和高
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY);
            measureChild(childView, childWidthMeasureSpec, childHeightMeasureSpec);
        }

        sizeHeight = itemWidthOrig * ((nonGoneChildCount + columnCount - 1) / columnCount);  // 總高度

        /*
        SLog.info("onMeasure, childCount[%d], sizeWidth[%d], sizeHeight[%d], itemWidth[%d], itemHeight[%d]",
                childCount, sizeWidth, sizeHeight, itemWidthOrig, itemHeightOrig);
                */

        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int childCount = getChildCount();

        int x = 0;
        int y = 0;
        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         */
        int index = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) {
                continue;
            }

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView.getLayoutParams();

            /*
            SLog.info("childWidth[%d], childHeight[%d], margins[%d][%d][%d][%d]",
                    childWidth, childHeight, marginLayoutParams.topMargin, marginLayoutParams.bottomMargin,
                    marginLayoutParams.leftMargin, marginLayoutParams.rightMargin);
                    */

            int remainder = index % columnCount;
            if (remainder == 0) {
                x = 0;

                // 重新另立一行
                if (index > 0) {
                    y += childHeight + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
                }
            } else {
                x += childWidth + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                // SLog.info("x=%d", x);
            }

            int left = x + marginLayoutParams.leftMargin;
            int top = y + marginLayoutParams.topMargin;
            int right = left + childWidth;
            int bottom = top + childHeight;
            // SLog.info("left[%d], top[%d], right[%d], bottom[%d]", left, top, right, bottom);
            childView.layout(left, top, right, bottom);

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

    /**
     * 添加圖片視圖
     * @param imageView
     * @param params
     */
    public void addImageView(ImageView imageView, LayoutParams params, String imageUrl){
        Context context=imageView.getContext();
        if (params == null) {
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.topMargin = Util.dip2px(context, 3);
            layoutParams.bottomMargin = Util.dip2px(context, 3);
            layoutParams.leftMargin = Util.dip2px(context, 3);
            layoutParams.rightMargin = Util.dip2px(context, 3);
            params=layoutParams;
        }
        if (!StringUtil.isEmpty(imageUrl)) {
            Glide.with(context).load(StringUtil.normalizeImageUrl(imageUrl)).centerCrop().into(imageView);
            imageView.setOnClickListener(v -> {
                if (imageUrl.endsWith(".gif")) { // 如果是Gif，顯示Gif動圖
                    Util.startFragment(GifFragment.newInstance(StringUtil.normalizeImageUrl(imageUrl)));
                } else if (imageList != null && imageList.size() > 0) { // 做成可翻頁的圖片列表展示
                    int currImageIndex = (int) ((DataImageView) v).getCustomData();
                    Util.startFragment(ImageFragment.newInstance(currImageIndex, imageList));
                } else {
                    Util.startFragment(ImageViewerFragment.newInstance(StringUtil.normalizeImageUrl(imageUrl)));
                }
            });
        }

        addView(imageView,params);
    }

    @Override
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        super.addFocusables(views, direction, focusableMode);
    }
}
