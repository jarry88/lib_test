package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.utils.SLog;

public class CrossBorderDrawView extends View {
    Paint p;
    RectF oval;

    public CrossBorderDrawView(Context context) {
        this(context, null);
    }

    public CrossBorderDrawView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CrossBorderDrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }


    private void init() {
        // 创建画笔
        p = new Paint();
        p.setColor(getContext().getColor(R.color.tw_cross_border_home_page_bg_color));
        p.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();
        SLog.info("height[%d], width[%d]", height, width);

        if (oval == null) {
            oval = new RectF(-Util.dip2px(getContext(), 25),
                    0,
                    width + Util.dip2px(getContext(), 25),
                    height);// 设置个新的长方形，扫描测量
        }

        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
        canvas.drawArc(oval, 0, 180, true, p);
    }

    public void setColor(int color) {
        p.setColor(color);
        invalidate();
    }
}
