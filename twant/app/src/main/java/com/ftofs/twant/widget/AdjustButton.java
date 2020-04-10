package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.cart.SkuStatus;
import com.ftofs.twant.entity.cart.SpuStatus;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

/**
 * 購物袋數量調節按鈕
 * @author zwm
 */
public class AdjustButton extends androidx.appcompat.widget.AppCompatTextView {
    public static final float DEFAULT_THRESHOLD = 0.28f;

    protected SkuStatus skuStatus;
    protected SpuStatus spuStatus;

    // 有效的取值范圍為 [minValue, maxValue]，開區間
    int minValue = 0;
    int maxValue = Integer.MAX_VALUE;

    /**
     * 超出有效范圍的Callback
     */
    OutOfValueCallback outOfMinValue;
    OutOfValueCallback outOfMaxValue;

    // 當前值，value的取值范圍為 [minValue, maxValue]，開區間
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
        SLog.info("onTouchEvent, action[%d]", action);

        if (action != MotionEvent.ACTION_UP) {
            return true;
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
                if (outOfMinValue != null) {
                    outOfMinValue.outOfValue();
                }
                return true;
            }
            changeValue(-1);
        }
        if (proportion > 1 - threshold) {
            if (value >= maxValue) {
                // 不能大于最大值
                if (outOfMaxValue != null) {
                    outOfMaxValue.outOfValue();
                }
                return true;
            }
            changeValue(1);
        }

        return true;
    }

    private void updateView() {
        setText(String.valueOf(value));
    }

    public int getValue() {
        return value;
    }

    /**
     * 設置當前值
     * @param value
     * @return 是否設置成功
     */
    public boolean setValue(int value) {
        if (value < minValue) {
            return false;
        }
        if (value > maxValue) {
            return false;
        }

        this.value = value;
//        if (skuStatus != null) {
//            skuStatus.setCount(value);
//        }
        if (spuStatus != null) {
            spuStatus.setCount(value);
        }
        updateView();

        return true;
    }
    public void setZero() {
        this.value = 0;
        setClickable(false);
        updateView();
    }
    /**
     * 通過按鈕增減數值
     *
     * @param delta 負數減少，正數增加
     */

    public void changeValue(int delta) {
        setValue(value + delta);
    }

    public void setMinValue(int minValue, OutOfValueCallback outOfValueCallback) {
        this.minValue = minValue;
        this.outOfMinValue = outOfValueCallback;
    }

    public void setMaxValue(int maxValue, OutOfValueCallback outOfValueCallback) {
        this.maxValue = maxValue;
        this.outOfMaxValue = outOfValueCallback;
    }

    public void setSkuStatus(SkuStatus skuStatus) {
        this.skuStatus = skuStatus;

    }

    public void setSpuStatus(SpuStatus spuStatus) {
        this.spuStatus = spuStatus;
    }

    public interface OutOfValueCallback {
        void outOfValue();
    }
}
