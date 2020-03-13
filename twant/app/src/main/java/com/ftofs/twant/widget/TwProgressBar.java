package com.ftofs.twant.widget;

import android.content.Context;

import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ftofs.twant.R;

/**
 * 想要圈想要帖截止日期指示
 * @author zwm
 */
public class TwProgressBar extends LinearLayout {
    public static final int COLOR_GREEN = 1;
    public static final int COLOR_ORANGE = 2;
    public static final int COLOR_RED = 3;

    LinearLayout llBackground;
    View vwForeground;
    int backgroundWidth;

    public TwProgressBar(Context context) {
        this(context, null);
    }

    public TwProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(context).inflate(R.layout.tw_progress_bar, this, true);
        llBackground = view.findViewById(R.id.ll_background);
        vwForeground = view.findViewById(R.id.vw_foreground);

        // SLog.info("constructor");
        llBackground.post(new Runnable() {
            @Override
            public void run() {
                backgroundWidth = llBackground.getWidth();
                // SLog.info("backgroundWidth[%d]", backgroundWidth);
            }
        });
    }

    public void setColor(int color) {
        if (backgroundWidth > 0) {
            setColorInternal(color);
        } else {
            llBackground.post(new Runnable() {
                @Override
                public void run() {
                    setColorInternal(color);
                }
            });
        }
    }

    private void setColorInternal(int color) {
        // 寬度固定寫死進度
        int foregroundWidth = 0;
        if (color == COLOR_GREEN) {
            foregroundWidth = (int) (0.9 * backgroundWidth);
            vwForeground.setBackgroundResource(R.drawable.tw_progress_bar_foreground_bg_green);
        } else if (color == COLOR_ORANGE) {
            foregroundWidth = (int) (0.33 * backgroundWidth);
            vwForeground.setBackgroundResource(R.drawable.tw_progress_bar_foreground_bg_orange);
        } else if (color == COLOR_RED) {
            foregroundWidth = (int) (0.13 * backgroundWidth);
            vwForeground.setBackgroundResource(R.drawable.tw_progress_bar_foreground_bg_red);
        }

        // SLog.info("backgroundWidth[%d]", backgroundWidth);

        ViewGroup.LayoutParams layoutParams = vwForeground.getLayoutParams();
        layoutParams.width = foregroundWidth;
        vwForeground.setLayoutParams(layoutParams);
    }
}
