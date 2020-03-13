package com.ftofs.twant.entity;

/**
 * 軟鍵盤的高度信息
 * @author zwm
 */
public class SoftInputInfo {
    public static final int INVALID_HEIGHT = -1;

    /**
     * height1、height分別為軟鍵盤顯示和隱藏狀態的View的高度
     */
    int height1 = INVALID_HEIGHT;
    int height2 = INVALID_HEIGHT;
    int softInputHeight = INVALID_HEIGHT; // 軟鍵盤的高度

    public int getSoftInputHeight() {
        return softInputHeight;
    }

    public void setHeight(int height) {
        if (height1 == height) {
            return;
        }
        if (height2 == height) {
            return;
        }

        if (height1 == INVALID_HEIGHT) {
            height1 = height;
            return;
        }

        if (height2 == INVALID_HEIGHT) {
            height2 = height;
            softInputHeight = Math.abs(height1 - height2);
            return;
        }
    }
}
