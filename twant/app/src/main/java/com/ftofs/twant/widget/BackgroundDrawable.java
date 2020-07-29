package com.ftofs.twant.widget;

import android.graphics.drawable.GradientDrawable;

public class BackgroundDrawable {
    /**
     * 创建背景Drawable
     * @param argb 背景色
     * @param radius 圆角, pixel
     * @return
     */
    public static GradientDrawable create(int argb, float radius) {
        GradientDrawable drawable =  new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(argb);
        drawable.setCornerRadius(radius);

        return drawable;
    }

    /**
     * 创建背景Drawable(分别指定4个角的radius)
     * @param argb
     * @param radii 圆角数组，长度为4，pixel, The corners are ordered top-left, top-right, bottom-right, bottom-left.
     * @return
     */
    public static GradientDrawable create(int argb, float[] radii) {
        GradientDrawable drawable =  new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(argb);
        if (radii != null && radii.length >= 4) {
            // 将每个角拆分为, [X_radius, Y_radius]
            float[] radii8 = new float[8];
            for (int i = 0; i < 4; i++) {
                radii8[i] = radii[i];
                radii8[i + 1] = radii[i];
            }
            drawable.setCornerRadii(radii8);
        }

        return drawable;
    }
}
