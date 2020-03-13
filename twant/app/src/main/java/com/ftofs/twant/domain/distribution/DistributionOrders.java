package com.ftofs.twant.domain.distribution;

import java.io.Serializable;

public class DistributionOrders implements Serializable {
    private int distributionOrdersId;

    /**
     * 买家会员编号
     */
    private int memberId;

    /**
     * 推广商编号
     */
    private int distributorId;

    /**
     * 產品SPU编号
     */
    private int commonId;

    /**
     * 產品店铺编号
     */
    private int storeId;

    /**
     * 创建时间
     */
    private String addTime;

    /**
     * 佣金（%）
     */
    private int commissionRate;

    /**
     * 订单產品编号
     */
    private int ordersGoodsId;

    /**
     * 订单完成时间
     */
    private String ordersFinishTime;

    /**
     * 结算完成时间
     */
    private String finishTime;

    /**
     * 分销订单状态
     */
    private int distributionOrdersType = 1;

    /**
     * 店铺结算标识,店铺结算时根据此标识扣除店铺结算金额
     */

    private int distributionStorePay = 0;

    /**
     * 店铺结算标识,店铺结算时根据此标识扣除店铺结算时间
     */

    private String distributionStorePayTime ;


    public int getDistributionStorePay() {
        return distributionStorePay;
    }

    public void setDistributionStorePay(int distributionStorePay) {
        this.distributionStorePay = distributionStorePay;
    }

    public String getDistributionStorePayTime() {
        return distributionStorePayTime;
    }

    public void setDistributionStorePayTime(String distributionStorePayTime) {
        this.distributionStorePayTime = distributionStorePayTime;
    }

    public int getDistributionOrdersId() {
        return distributionOrdersId;
    }

    public void setDistributionOrdersId(int distributionOrdersId) {
        this.distributionOrdersId = distributionOrdersId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(int distributorId) {
        this.distributorId = distributorId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public int getOrdersGoodsId() {
        return ordersGoodsId;
    }

    public void setOrdersGoodsId(int ordersGoodsId) {
        this.ordersGoodsId = ordersGoodsId;
    }

    public String getOrdersFinishTime() {
        return ordersFinishTime;
    }

    public void setOrdersFinishTime(String ordersFinishTime) {
        this.ordersFinishTime = ordersFinishTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public int getDistributionOrdersType() {
        return distributionOrdersType;
    }

    public void setDistributionOrdersType(int distributionOrdersType) {
        this.distributionOrdersType = distributionOrdersType;
    }

    @Override
    public String toString() {
        return "DistributionOrders{" +
                "distributionOrdersId=" + distributionOrdersId +
                ", memberId=" + memberId +
                ", distributorId=" + distributorId +
                ", commonId=" + commonId +
                ", storeId=" + storeId +
                ", addTime=" + addTime +
                ", commissionRate=" + commissionRate +
                ", ordersGoodsId=" + ordersGoodsId +
                ", ordersFinishTime=" + ordersFinishTime +
                ", finishTime=" + finishTime +
                ", distributionOrdersType=" + distributionOrdersType +
                ", distributionStorePay=" + distributionStorePay +
                ", distributionStorePayTime=" + distributionStorePayTime +
                '}';
    }
}
