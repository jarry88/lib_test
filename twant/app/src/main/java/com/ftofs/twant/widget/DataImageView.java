package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 可以設置自定義數據的ImageView
 * 為了應對setTag()操作時， 與Glide的衝突
 * @author zwm
 */
public class DataImageView extends androidx.appcompat.widget.AppCompatImageView {
    Object customData;
    public DataImageView(Context context) {
        super(context);
    }

    public DataImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DataImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Object getCustomData() {
        return customData;
    }

    public void setCustomData(Object customData) {
        this.customData = customData;
    }
}
