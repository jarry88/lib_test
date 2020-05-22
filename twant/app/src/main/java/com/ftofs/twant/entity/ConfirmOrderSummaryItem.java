package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;

/**
 * 產品總計
 * @author zwm
 */
public class ConfirmOrderSummaryItem implements MultiItemEntity {
    public ConfirmOrderSummaryItem() {
        paymentTypeCode = Constant.PAYMENT_TYPE_CODE_ONLINE;
    }


    public double totalTaxAmount;
    public String paymentTypeCode;
    public double totalAmount;
    public double totalFreight;
    public double storeDiscount;
    public double platformDiscount;
    public Receipt receipt; // 單據信息
    public int shipTimeType; // 配送時間
    public int totalItemCount;  // 總件數
    public int platformCouponCount;  // 平台券數量
    public String platformCouponStatus;  // 當前平台券的狀態描述，比如【可用XX張】、【$10元無門檻券】

    public int payWayIndex = 0;

    @Override
    public int getItemType() {
        return Constant.ITEM_VIEW_TYPE_SUMMARY;
    }

    /**
     * 計算最終的總價
     * @return
     */
    public double calcTotalPrice() {
        // 總金額 + 總運費 - 商店折扣 - 平臺折扣
        double result = totalAmount - storeDiscount - platformDiscount+totalTaxAmount;
        //2是門店自提現在
        if (payWayIndex != Constant.PAY_WAY_FETCH) { // 不是門店自提才加上運費
            result += totalFreight;
        }
        return result;
    }
}
