package com.ftofs.twant.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ftofs.twant.R;

/**
 * 擴大點擊范圍的按鈕 有時候，因為icon太小，不好點擊
 * @author zwm
 */
public class ScaledButton extends RelativeLayout {
    boolean isChecked;
    ImageView buttonIcon;
    public ScaledButton(Context context) {
        this(context, null);
    }

    public ScaledButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaledButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.scaled_button, this);
        buttonIcon = findViewById(R.id.button_icon);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScaledButton, defStyleAttr, 0);
        Drawable drawable = array.getDrawable(R.styleable.ScaledButton_icon_src);

        final float imgWidth = array.getDimension(R.styleable.ScaledButton_icon_width, -1);
        final float imgHeight = array.getDimension(R.styleable.ScaledButton_icon_width, -1);
        // SLog.info("imgWidth[%f], imgHeight[%f]", imgWidth, imgHeight);
        array.recycle();

        if (imgWidth > 0 && imgHeight > 0) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) imgWidth, (int) imgHeight);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            buttonIcon.setLayoutParams(layoutParams);
        }

        buttonIcon.setImageDrawable(drawable);
    }

    public void setIconResource(int resId) {
        buttonIcon.setImageResource(resId);
    }

    public void setColorFilter(int color) {
        buttonIcon.setColorFilter(color);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        if (isChecked) {
            this.setIconResource(R.drawable.icon_cart_item_checked);
        } else {
            this.setIconResource(R.drawable.icon_cart_item_unchecked);
        }
    }


}

