package com.ftofs.twant.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * 可以設置自定義數據的ImageView
 * 為了應對setTag()操作時， 與Glide的衝突
 * @author zwm
 */
public class RoundedDataImageView extends RoundedImageView {
    Object customData;
    public RoundedDataImageView(Context context) {
        super(context);
    }

    public RoundedDataImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedDataImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Object getCustomData() {
        return customData;
    }

    public void setCustomData(Object customData) {
        this.customData = customData;
    }
}
