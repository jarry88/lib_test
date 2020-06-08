package com.ftofs.twant.util;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.domain.activity.Activity;

/**
 * 剪贴板开发指南  https://developer.android.com/guide/topics/text/copy-paste
 *
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/25
 *     desc  : 剪贴板相关工具类
 * </pre>
 */
public final class ClipboardUtils {

    private ClipboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 复制文本到剪贴板
     *
     * @param text 文本
     */
    public static void copyText(Context context, CharSequence text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(ClipData.newPlainText("text", text));
    }

    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本
     */
    public static CharSequence getText(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(context);
        }
        return null;
    }
    public interface Function {
        /** Invokes the function. */
        void invoke(String text);
    }

    public static void getClipBoardText(@Nullable android.app.Activity activity, final Function f) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && activity != null) {
            getTextFromClipFroAndroidQ(activity, f);
        } else {
            f.invoke(getTextFromClip(activity));
        }
    }

    /**
     * AndroidQ 获取剪贴板的内容
     */
    @TargetApi(Build.VERSION_CODES.Q)
    private static void getTextFromClipFroAndroidQ(@NonNull final android.app.Activity activity, final Function f) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ClipboardManager clipboardManager =
                        (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
                if (null == clipboardManager || !clipboardManager.hasPrimaryClip()) {
                    f.invoke("");
                    return;
                }
                ClipData clipData = clipboardManager.getPrimaryClip();
                if (null == clipData || clipData.getItemCount() < 1) {
                    f.invoke("");
                    return;
                }
                ClipData.Item item = clipData.getItemAt(0);
                if (item == null) {
                    f.invoke("");
                    return;
                }
                CharSequence clipText = item.getText();
                if (TextUtils.isEmpty(clipText)){
                    f.invoke("");
                }
                else{

                    f.invoke(clipText.toString());
                }
            }
        };
        activity.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull android.app.Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull android.app.Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull android.app.Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull android.app.Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull android.app.Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull android.app.Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull android.app.Activity activity) {
                activity.getWindow().getDecorView().removeCallbacks(runnable);
            }
        });
        activity.getWindow().getDecorView().post(runnable);
    }

    private static String getTextFromClip(android.app.Activity activity) {
        ClipboardManager clipboardManager =
                (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        if (null == clipboardManager || !clipboardManager.hasPrimaryClip()) {
            return "";
        }
        ClipData clipData = clipboardManager.getPrimaryClip();
        if (null == clipData || clipData.getItemCount() < 1) {
            return "";
        }
        ClipData.Item item = clipData.getItemAt(0);
        if (item == null) {
            return "";
        }
        CharSequence clipText = item.getText();
        if (TextUtils.isEmpty(clipText)){
            return "";
        }
        else{
            return clipText.toString();
        }

    }
    /**
     * 复制uri到剪贴板
     *
     * @param uri uri
     */
    public static void copyUri(Context context, Uri uri) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(ClipData.newUri(context.getContentResolver(), "uri", uri));
    }

    /**
     * 获取剪贴板的uri
     *
     * @return 剪贴板的uri
     */
    public static Uri getUri(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getUri();
        }
        return null;
    }

    /**
     * 复制意图到剪贴板
     *
     * @param intent 意图
     */
    public static void copyIntent(Context context, Intent intent) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(ClipData.newIntent("intent", intent));
    }

    /**
     * 获取剪贴板的意图
     *
     * @return 剪贴板的意图
     */
    public static Intent getIntent(Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getIntent();
        }
        return null;
    }




}
