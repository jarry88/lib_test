package com.ftofs.twant.background;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.shapes.Shape;

public class WeirdShape extends Shape {
    private static final int    COLOUR       = Color.RED;
    private static final float  STROKE_WIDTH = 1.0f;
    private static final float  CORNER = 20.0f;
    private static final float  LACKCORNER = 72.0f;
    private static final float  LACK_WIDTH = 25f;
    private static final float  LACK_THINKNESS = 6.0f;

    private final Paint border = new Paint();
    private final Paint raindStart = new Paint();
    private final Paint raindEnd = new Paint();
    private final Path  path;

    public WeirdShape() {
        path   = new Path();

        border.setColor      (COLOUR);
        border.setStyle      (Paint.Style.STROKE);
        border.setStrokeWidth(STROKE_WIDTH);
        border.setAntiAlias  (true);
        border.setDither     (true);
        border.setStrokeJoin (Paint.Join.ROUND);
        border.setStrokeCap  (Paint.Cap.ROUND);
    }

    @Override
    protected void onResize(float width, float height) {
        super.onResize(width, height);

        float dx = STROKE_WIDTH/2.0f;
        float dy = STROKE_WIDTH/2.0f;
        float x  = dx;
        float y  = dy;
        float w  = width  - dx;
        float h  = height - dy;


        RectF arcStart = new RectF(x-LACKCORNER+LACK_WIDTH,y,x+LACK_WIDTH,y+LACKCORNER);
        RectF arcEnd = new RectF(w-LACK_WIDTH,y,w+LACKCORNER-LACK_WIDTH,y+LACKCORNER);

        path.reset();
        path.moveTo(x,y);
        path.lineTo(w ,y);
        path.arcTo(arcEnd,90,180);
        path.moveTo(w, y+LACKCORNER);
        path.lineTo(w ,h);
        path.lineTo(x ,h);
        path.lineTo(x,y+LACKCORNER);
        path.arcTo(arcStart,-90,180);
        path.close();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawPath(path,border);
    }
}