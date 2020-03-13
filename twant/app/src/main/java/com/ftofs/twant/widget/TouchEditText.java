package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;

/**
 * 捕獲觸摸選中事件的EditText
 * @author zwm
 */
public class TouchEditText extends androidx.appcompat.widget.AppCompatEditText {
    SimpleCallback simpleCallback;
    Object object;

    public TouchEditText(Context context) {
        this(context, null);
    }

    public TouchEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /*
        需要設置這兩項才可以點擊彈出軟鍵盤
        參考 https://stackoverflow.com/questions/51793711/cant-type-in-an-edittext-android-support-v7-widget-appcompatedittext
         */
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void setSimpleCallback(SimpleCallback callback) {
        this.simpleCallback = callback;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action =  event.getAction();
        SLog.info("TouchEditText::onTouchEvent, action[%d]", action);

        if (action == MotionEvent.ACTION_DOWN) {
            if (simpleCallback != null) {
                simpleCallback.onSimpleCall(getId());
            }
        }

        return super.onTouchEvent(event);
    }
}
