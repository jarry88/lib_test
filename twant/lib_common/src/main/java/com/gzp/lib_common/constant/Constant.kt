package com.gzp.lib_common.constant

object Constant {


    /**
     * 增刪查改 -- 動作定義
     */
    const val ACTION_ADD = 1
    const val ACTION_EDIT = 2

    const val UMENG_ALIAS_TYPE = "twant"
    /*
    "promotionType": 0,  #0無促銷活動 1限時折扣 2全款預售 3定金預售 4多人拼團 5優惠套裝 6秒殺活動 7砍價活動
     */
    const val PROMOTION_TYPE_NONE = 0
    const val PROMOTION_TYPE_TIME_LIMITED_DISCOUNT = 1
    const val PROMOTION_TYPE_GROUP = 4
    const val PROMOTION_TYPE_SEC_KILL = 6
    const val PROMOTION_TYPE_BARGAIN = 7


    /**
     * 正常的Item
     */
    const val ITEM_TYPE_NORMAL = 1

    /**
     * 數據全部加載完成的提示
     */
    const val ITEM_TYPE_LOAD_END_HINT = 2

    /**
     * Banner
     */
    const val ITEM_TYPE_BANNER = 3

    /**
     * 標題Item
     */
    const val ITEM_TYPE_TITLE = 4

    /**
     * 沒有數據的Item
     */
    const val ITEM_TYPE_NO_DATA = 5

    /**
     * 數據全部加載完成的提示
     */
    const val ITEM_TYPE_NO_STORE_DATA = 6


    /**
     * INT類型的true | false 定義
     */
    const val TRUE_INT = 1
    const val FALSE_INT = 0
    const val MSG_NOTIFY_HXID = "hId"

    // public static final int SKU_SELECTED_FONT_SIZE = 14;
    // public static final int SKU_UNSELECTED_FONT_SIZE = 14;

    // public static final int SKU_SELECTED_FONT_SIZE = 14;
    // public static final int SKU_UNSELECTED_FONT_SIZE = 14;
    const val DISTRICT_ID_MACAO = 45056
}