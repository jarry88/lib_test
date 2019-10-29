package com.ftofs.twant.handler;

import android.webkit.JavascriptInterface;

import com.ftofs.twant.log.SLog;

/**
 * js調用原生接口
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
}
