package com.gzp.lib_common.base.callback;

import androidx.annotation.Nullable;


/**
 * 通用回調接口
 * @author zwm
 */
public interface CommonCallback {
    /**
     * 成功时的回调
     * @param data
     * @return
     */
    String onSuccess(@Nullable String data);


    /**
     * 失败时的回调
     * @param data
     * @return
     */
    String onFailure(@Nullable String data);
}
