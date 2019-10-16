package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

/**
 * 商品總計
 * @author zwm
 */
public class ConfirmOrderSummaryItem implements MultiItemEntity {
    public ConfirmOrderSummaryItem() {
        paymentTypeCode = Constant.PAYMENT_TYPE_CODE_ONLINE;
    }

    public String paymentTypeCode;
    public float totalAmount;
    public float totalFreight;
    public float storeDiscount;
    public float platformDiscount;
    public Receipt receipt; // 票據信息
    public int shipTimeType; // 配送時間
    public int totalItemCount;  // 總件數
    public int platformCouponCount;  // 平臺券數量
    public String platformCouponStatus;  // 當前平臺券的狀態描述，比如【可用XX張】、【$10元無門檻券】

    @Override
    public int getItemType() {
        return Constant.ITEM_VIEW_TYPE_SUMMARY;
    }
}
