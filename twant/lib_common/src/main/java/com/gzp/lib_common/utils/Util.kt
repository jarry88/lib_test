package com.gzp.lib_common.utils

import android.content.Context
import android.os.Build
import com.gzp.lib_common.BuildConfig
import com.gzp.lib_common.constant.Constant
import com.gzp.lib_common.constant.SPField
import com.orhanobut.hawk.Hawk
import java.util.*

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

    fun random(i: Int): Boolean {
        val a =(Math.random()*10).toInt()
        SLog.info((a>i).toString()+a.toInt())
        return a>i
    }

    /**
     * 獲取UUID
     * 參考：
     * Android Q 适配指南 让你少走一堆弯路  #设备唯一标识符
     * https://juejin.im/post/5cad5b7ce51d456e5a0728b0#heading-8
     * @return
     */
    fun getUUID(): String? {
        // 優先從SharedPreferences中取
        var uuid = Hawk.get<String>(SPField.FIELD_DEVICE_UUID)
        SLog.info("device_uuid[%s]", uuid)
        if (!uuid.isNullOrEmpty()) {
            return uuid
        }

        // 如果SharedPreferences中沒有，才生成
        var serial: String? = null
        val m_szDevIDShort = "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 + Build.HOST.length % 10 + Build.ID.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10 + Build.TAGS.length % 10 + Build.TYPE.length % 10 + Build.USER.length % 10 //13 位
        try {
            serial = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                android.os.Build.getSerial()
            } else {
                Build.getSerial()
            }
            //API>=9 使用serial号
            return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (exception: Exception) {
            //serial需要一个初始化
            serial = "serial" // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        uuid = UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        SLog.info("device_uuid[%s]", uuid)
        Hawk.put(SPField.FIELD_DEVICE_UUID, uuid)
        return uuid
    }
}