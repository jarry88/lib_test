package com.ftofs.twant.handler;

import android.webkit.JavascriptInterface;

import com.ftofs.twant.log.SLog;

/**
 * js調用原生接口
 * 最全面总结 Android WebView与 JS 的交互方式
 * https://www.jianshu.com/p/345f4d8a5cfa
 * @author zwm
 */
public class NativeJsBridge {
    /**
     * 雙11活動
     * @param data js傳進來的數據
     */
    @JavascriptInterface
    public void activity11(String data) {
        SLog.info("activity11[%s]", data);
    }

    /**
     * facebook分享
     * @param data
     */
    @JavascriptInterface
    public void fbShare(String data) {
        SLog.info("fbShare[%s]", data);
    }
}
