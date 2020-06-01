package com.ftofs.twant.seller.entity;

import androidx.annotation.NonNull;

/**
 * 【賣家】訂單列表過濾參數
 * @author zwm
 */
public class SellerOrderFilterParams {
    public String buyerNickname;
    public String buyerMobile;
    public String orderSN;
    public String goodsName;
    public String receiverMobile;
    public String receiverAddress;

    public TwDate beginDate;
    public TwDate endDate;
    public String orderType;
    public String orderSource;
    public String refundSN;
    public String searchType;//搜索類型搜索類型 all所有 waiting待處理 agree同意 disagree不同意

    @NonNull
    @Override
    public String toString() {
        return String.format("buyerNickname[%s], buyerMobile[%s], orderSN[%s], goodsName[%s], receiverMobile[%s], receiverAddress[%s], beginDate[%s], endDate[%s], orderType[%s], orderSource[%s]",
                buyerNickname, buyerMobile, orderSN, goodsName, receiverMobile, receiverAddress, beginDate, endDate, orderType, orderSource);
    }
}
