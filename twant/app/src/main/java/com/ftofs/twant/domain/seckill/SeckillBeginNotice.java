package com.ftofs.twant.domain.seckill;

import java.sql.Timestamp;

public class SeckillBeginNotice {
    /**
     * 通知编号
     */
    private int arrivalId;

    /**
     * 商品SPU编号
     */
    private int commonId;

    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 开始时间
     */
    private Timestamp startTime = new Timestamp(0);

    public int getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(int arrivalId) {
        this.arrivalId = arrivalId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "SeckillBeginNotice{" +
                "arrivalId=" + arrivalId +
                ", commonId=" + commonId +
                ", memberId=" + memberId +
                ", startTime=" + startTime +
                '}';
    }
}
