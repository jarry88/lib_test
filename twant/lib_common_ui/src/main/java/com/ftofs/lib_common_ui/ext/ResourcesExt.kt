package com.ftofs.lib_common_ui.ext

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlin.math.log

/**
 *  Created by chen on 2019/6/10
 **/
fun Context.color(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}


fun View.color(@ColorRes color: Int): Int {
    return context.color(color)
}

fun Context.drawable(@DrawableRes drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableId)
}


fun View.drawable(@DrawableRes drawableId: Int): Drawable? {
    return context.drawable(drawableId)
}

fun Context.dimen(@DimenRes dimen: Int): Int {
    return resources.getDimensionPixelSize(dimen)
}


fun View.dimen(@DimenRes dimen: Int): Int {
    return context.dimen(dimen)
}

fun dp2px(dpValue: Float?): Float {
    return dpValue?.run { 0.5f + dpValue * Resources.getSystem().displayMetrics.density } ?: 0f
}
fun px2dp(pxValue: Float?): Float {
    return pxValue?.run { (this-0.5f)/Resources.getSystem().displayMetrics.density } ?: 0f
}

fun dp2px(dpValue: Int?): Int {
    return dp2px(dpValue?.toFloat()).toInt()
}
fun px2dp(pxValue: Int?): Int {
    return px2dp(pxValue?.toFloat()).toInt()
}

fun sp2px(dpValue: Float?): Float {
    return dpValue?.run { 0.5f + dpValue * Resources.getSystem().displayMetrics.scaledDensity }
            ?: 0f
}