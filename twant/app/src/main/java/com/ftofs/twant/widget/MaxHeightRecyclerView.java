package com.ftofs.twant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.ftofs.twant.R;


/**
 * 可以设置最大高度的recyclerView，在布局里使用 maxHeight属性指定最大高度
 *
 * @author Huangming  2019/4/8
 */
public class MaxHeightRecyclerView extends RecyclerView {
    private int mMaxHeight;

    /**
     * 设置最大高度
     *
     * @param maxHeight 最大高度 px
     */
    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
        // 重绘 RecyclerView
        requestLayout();
    }

    public MaxHeightRecyclerView(Context context) {
        super(context);
    }


    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView);
        mMaxHeight = arr.getLayoutDimension(R.styleable.MaxHeightRecyclerView_maxHeight, mMaxHeight);
        arr.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (mMaxHeight > 0) {
            /*
            AT_MOST 任意尺寸，但最大不超过size指定的尺寸
            所以上面这行代码的意思就是：这个RecyclerView的高度可以是任意的，但最大不超过maxHeight px
             */
            heightSpec = View.MeasureSpec.makeMeasureSpec(mMaxHeight, View.MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
    }


}

