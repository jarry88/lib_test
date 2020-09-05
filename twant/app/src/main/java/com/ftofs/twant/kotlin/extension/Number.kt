package com.ftofs.twant.kotlin.extension

import android.content.Context
import java.text.DecimalFormat
import kotlin.random.Random

fun Float.dp2IntPx(context: Context): Int {
    return (this.dp2FloatPx(context) + 0.5f).toInt()
}

fun Float.dp2FloatPx(context: Context): Float {
    val scale = context.resources.displayMetrics.density
    return scale * this
}

fun Int.dp2IntPx(context: Context): Int {
    return this.toFloat().dp2IntPx(context)
}
inline operator fun Int.rem(blk: () -> Unit) {
    if (Random (System.currentTimeMillis()).nextInt(100) < this) blk()
}