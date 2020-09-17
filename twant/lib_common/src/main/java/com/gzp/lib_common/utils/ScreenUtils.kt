package com.gzp.lib_common.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import java.lang.reflect.Method
import kotlin.math.max


/**
 * 获取屏幕信息的Util
 */
object ScreenUtils {

    //获取屏幕宽度
    val SCREEN_WIDTH by lazy { Resources.getSystem().displayMetrics.widthPixels }

    //获取屏幕宽度
    val SCREEN_HEIGHT by lazy { Resources.getSystem().displayMetrics.heightPixels }

    //获取屏幕像素密度
    fun getScreenDensity(): Float {
        return Resources.getSystem().displayMetrics.density
    }

    //获取屏幕文字像素密度
    fun getScreenScaleDensity(): Float {
        return Resources.getSystem().displayMetrics.scaledDensity
    }

    //获取屏幕真实高度  （包括虚拟按键）
    fun getRealHeight(): Int {

        return DisplayMetrics().run {
            (BaseContext.instance.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                    .defaultDisplay
                    .getRealMetrics(this)
            heightPixels
        }
    }

    //获取屏幕真实宽度
    fun getRealWidth(): Int {
        return DisplayMetrics().run {
            (BaseContext.instance.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                    .defaultDisplay
                    .getRealMetrics(this)
            widthPixels
        }
    }

    fun getBottomNavigateHeight(): Int {
        (BaseContext.instance.getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).run {
            val metrics = DisplayMetrics()
            defaultDisplay.getMetrics(metrics)
            val usableHeight = metrics.heightPixels
            defaultDisplay.getRealMetrics(metrics)
            val realHeight = metrics.heightPixels
            return realHeight - usableHeight
        }
    }

    fun isKeyBoardShow(activity: Activity): Boolean {

        return Rect().run {
            activity.window.decorView.getWindowVisibleDisplayFrame(this)
            SCREEN_HEIGHT - bottom > 500
        }
    }

    fun isImmerseStatus(activity: Activity): Boolean {
        return (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS and activity.window.attributes.flags == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    /**
     * 判断是否有刘海
     */
    fun hasNotch(context: Context): Boolean {
        return Build.MANUFACTURER.run {
            when {
                equals("xiaomi", true) -> xiaoMiHasNotch(context)
                equals("HUAWEI", true) -> huaWeiHasNotch(context)
                equals("OPPO", true) -> oppoHasNotch(context)
                else -> false
            }
        }
    }

    /**
     * 刘海高度
     */
    fun getNotchHeight(context: Context): Int {

        if (!hasNotch(context)) return 0

        return Build.MANUFACTURER.run {
            when {
                equals("xiaomi", true) -> getXiaoMiNotchHeight(context)
                equals("HUAWEI", true) -> getHuaWeiNotchHeight(context)
                equals("OPPO", true) -> getOppoNotchHeight()
                else -> 0
            }
        }
    }

    //小米刘海
    private fun xiaoMiHasNotch(context: Context): Boolean {
        try {
            val classLoader = context.classLoader
            val systemProperties = classLoader.loadClass("android.os.SystemProperties")
            //参数类型
            val paramTypes = arrayOfNulls<Class<*>>(2)
            paramTypes[0] = String::class.java
            paramTypes[1] = Int::class.javaPrimitiveType
            val getInt = systemProperties.getMethod("getInt", *paramTypes)
            //参数
            val params = arrayOfNulls<Any>(2)
            params[0] = "ro.miui.notch"
            params[1] = 0
            return getInt.invoke(systemProperties, *params) as Int == 1
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun getXiaoMiNotchHeight(context: Context): Int {
        return context.resources.run {
            getIdentifier("notch_height", "dimen", "android").run {
                if (this > 0) getDimensionPixelSize(this) else 0
            }
        }
    }

    //oppo刘海
    private fun oppoHasNotch(context: Context): Boolean {
        return context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    private fun getOppoNotchHeight(): Int {

        var property = ""

        try {
            val cls: Class<*>? = Class.forName("android.os.SystemProperties")
            val hideMethod = cls!!.getMethod("get", String::class.java)
            val classEntity = cls.newInstance()
            property = hideMethod.invoke(classEntity, "ro.oppo.screen.heteromorphism") as String
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return property.run {
            if (isNotEmpty()) {
                split(",").run {
                    if (size == 4) this[3].toInt() - this[1].toInt() else 0
                }
            } else {
                0
            }
        }
    }


    /**
     * 华为判断是否是刘海平
     */
    private fun huaWeiHasNotch(context: Context): Boolean {
        try {
            val cl: ClassLoader = context.classLoader
            val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get: Method = hwNotchSizeUtil.getMethod("hasNotchInScreen")
            return get.invoke(hwNotchSizeUtil) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 获取华为手机刘海屏幕高度
     */
    private fun getHuaWeiNotchHeight(context: Context): Int {
        var ret = intArrayOf(0, 0)
        try {
            val cl = context.classLoader
            val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = hwNotchSizeUtil.getMethod("getNotchSize")
            ret = get.invoke(hwNotchSizeUtil) as IntArray
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ret[1]
    }
}
