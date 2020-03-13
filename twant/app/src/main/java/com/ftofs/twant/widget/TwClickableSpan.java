package com.ftofs.twant.widget;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;

public class TwClickableSpan extends ClickableSpan {
    SimpleCallback simpleCallback;
    Context context;
    private String data;

    public TwClickableSpan(Context context, String data, SimpleCallback simpleCallback) {
        this.context = context;
        this.data = data;
        this.simpleCallback = simpleCallback;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(true);
        ds.setColor(context.getColor(R.color.tw_blue));
    }

    @Override
    public void onClick(View widget) {
        SLog.info("onClick, data[%s]", data);
        if (simpleCallback != null) {
            simpleCallback.onSimpleCall(data);
        }
    }
}
