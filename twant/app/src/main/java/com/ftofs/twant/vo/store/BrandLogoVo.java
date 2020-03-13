package com.ftofs.twant.vo.store;

/**
 * @Description: logo視圖對象
 * @Auther: yangjian
 * @Date: 2018/9/27 11:35
 */
public class BrandLogoVo {
    /**
     * 商店id
     */
    private int storeId;
    /**
     * 商家id
     */
    private int sellerId;
    /**
     * logo地址
     */
    private String logoUrl;

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public String toString() {
        return "BrandLogoVo{" +
                "storeId=" + storeId +
                ", sellerId=" + sellerId +
                ", logoUrl='" + logoUrl + '\'' +
                '}';
    }
}
