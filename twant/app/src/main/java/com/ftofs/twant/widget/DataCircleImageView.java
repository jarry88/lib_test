package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class DataCircleImageView extends CircleImageView {
    Object customData;
    private boolean isCircle;

    public DataCircleImageView(Context context) {
        super(context);
    }

    public DataCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DataCircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Object getCustomData() {
        return customData;
    }

    public void setCustomData(Object customData) {
        this.customData = customData;
    }


}
