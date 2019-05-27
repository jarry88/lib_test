package com.ftofs.twant.domain.evaluate;

public class EvaluateStore {
    /**
     * 自增编码
     */
    private int evaluateId;

    /**
     * 订单编码
     */
    private int ordersId;

    /**
     * 订单编号
     */
    private long ordersSn;

    /**
     * 订单类型 0-普通订单 1-虚拟订单
     */
    private int ordersType;

    /**
     * 店铺ID
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 评价时间
     */
    private String evaluateTime;

    /**
     * 描述相符评分
     */
    private int descriptionCredit;

    /**
     * 服务态度评分
     */
    private int serviceCredit;

    /**
     * 发货速度评分
     */
    private int deliveryCredit;

    public int getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public long getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(long ordersSn) {
        this.ordersSn = ordersSn;
    }

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
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

    public String getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getDescriptionCredit() {
        return descriptionCredit;
    }

    public void setDescriptionCredit(int descriptionCredit) {
        this.descriptionCredit = descriptionCredit;
    }

    public int getServiceCredit() {
        return serviceCredit;
    }

    public void setServiceCredit(int serviceCredit) {
        this.serviceCredit = serviceCredit;
    }

    public int getDeliveryCredit() {
        return deliveryCredit;
    }

    public void setDeliveryCredit(int deliveryCredit) {
        this.deliveryCredit = deliveryCredit;
    }

    @Override
    public String toString() {
        return "EvaluateStore{" +
                "evaluateId=" + evaluateId +
                ", ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", ordersType=" + ordersType +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", evaluateTime=" + evaluateTime +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", descriptionCredit=" + descriptionCredit +
                ", serviceCredit=" + serviceCredit +
                ", deliveryCredit=" + deliveryCredit +
                '}';
    }
}

