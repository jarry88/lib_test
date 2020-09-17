package com.gzp.lib_common.constant

import android.os.Build
import android.util.Log
import com.gzp.lib_common.utils.SLog
import java.io.BufferedReader
import java.io.InputStreamReader

object Vendor {
    /**
     * 手機廠商ROM類型
     */
    const val VENDOR_OTHER = 0
    const val VENDOR_SONY = 1
    const val VENDOR_HUAWEI = 2
    const val VENDOR_XIAOMI = 3
    const val VENDOR_SAMSUNG = 4


    const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    const val KEY_VERSION_EMUI = "ro.build.version.emui"
    /**
     * 获取厂商信息
     *
     * @return 获取厂商信息
     */
    fun getManufacturer(): String {
        return if (Build.MANUFACTURER == null) "" else Build.MANUFACTURER.trim { it <= ' ' }
    }

    fun getProp(name: String): String? {
        var line: String? = null
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: Exception) {
            SLog.info("Error!message[%s], trace[%s]", ex.message, Log.getStackTraceString(ex))
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: Exception) {
                    SLog.info("Error!message[%s], trace[%s]", e.message, Log.getStackTraceString(e))
                }
            }
        }
        return line
    }
    fun getVendorType(): Int {
        val manufacturer = getManufacturer().toLowerCase()
        if (manufacturer.contains("sony")) {
            SLog.info("索尼手機")
            return VENDOR_SONY
        }
        if (!getProp(KEY_VERSION_EMUI).isNullOrEmpty()) {
            SLog.info("華為手機")
            return VENDOR_HUAWEI
        }
        if (!getProp(KEY_VERSION_MIUI).isNullOrEmpty()) {
            SLog.info("小米手機")
            return VENDOR_XIAOMI
        }
        if (manufacturer.contains("samsung")) {
            SLog.info("三星手機")
            return VENDOR_SAMSUNG
        }
        return VENDOR_OTHER
    }
}