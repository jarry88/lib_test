package com.ftofs.lib_common_ui

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import com.gzp.lib_common.constant.SPField
import com.gzp.lib_common.utils.SLog
import com.jaeger.library.StatusBarUtil
import com.orhanobut.hawk.Hawk
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

fun switchTranslucentMode(activity: Activity,isImageMode: Boolean){
    val contentView = activity.findViewById<View>(android.R.id.content);
    if (isImageMode) {
        contentView.setPadding(0, 0, 0, 0)
        StatusBarUtil.setTranslucentForImageViewInFragment(activity, null)
        StatusBarUtil.setTranslucentForImageView(activity, 0, null)
    } else {
        StatusBarUtil.setColor(activity, Color.WHITE, 0);  // 设置状态栏为白色
        StatusBarUtil.setLightMode(activity);

        val statusBarHeight = getStatusbarHeight(activity);
        /*
        修复问题
        https://github.com/laobie/StatusBarUtil/issues/291
        setDarkMode 和 setLightMode 会使 布局向上偏移，设置fitsSystemWindows会使Edittextview长按上下文菜单边距失效

        参考：
        StatusBarUtils实现沉浸式状态栏适配（第一种实现方式）   https://www.jianshu.com/p/fbbaa493633f
        StatusBarUtils沉浸式状态栏适配（第二种实现方式）  https://www.jianshu.com/p/a8bf1aeb04f5
        fitsSystemWindows之大坑  https://www.jianshu.com/p/9c6cde59575e
        该使用 fitsSystemWindows 了！ http://blog.chengyunfeng.com/?p=905#ixzz6X4W3W7xU
         */
        contentView.setPadding(0, statusBarHeight, 0, 0);
    }
}


fun getStatusbarHeight(context: Context?): Int {
    var height = Hawk.get(SPField.FIELD_STATUS_BAR_HEIGHT, 0)
    if (height > 0) {
        SLog.info("status_bar_height[%d]", height)
        return height
    }

    // 如果在Hawk中沒有保存，則查詢statusBar的高度
    height = QMUIStatusBarHelper.getStatusbarHeight(context)
    if (height > 0) {
        Hawk.put(SPField.FIELD_STATUS_BAR_HEIGHT, height)
    }
    return 0
}
