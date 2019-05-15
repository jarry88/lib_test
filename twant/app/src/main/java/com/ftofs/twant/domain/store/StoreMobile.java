package com.ftofs.twant.domain.store;

public class StoreMobile {
    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 移动端店铺横幅
     */
    private String storeBanner = "";

    /**
     * 是否开启移动端店铺装修 1-开启 0-关闭
     */
    private int decorationState = 0;

    /**
     * 是否仅显示移动端店铺装修内容 1-是 0-否
     */
    private int decorationOnly = 0;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreBanner() {
        return storeBanner;
    }

    public String getStoreBannerUrl() {
        return storeBanner;
    }

    public void setStoreBanner(String storeBanner) {
        this.storeBanner = storeBanner;
    }

    public int getDecorationState() {
        return decorationState;
    }

    public void setDecorationState(int decorationState) {
        this.decorationState = decorationState;
    }

    public int getDecorationOnly() {
        return decorationOnly;
    }

    public void setDecorationOnly(int decorationOnly) {
        this.decorationOnly = decorationOnly;
    }

    @Override
    public String toString() {
        return "StoreMobile{" +
                "storeId=" + storeId +
                ", storeBanner='" + storeBanner + '\'' +
                ", decorationState=" + decorationState +
                ", decorationOnly=" + decorationOnly +
                '}';
    }
}
