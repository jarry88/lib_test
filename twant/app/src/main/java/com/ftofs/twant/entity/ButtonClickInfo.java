package com.ftofs.twant.entity;

/**
 * 這個類主要是記錄按鈕的點擊信息，用于防止有某些用戶快速點擊，造成重復提交的問題
 * @author zwm
 */
public class ButtonClickInfo {
    public static final long CAN_CLICK_TIMEOUT = 5000;  // 超時時間，上次點擊與當前時間超過多少毫秒后，就可以繼續點擊

    public long lastClickTime; // 上次點擊的時間戳
    public boolean canClick;   // 能否點擊

    public ButtonClickInfo() {
        // 默認能夠點擊
        this.canClick = true;
    }

    /**
     * 獲取本按鈕能否點擊
     * @return
     */
    public boolean getCanClick() {
        if (canClick) {
            return true;
        }

        // 如果超過超時時間，也可以點擊
        if (System.currentTimeMillis() - lastClickTime >= CAN_CLICK_TIMEOUT) {
            this.canClick = true;
            return true;
        }

        return false;
    }
}
