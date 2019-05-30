package com.ftofs.twant.entity;

public class StoreVoucher {
    public StoreVoucher(int storeId, String storeName, int templatePrice, String limitAmountText,
                        String usableClientTypeText, String useStartTime, String useEndTime,
                        int memberIsReceive) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.templatePrice = templatePrice;
        this.limitAmountText = limitAmountText;
        this.usableClientTypeText = usableClientTypeText;
        this.useStartTime = useStartTime;
        this.useEndTime = useEndTime;
        this.memberIsReceive = memberIsReceive;
    }

    public int storeId;
    public String storeName;
    public int templatePrice;
    public String limitAmountText;
    public String usableClientTypeText;
    public String useStartTime;
    public String useEndTime;

    /**
     * 優惠券是否已經領取
     */
    public int memberIsReceive;
}
