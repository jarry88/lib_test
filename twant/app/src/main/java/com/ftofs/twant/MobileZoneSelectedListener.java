package com.ftofs.twant;

/**
 * 確認選中區號時調用的接口
 */
public interface MobileZoneSelectedListener {
    /**
     * 確認選中區號時調用
     * @param selectedIndex 選中區號的索引（從0開始）
     */
    public void onMobileZoneSelected(int selectedIndex);
}
