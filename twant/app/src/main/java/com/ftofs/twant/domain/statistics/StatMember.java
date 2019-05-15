package com.ftofs.twant.domain.statistics;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class StatMember implements Serializable {
    /**
     * 自增ID
     */
    private int statId;

    /**
     * 会员ID
     */
    private int memberId = 0;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 日期(具体到天)
     */
    private Timestamp statDate;

    /**
     * 下单量
     */
    private long ordersNum = 0;

    /**
     * 下单金额
     */
    private BigDecimal ordersAmount = new BigDecimal(0);

    /**
     * 预存款增加金额
     */
    private BigDecimal predepositIncrease = new BigDecimal(0);

    /**
     * 预存款减少金额
     */
    private BigDecimal predepositReduce = new BigDecimal(0);

    /**
     * 积分增加金额
     */
    private long pointsIncrease = 0;

    /**
     * 积分减少金额
     */
    private long pointsReduce = 0;

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
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

    public Timestamp getStatDate() {
        return statDate;
    }

    public void setStatDate(Timestamp statDate) {
        this.statDate = statDate;
    }

    public long getOrdersNum() {
        return ordersNum;
    }

    public void setOrdersNum(long ordersNum) {
        this.ordersNum = ordersNum;
    }

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
    }

    public BigDecimal getPredepositIncrease() {
        return predepositIncrease;
    }

    public void setPredepositIncrease(BigDecimal predepositIncrease) {
        this.predepositIncrease = predepositIncrease;
    }

    public BigDecimal getPredepositReduce() {
        return predepositReduce;
    }

    public void setPredepositReduce(BigDecimal predepositReduce) {
        this.predepositReduce = predepositReduce;
    }

    public long getPointsIncrease() {
        return pointsIncrease;
    }

    public void setPointsIncrease(long pointsIncrease) {
        this.pointsIncrease = pointsIncrease;
    }

    public long getPointsReduce() {
        return pointsReduce;
    }

    public void setPointsReduce(long pointsReduce) {
        this.pointsReduce = pointsReduce;
    }

    @Override
    public String toString() {
        return "StatMember{" +
                "statId=" + statId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", statDate=" + statDate +
                ", ordersNum=" + ordersNum +
                ", ordersAmount=" + ordersAmount +
                ", predepositIncrease=" + predepositIncrease +
                ", predepositReduce=" + predepositReduce +
                ", pointsIncrease=" + pointsIncrease +
                ", pointsReduce=" + pointsReduce +
                '}';
    }
}