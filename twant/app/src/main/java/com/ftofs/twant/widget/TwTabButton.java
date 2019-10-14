package com.ftofs.twant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;

/**
 * 高亮狀態下，帶有下劃線的TabButton
 * @author zwm
 */
public class TwTabButton extends android.support.v7.widget.AppCompatTextView {
    int status = Constant.STATUS_UNSELECTED;

    Paint paint;

    int twBlue;
    int twBlack;
    int strokeWidthPx;
    boolean useCap;
    TtbOnClickListener onClickListener;
    TtbOnSelectListener onSelectListener;

    public TwTabButton(Context context) {
        this(context, null);
    }

    public TwTabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwTabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TwTabButton, defStyleAttr, 0);
        strokeWidthPx = (int) array.getDimension(R.styleable.TwTabButton_ttb_stroke_width, 0);
        useCap = array.getBoolean(R.styleable.TwTabButton_ttb_use_cap, false);
        array.recycle();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        twBlack = getResources().getColor(R.color.tw_black, null);
        twBlue = getResources().getColor(R.color.tw_blue, null);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == Constant.STATUS_SELECTED) {
                    setStatus(Constant.STATUS_UNSELECTED);
                } else {
                    setStatus(Constant.STATUS_SELECTED);
                }

                if (onClickListener != null) {
                    onClickListener.onClick(TwTabButton.this);
                }

                // Tab選中事件的處理
                if (status == Constant.STATUS_SELECTED && onSelectListener != null) {
                    onSelectListener.onSelect(TwTabButton.this);
                }
            }
        });
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;

        if (status == Constant.STATUS_SELECTED) {
            setTextColor(twBlue);
        } else {
            setTextColor(twBlack);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (status == Constant.STATUS_SELECTED && strokeWidthPx > 0) {
            int width = getWidth();
            int height = getHeight();
            // SLog.info("width[%d], height[%d]", width, height);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidthPx);
            if (useCap) {
                paint.setStrokeCap(Paint.Cap.ROUND);
            }

            paint.setColor(twBlue);

            // 水平方向：兩邊往中間縮進strokeWidth，以容納Cap的寬度
            float startX = 0;
            float stopX = width;
            if (useCap) {
                startX = strokeWidthPx;
                stopX = width - strokeWidthPx;
            }
            canvas.drawLine(startX, height - strokeWidthPx, stopX, height - strokeWidthPx, paint);
        }
    }

    public void setTtbOnClickListener(TtbOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setTtbOnSelectListener(TtbOnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public interface TtbOnClickListener {
        public void onClick(TwTabButton tabButton);
    }

    public interface TtbOnSelectListener {
        public void onSelect(TwTabButton tabButton);
    }
}
