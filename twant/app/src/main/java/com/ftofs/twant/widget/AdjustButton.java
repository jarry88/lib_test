package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.cart.SkuStatus;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

/**
 * 購物車數量調節按鈕
 * @author zwm
 */
public class AdjustButton extends android.support.v7.widget.AppCompatTextView {
    public static final float DEFAULT_THRESHOLD = 0.28f;

    protected SkuStatus skuStatus;

    int minValue = 0;
    int value;
    // 調節按鈕的閾值，范圍  0 ~ 0.5
    float threshold = DEFAULT_THRESHOLD;
    public AdjustButton(Context context) {
        this(context, null);
    }

    public AdjustButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdjustButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setGravity(Gravity.CENTER);
        setBackgroundResource(R.drawable.quantity_adjust_bg);
        updateView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action != MotionEvent.ACTION_UP) {
            return super.onTouchEvent(event);
        }

        float x = event.getX();
        float y = event.getY();
        int width = getWidth();
        SLog.info("onTouchEvent, dpi[%s], action[%d], width[%d], x=%s, y=%s",
                Util.dip2px(getContext(), 1),
                action, width,
                Util.px2dip(getContext(), x), Util.px2dip(getContext(), y));
        float proportion = x / width;
        if (proportion < threshold) {
            if (value <= minValue) {
                // 不能小于最小值
                return super.onTouchEvent(event);
            }
            changeValue(-1);
        }
        if (proportion > 1 - threshold) {
            changeValue(1);
        }

        return super.onTouchEvent(event);
    }

    private void updateView() {
        setText(String.valueOf(value));
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        skuStatus.setCount(value);
        updateView();
    }

    /**
     * 通過按鈕增減數值
     * @param delta 負數減少，正數增加
     */
    public void changeValue(int delta) {
        setValue(value + delta);
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setSkuStatus(SkuStatus skuStatus) {
        this.skuStatus = skuStatus;
    }
}
