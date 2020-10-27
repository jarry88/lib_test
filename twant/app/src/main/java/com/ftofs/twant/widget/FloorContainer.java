package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.FloorItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.utils.SLog;

public class FloorContainer extends ViewGroup {
    public static final int SPAN_COUNT = 2; // 每行2列
    public static final int HORIZONTAL_MARGIN_DP = 11; // 圖片間的水平間距(dp)
    public static final int VERTICAL_MARGIN_DP = 8; // 圖片間的垂直間距(dp)
    int horizontalMarginPx = 0;
    int verticalMarginPx = 0;
    int itemWidth = 0; // 每一项的宽度

    public FloorContainer(@NonNull Context context) {
        this(context, null);
    }

    public FloorContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloorContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        horizontalMarginPx = Util.dip2px(context, HORIZONTAL_MARGIN_DP);
        verticalMarginPx = Util.dip2px(context, VERTICAL_MARGIN_DP);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        itemWidth = (width - horizontalMarginPx) / SPAN_COUNT;

        int leftHeight = 0; // 左边已占用的高度
        int rightHeight = 0; // 右边已占用的高度

        int childCount = getChildCount();
        SLog.info("childCount[%d]", childCount);
        for (int i = 0; i < childCount; i++) {
            ImageView childView = (ImageView) getChildAt(i);
            childView.setBackgroundColor(Color.BLACK);
            FloorItem item = (FloorItem) childView.getTag(R.id.key_meta_data);

            // 计算图片显示的高度
            int itemHeight = itemWidth * item.imageHeight / item.imageWidth;
            SLog.info("itemHeight[%d]", itemHeight);

            if (leftHeight <= rightHeight) {
                leftHeight = leftHeight + itemHeight + verticalMarginPx;
            } else {
                rightHeight = rightHeight + itemHeight + verticalMarginPx;
            }
        }
        int finalHeight = Math.max(leftHeight, rightHeight);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.AT_MOST);

        SLog.info("childCount[%d], width[%d], height[%d], finalHeight[%d]", childCount, width, height, finalHeight);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int leftHeight = 0; // 左边已占用的高度
        int rightHeight = 0; // 右边已占用的高度

        int childCount = getChildCount();
        SLog.info("childCount[%d]", childCount);
        for (int i = 0; i < childCount; i++) {
            ImageView childView = (ImageView) getChildAt(i);
            // childView.setImageResource(R.drawable.cs_avatar_160);

            FloorItem item = (FloorItem) childView.getTag(R.id.key_meta_data);

            Glide.with(getContext()).load(StringUtil.normalizeImageUrl(item.imageName)).into(childView);

            // 计算图片显示的高度
            int itemHeight = itemWidth * item.imageHeight / item.imageWidth;
            SLog.info("itemHeight[%d]", itemHeight);

            if (leftHeight <= rightHeight) {
                childView.layout(0, leftHeight, itemWidth, leftHeight + itemHeight);
                leftHeight = leftHeight + itemHeight + verticalMarginPx;
            } else {
                childView.layout(itemWidth + horizontalMarginPx, rightHeight, 2 * itemWidth + horizontalMarginPx, rightHeight + itemHeight);
                rightHeight = rightHeight + itemHeight + verticalMarginPx;
            }
        }
    }
}

