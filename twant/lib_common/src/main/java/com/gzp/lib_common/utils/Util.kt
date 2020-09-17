package com.gzp.lib_common.utils

import android.content.Context
import com.gzp.lib_common.BuildConfig

object Util {
    fun inDev(): Boolean {
        return BuildConfig.DEBUG
    }

    /**
     * dp和pixel转换
     *
     * @param dipValue
     * dp值
     * @return 像素值
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val m = context.resources.displayMetrics.density
        return (dipValue * m + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}