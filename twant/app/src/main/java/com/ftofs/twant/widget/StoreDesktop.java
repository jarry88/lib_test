package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * 商品展架平臺的灰色桌面
 * @author zwm
 */
public class StoreDesktop extends View {
    Paint paint;
    Path path;
    int color;

    public StoreDesktop(Context context) {
        this(context, null);
    }

    public StoreDesktop(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoreDesktop(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        color = Color.parseColor("#F3F3F3");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();


        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        path.moveTo(height, 0);
        path.lineTo(width - height, 0);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
        canvas.drawPath(path, paint);
    }
}