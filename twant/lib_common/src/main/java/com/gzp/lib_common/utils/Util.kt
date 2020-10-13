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
import com.umeng.analytics.MobclickAgent
import java.io.File
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

fun pushUmengEvent(actionName:String ,analyticsDataMap:HashMap<String,Any?>?=null)=analyticsDataMap?.let {MobclickAgent.onEventObject(BaseContext.instance.getContext(),actionName,it)  }?:MobclickAgent.onEvent(BaseContext.instance.getContext(),actionName)
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

