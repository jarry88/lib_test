package com.gzp.lib_common.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.gzp.lib_common.BuildConfig
import com.gzp.lib_common.constant.SPField
import com.orhanobut.hawk.Hawk
import java.io.File
import java.util.*

object Util {
    fun inDev(): Boolean {
        return BuildConfig.DEBUG
    }
    const val DATA_IMAGE_PNG_PREFIX = "data:image/png;base64,"

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
        SLog.info((a > i).toString() + a.toInt())
        return a>i
    }

    /**
     * 根據context查找Activity
     * @param context
     * @return
     */
    fun findActivity(context: Context): Activity? {
        return if (context is Activity) {
            context
        } else if (context is ContextWrapper) {
            findActivity(context.baseContext)
        } else {
            null
        }
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
    fun makeParentDirectory(absolutePath: String?): Boolean {
        return makeParentDirectory(File(absolutePath))
    }

    /**
     * 創建file的父目錄（如果不存在的話)
     * @param file
     * @return 如果創建成功或已經存在，返回 true; 創建失敗返回 false
     */
    fun makeParentDirectory(file: File): Boolean {
        val parentFile = file.parentFile
        return if (parentFile.exists()) {
            true
        } else parentFile.mkdirs()
    }

    /**
     * 將圖片文件添加到系統相冊
     * 參考:
     * android 保存图片到相册并正常显示
     * https://blog.csdn.net/a394268045/article/details/51645411
     * @param context
     * @param file
     */
    fun addImageToGallery(context: Context, file: File) {
        // 其次把文件插入到系统图库
        val path = file.absolutePath
        try {
            MediaStore.Images.Media.insertImage(context.contentResolver, path, file.name, null)
        } catch (e: java.lang.Exception) {
            SLog.info("Error!message[%s], trace[%s]", e.message, Log.getStackTraceString(e))
        }
        // 最后通知图库更新
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val uri = Uri.fromFile(file)
        intent.data = uri
        context.sendBroadcast(intent)
        SLog.info("HERE")
    }

}

