package com.gzp.lib_common.utils

import android.graphics.Bitmap
import com.gzp.lib_common.BuildConfig

object Util {
    fun inDev(): Boolean {
        return BuildConfig.DEBUG
    }
}