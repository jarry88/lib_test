package com.ftofs.twant.constant;

/**
 * 自定義的動作
 * @author zwm
 */
public enum CustomAction {
    /**
     * 重新加載數據
     */
    CUSTOM_ACTION_RELOAD_DATA,

    /**
     * 刪除所有歷史記錄
     */
    CUSTOM_ACTION_CLEAR_ALL_HISTORY,


    /**
     * 【商家】刪除商品
     */
    CUSTOM_ACTION_SELLER_DELETE_GOODS,


    /**
     * 【商家】切換商品的上架、下架狀態
     */
    CUSTOM_ACTION_SELLER_SWITCH_GOODS_SHELF_STATUS,


    /**
     * 【商家】編輯SKU信息
     */
    CUSTOM_ACTION_SELLER_EDIT_SKU_INFO,


    /**
     * 【商家】添加規格
     */
    CUSTOM_ACTION_SELLER_ADD_SPEC,


    /**
     * 【商家】編輯規格
     */

    CUSTOM_ACTION_SELLER_EDIT_SPEC,

    /**
     * 選擇拼團
     */
    CUSTOM_ACTION_SELECT_JOIN_GROUP,
}



