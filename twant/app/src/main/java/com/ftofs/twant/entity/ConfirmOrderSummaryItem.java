package com.ftofs.twant.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.constant.Constant;
import com.gzp.lib_common.utils.SLog;

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

    @Override
    public int getItemType() {
        return Constant.ITEM_VIEW_TYPE_SUMMARY;
    }

    /**
     * 計算最終的總價
     * @return
     */
    public double calcTotalPrice() {
        // 最終付款額 = 總金額 + 總運費 - 商店折扣 - 平臺折扣 + 總稅費
        SLog.info("totalAmount[%s], storeDiscount[%s], totalTaxAmount[%s]", totalAmount, storeDiscount, totalTaxAmount);
        double result = totalAmount - storeDiscount - platformDiscount + totalTaxAmount;

        SLog.info("paymentTypeCode[%s]", paymentTypeCode);
        if (!Constant.PAYMENT_TYPE_CODE_CHAIN.equals(paymentTypeCode)) { // 不是門店自提才加上運費
            SLog.info("here");
            result += totalFreight;
        }
        SLog.info("result[%s]", result);
        return result;
    }
}
