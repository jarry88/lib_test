package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的處理結果結構
 * @author zwm
 */
public class CommonResult extends Throwable {
    public static final int CODE_SUCCESS = 200;   // 成功
    public static final int CODE_API_FAILED = 40001; // Api處理失敗
    public static final int CODE_USER_NOT_LOGIN = 40002; // 用戶未登錄
    public static final int CODE_OTHER_ERROR = 40003;  // 其他錯誤

    public static Map<Integer, String> messageMap = new HashMap<>();

    static {
        messageMap.put(CODE_SUCCESS, "");
        messageMap.put(CODE_API_FAILED, "Api處理失敗");
        messageMap.put(CODE_USER_NOT_LOGIN, "請先登錄App");
        messageMap.put(CODE_OTHER_ERROR, "其他錯誤");
    }

    public int code; // 錯誤碼
    public String message; // 錯誤描述
    public Object extra; // 附加數據

    public CommonResult(int code) {
        this(code, messageMap.get(code));
    }

    public CommonResult(int code, String message) {
        this(code, message, null);
    }

    public CommonResult(int code, String message, Object extra) {
        this.code = code;
        this.message = message;
        this.extra = extra;
    }

    public static CommonResult success() {
        return new CommonResult(CODE_SUCCESS, "");
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("code[%d], message[%s], extra[%s]", code, message, extra);
    }
}
