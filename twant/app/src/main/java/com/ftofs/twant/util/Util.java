package com.ftofs.twant.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * 常用工具類
 * @author zwm
 */
public class Util {
    /**
     * 进入全屏模式
     * 参考
     * Android 正确进入全屏和退出全屏的姿势
     * https://blog.csdn.net/bobcat_kay/article/details/82794317
     * @param activity
     */
    public static void enterFullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /**
     * 退出全屏模式
     * 参考
     * Android 正确进入全屏和退出全屏的姿势
     * https://blog.csdn.net/bobcat_kay/article/details/82794317
     * @param activity
     */
    public static void exitFullScreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }


    /**
     * 在parentView中根據id查找目標view，并設置OnClickListener
     * @param parentView
     * @param id
     * @param listener
     */
    public static void setOnClickListener(View parentView, int id, View.OnClickListener listener) {
        View targetView = parentView.findViewById(id);

        if (targetView != null) {
            targetView.setOnClickListener(listener);
        }
    }



    public static void setWindowStatusBarColor(Activity activity, int color) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 將byte數組轉換為字符串
     * @param byteArray
     * @return
     */
    public static String byteArrayToString(byte[] byteArray) {
        return byteArrayToString(byteArray, 0, byteArray.length);
    }

    /**
     * 將byte數組轉換為字符串
     * @param byteArray
     * @param len
     * @return
     */
    public static String byteArrayToString(byte[] byteArray, int len) {
        return byteArrayToString(byteArray, 0, len);
    }


    /**
     * 將byte數組轉換為字符串
     * @param byteArray
     * @param begin 開始位置(inclusive)
     * @param end 結束位置(exclusive)
     * @return
     */
    public static String byteArrayToString(byte[] byteArray, int begin, int end) {
        if (byteArray == null || begin >= end) {
            return null;
        }

        int len = byteArray.length;
        if (len < 1) {
            return "";
        }


        byte[] buffer = new byte[end - begin];

        for (int i = begin; i < end; ++i) {
            buffer[i - begin] = byteArray[i];
        }

        return new String(buffer);
    }
}
