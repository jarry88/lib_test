package com.ftofs.twant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * 更完善的Tab按鈕
 * @author zwm
 */
public class SimpleTabButton extends RelativeLayout {
    TextView tvTitle;
    int status = Constant.STATUS_UNSELECTED;
    Paint paint;

    int selectedColor;
    int unselectedColor;
	
	int strokeWidthPx;
    boolean useCap;
    boolean drawUnderline;  // 是否繪製下劃線
    String text;

    int textWidth;
    int horizontalPaddingPx;
    int paddingBottomPx;
    int textSizePx;

    public SimpleTabButton(Context context) {
        this(context, null);
    }

    public SimpleTabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleTabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimpleTabButton, defStyleAttr, 0);
        strokeWidthPx = (int) array.getDimension(R.styleable.SimpleTabButton_stb_stroke_width, Util.dip2px(context, 2));
        useCap = array.getBoolean(R.styleable.SimpleTabButton_stb_use_cap, false);
        horizontalPaddingPx = (int) array.getDimension(R.styleable.SimpleTabButton_stb_horizontal_padding, 0);
        text = array.getString(R.styleable.SimpleTabButton_stb_text);
        textSizePx = (int) array.getDimension(R.styleable.SimpleTabButton_stb_text_size, 0);
        paddingBottomPx = (int) array.getDimension(R.styleable.SimpleTabButton_stb_padding_bottom, 0);
        selectedColor = array.getColor(R.styleable.SimpleTabButton_stb_selected_color, getResources().getColor(R.color.tw_blue, null));
        unselectedColor = array.getColor(R.styleable.SimpleTabButton_stb_unselected_color, getResources().getColor(R.color.tw_black, null));
        drawUnderline = array.getBoolean(R.styleable.SimpleTabButton_stb_draw_underline, true);
        array.recycle();

        // SLog.info("strokeWidthPx[%d],useCap[%s],horizontalPaddingPx[%d],text[%s], paddingBottom[%d]",
        //         strokeWidthPx, useCap, horizontalPaddingPx, text, paddingBottomPx);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        tvTitle = new TextView(context);
        tvTitle.setTextColor(unselectedColor);
        if (textSizePx > 0) {
            tvTitle.setTextSize(COMPLEX_UNIT_PX, textSizePx);
        }

        if (!StringUtil.isEmpty(text)) {
            tvTitle.setText(text);
        }

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT, TRUE);
        addView(tvTitle, layoutParams);

        // ViewGroup需要設置這個為false，invalidate()才會觸發onDraw()調用
        setWillNotDraw(false);

        tvTitle.post(new Runnable() {
            @Override
            public void run() {
                textWidth = tvTitle.getMeasuredWidth();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        textWidth = tvTitle.getMeasuredWidth();
        SLog.info("status[%d], strokeWidthPx[%d], textWidth[%d]", status, strokeWidthPx, textWidth);
        if (status == Constant.STATUS_SELECTED && strokeWidthPx > 0 && textWidth > 0 && drawUnderline) {
            int width = getWidth();
            int height = getHeight();
            SLog.info("__==height[%d]", height);

            int halfWidth = width / 2;

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidthPx);
            if (useCap) {
                paint.setStrokeCap(Paint.Cap.ROUND);
            }

            paint.setColor(selectedColor);

            int y = height - strokeWidthPx / 2 - paddingBottomPx;
            float startX = halfWidth - textWidth / 2 - horizontalPaddingPx;
            float stopX = halfWidth + textWidth / 2 + horizontalPaddingPx;

            canvas.drawLine(startX, y, stopX, y, paint);
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;

        if (status == Constant.STATUS_SELECTED) {
            tvTitle.setTextColor(selectedColor);
        } else {
            tvTitle.setTextColor(unselectedColor);
        }
        invalidate();
    }

    public void setText(String text) {
        tvTitle.setText(text);
    }

    public void setSelectedColor(int color) {
        selectedColor = color;
    }

    public void setUnselectedColor(int color) {
        unselectedColor = color;
    }
}
