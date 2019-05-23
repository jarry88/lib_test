package com.ftofs.twant.domain.store;

import java.io.Serializable;
import java.math.BigDecimal;


public class StoreCost implements Serializable {
    /**
     * 自增编码
     */
    private int costId;

    /**
     * 店铺ID
     */
    private int storeId = 0;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 卖家编号
     */
    private int sellerId = 0;

    /**
     * 卖家名称
     */
    private String sellerName = "";

    /**
     * 费用
     */
    private BigDecimal cost = new BigDecimal(0);

    /**
     * 备注
     */
    private String remark = "";

    /**
     * 费用状态(0-未结算 1-已结算 StoreCostState)
     */
    private int costState = 0;

    /**
     * 添加时间
     */
    private String addTime;

    public int getCostId() {
        return costId;
    }

    public void setCostId(int costId) {
        this.costId = costId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getCostState() {
        return costState;
    }

    public void setCostState(int costState) {
        this.costState = costState;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "StoreCost{" +
                "costId=" + costId +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", cost=" + cost +
                ", remark='" + remark + '\'' +
                ", costState=" + costState +
                ", addTime=" + addTime +
                '}';
    }
}
