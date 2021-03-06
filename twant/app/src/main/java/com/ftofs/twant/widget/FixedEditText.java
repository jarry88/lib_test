package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ftofs.twant.R;

/**
 * 左边有固定文字EditText
 */
public class FixedEditText extends androidx.appcompat.widget.AppCompatEditText {
    private String fixedText;
    private View.OnClickListener mListener;
    private int leftPadding;
    private int fixedTextSp;

    public FixedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFixedText(String text) {
        fixedText = text;
        fixedTextSp = 0;
        leftPadding = getPaddingLeft();
        int left = (int) getPaint().measureText(fixedText) + leftPadding;
        setPadding(left, getPaddingTop(), getPaddingBottom(), getPaddingRight());
        invalidate();
    }
    public String  getFixedText(){
        return fixedText;
    }
    public void setFixedText(String text,int sp) {
        fixedTextSp = sp;
        setFixedText(text);
    }

    public void setDrawableClick(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(fixedText)) {
            Paint paint = getPaint();
            paint.setColor(getResources().getColor(R.color.tw_black));
            if (fixedTextSp > 0) {
                paint.setTextSize(fixedTextSp);
            }
            canvas.drawText(fixedText, leftPadding, getBaseline(), paint);
//            通过下面的代码，可以查看出文字的基线，以及view的中线
//            Paint p = new Paint();
//            p.setStrokeWidth(1);
//            p.setColor(Color.parseColor("#ff0000"));
//            canvas.drawLine(0, getBaseline(), getMeasuredWidth(), getBaseline(), p);
//            canvas.drawLine(0, getMeasuredHeight() / 2, getMeasuredWidth(), getMeasuredHeight() / 2, p);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mListener != null && getCompoundDrawables()[2] != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int i = getMeasuredWidth() - getCompoundDrawables()[2].getIntrinsicWidth();
                    if (event.getX() > i) {
                        mListener.onClick(this);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

}