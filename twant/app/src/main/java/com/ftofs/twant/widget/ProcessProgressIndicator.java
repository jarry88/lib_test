package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * 退款、退貨、投訴處理流程的指示器
 * @author zwm
 */
public class ProcessProgressIndicator extends ViewGroup {
    Context context;
    Paint paint;

    int currentStep;
    int lineWidth;
    int colorFinished;
    int colorUnfinished;

    // 指示圖標頂部的外邊距
    int iconMarginTop;
    private boolean upToDown =true;
    private int finishResId=R.drawable.icon_refund_finished;
    private int unfinishResId=R.drawable.icon_refund_unfinished;

    public ProcessProgressIndicator(Context context) {
        this(context, null);
    }

    public ProcessProgressIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessProgressIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        colorFinished = Color.WHITE;
        colorUnfinished = Color.parseColor("#FFFA80AB");

        // 進度線條是2dp大小
        lineWidth = Util.dip2px(context, 2);
        iconMarginTop = Util.dip2px(context, 15);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        setBackgroundColor(getResources().getColor(R.color.tw_red));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();

        int parentWidth = getWidth();
        int parentHeight = getHeight();
        // SLog.info("onMeasure, childCount[%d], width[%d], height[%d]", childCount, parentWidth, parentHeight);

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            int childWidth = MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST);
            int childHeight = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.AT_MOST);
            measureChild(childView, childWidth, childHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();

        int parentWidth = getWidth();
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        // SLog.info("onLayout, childCount[%d], paddingLeft[%d], paddingRight[%d]", childCount, paddingLeft, paddingRight);

        int textViewCount = 0;
        int totalChildWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (!(childView instanceof TextView)) {
                continue;
            }

            textViewCount++;
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            totalChildWidth += childWidth;
        }

        // 各個進度文本之間的間距
        int gapWidth = (parentWidth - paddingLeft - paddingRight - totalChildWidth) / (textViewCount - 1);

        int textViewLeft = 0, textViewRight = 0, textViewTop = 0, textViewBottom = 0;
        int textViewWidth = 0;
        int left = paddingRight;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            // 布局TextView
            if (childView instanceof TextView) {
                textViewLeft = left;
                if (upToDown) {
                    textViewTop = paddingTop;
                } else {
                    textViewTop =2*paddingTop;
                }
                textViewRight = textViewLeft + childWidth;
                textViewBottom = textViewTop + childHeight;

                textViewWidth = childWidth;

                childView.layout(textViewLeft, textViewTop, textViewRight, textViewBottom);
                left += childWidth + gapWidth;
            } else if (childView instanceof ImageView) { // 布局ImageView
                int imageViewTop;
                int imageViewLeft;
                imageViewLeft = textViewLeft + (textViewWidth - childWidth) / 2;
                if (upToDown) {
                    imageViewTop = textViewBottom + iconMarginTop;
                } else {
                    imageViewTop = paddingTop;
                }
                int imageViewRight = imageViewLeft + childWidth;
                int imageViewBottom = imageViewTop + childHeight;
                childView.layout(imageViewLeft, imageViewTop, imageViewRight, imageViewBottom);
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        List<Point> pointList = new ArrayList<>();

        int top, bottom, left, right;

        int imageViewIndex = 0;
        int childCount = getChildCount();

        int imageViewCount = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            if (childView instanceof ImageView) {
                imageViewCount++;
            }
        }

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            if (childView instanceof TextView) {
                continue;
            }

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            top = childView.getTop();
            bottom = childView.getBottom();
            left = childView.getLeft();
            right = childView.getRight();

            if (imageViewIndex > 0) {
                Point point = new Point(left, (top + bottom) / 2);
                pointList.add(point);
            }

            if (imageViewIndex < imageViewCount - 1) {
                Point point = new Point(right, (top + bottom) / 2);
                pointList.add(point);
            }

            imageViewIndex++;
        }

        if (pointList.size() % 2 != 0) {
            SLog.info("Error!Data invalid");
            return;
        }

        int lineCount = pointList.size() / 2;
        int index = 0;
        for (int i = 0; i < lineCount; i++) {
            index = 2 * i;
            Point start = pointList.get(index);
            Point stop = pointList.get(index + 1);

            if (i < currentStep) {
                paint.setColor(colorFinished);
            } else {
                paint.setColor(colorUnfinished);
            }

            canvas.drawLine(start.x, start.y, stop.x, stop.y, paint);
        }
    }

    /**
     * 設置進度數據
     * @param stepList
     * @param currentStep 當前進度處于第幾步，從0開始
     */
    public void setData(List<String> stepList, int currentStep) {
        if (stepList == null || stepList.size() < 2) {  // 最少要有兩個步驟
            return;
        }

        if (currentStep < 0) {
            currentStep = 0;
        }
        if (currentStep > stepList.size() - 1) {
            currentStep = stepList.size() - 1;
        }

        this.currentStep = currentStep;
        removeAllViews();

        int count = 0;
        for (String text : stepList) {
            TextView textView = new TextView(context);
            textView.setText(text);

            // 不同的進度用不同的文字顏色
            if (count <= currentStep) {
                textView.setTextColor(colorFinished);
            } else {
                textView.setTextColor(colorUnfinished);
            }

            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            ImageView imageView = new ImageView(context);
            // 不同的進度用不同的圖標
            if (count <= currentStep) {
                imageView.setImageResource(finishResId);
            } else {
                imageView.setImageResource(unfinishResId);
            }


            addView(textView, layoutParams);
            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            addView(imageView, layoutParams);


            count++;
        }
    }

    public void setTheme() {
        setBackgroundColor(getResources().getColor(R.color.tw_blue));
        colorUnfinished = Color.parseColor("#99ffffff");
        finishResId = R.drawable.svg_seller_finish;
        unfinishResId = R.drawable.svg_seller_unfinish;
        upToDown=false;
    }
}
