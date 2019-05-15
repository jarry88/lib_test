package com.ftofs.twant.domain.orders;

import java.io.Serializable;

public class OrdersPay implements Serializable {
    /**
     * 编号
     * 主键、自增
     */
    private int payId;

    /**
     * 支付单号
     */
    private long paySn;

    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 订单支付类型
     */
    private int payOrdersType = 0;

    /**
     * 使用的平台券Id，多个Id以，分开
     */
    private String couponId;

    /**
     * 平台是否已经返还(订单取消时会返还)
     */
    private int couponReturn;

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public long getPaySn() {
        return paySn;
    }

    public void setPaySn(long paySn) {
        this.paySn = paySn;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getPayOrdersType() {
        return payOrdersType;
    }

    public void setPayOrdersType(int payOrdersType) {
        this.payOrdersType = payOrdersType;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public int getCouponReturn() {
        return couponReturn;
    }

    public void setCouponReturn(int couponReturn) {
        this.couponReturn = couponReturn;
    }

    @Override
    public String toString() {
        return "OrdersPay{" +
                "payId=" + payId +
                ", paySn=" + paySn +
                ", memberId=" + memberId +
                ", payOrdersType=" + payOrdersType +
                ", couponId=" + couponId +
                '}';
    }
}
