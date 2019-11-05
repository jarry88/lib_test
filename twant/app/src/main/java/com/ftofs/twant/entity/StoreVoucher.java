package com.ftofs.twant.entity;

public class StoreVoucher {
    public StoreVoucher(int storeId, int templateId, String storeName, int templatePrice, String limitAmountText,
                        String usableClientTypeText, String useStartTime, String useEndTime,
                        boolean usable) {
        this.storeId = storeId;
        this.templateId = templateId;
        this.storeName = storeName;
        this.templatePrice = templatePrice;
        this.limitAmountText = limitAmountText;
        this.usableClientTypeText = usableClientTypeText;
        this.useStartTime = useStartTime;
        this.useEndTime = useEndTime;
        this.usable = usable;
    }

    public int storeId;
    public int templateId;
    public String storeName;
    public int templatePrice;
    public String limitAmountText;
    public String usableClientTypeText;
    public String useStartTime;
    public String useEndTime;

    /**
     * 券是否可用
     */
    public boolean usable;
    public boolean received;  // 是否已領取，如果已領取-顯示【進店使用】
    public String searchSn;
}
