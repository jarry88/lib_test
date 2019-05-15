package com.ftofs.twant.vo.evaluate;

import com.ftofs.twant.domain.orders.Orders;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 评价订单Vo
 *
 * @author dqw
 * Created 2017/10/24 14:56
 */
public class EvaluateOrdersVo {
    /**
     * 订单ID
     */
    private int ordersId;
    /**
     * 订单编号
     */
    private long ordersSn;
    /**
     * 订单类型
     */
    private int ordersType;
    /**
     * 店铺编号
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
    private String memberName;

    public EvaluateOrdersVo() {
    }

    public EvaluateOrdersVo(Orders orders) {
        this.ordersId = orders.getOrdersId();
        this.ordersSn = orders.getOrdersSn();
        this.storeId = orders.getStoreId();
        this.storeName = orders.getStoreName();
        this.memberId = orders.getMemberId();
        this.memberName = orders.getMemberName();
        this.ordersType = 1;
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

    @Override
    public String toString() {
        return "EvaluateOrdersVo{" +
                "ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", ordersType=" + ordersType +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}

