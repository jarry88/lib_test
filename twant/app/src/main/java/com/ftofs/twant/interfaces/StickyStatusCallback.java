package com.ftofs.twant.interfaces;

public interface StickyStatusCallback {
    /**
     * Sticky状态改变时的回调
     * @param isAdd 是否是添加View
     * @param object 如果isAdd为true，object表示要添加的View
     */
    void changeStickyStatus(boolean isAdd, Object object);
}
