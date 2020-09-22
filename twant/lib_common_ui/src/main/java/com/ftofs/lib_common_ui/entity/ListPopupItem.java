package com.ftofs.lib_common_ui.entity;

import androidx.annotation.NonNull;

/**
 * 彈出框的數據項
 * @author zwm
 */
public class ListPopupItem {
    public ListPopupItem(int id, String title, Object data) {
        this.id = id;
        this.selectedIconResId = 0;
        this.unselectedIconResId = 0;
        this.title = title;
        this.data = data;
    }

    public ListPopupItem(int id, int selectedIconResId, int unselectedIconResId, String title, Object data) {
        this.id = id;
        this.selectedIconResId = selectedIconResId;
        this.unselectedIconResId = unselectedIconResId;
        this.title = title;
        this.data = data;
    }

    public int id;
    /**
     * 選中的圖標，0表示沒有圖標
     */
    public int selectedIconResId;
    /**
     * 未選中的圖標，0表示沒有圖標
     */
    public int unselectedIconResId;
    public String title;
    public Object data;

    @NonNull
    @Override
    public String toString() {
        return title;
    }
}
