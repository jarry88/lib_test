package com.ftofs.twant.domain.store;

import java.math.BigDecimal;

public class Renew {
    /**
     * 主键
     */
    private int renewId;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 店铺等级编号
     */
    private int gradeId;

    /**
     * 店铺等级名称
     */
    private String gradeName;

    /**
     * 等级收费(MOP/年)
     */
    private BigDecimal renewPrice = new BigDecimal(0);

    /**
     * 续签时长
     */
    private int renewYear;

    /**
     * 应支付金额
     */
    private BigDecimal payAmount = new BigDecimal(0);

    /**
     * 申请时间
     */
    private String createTime;

    /**
     * 状态 0-默认待审核，1-审核通过
     */
    private int renewState = 0;

    /**
     * 有效期开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 付款凭证
     */
    private String payCert;

    /**
     * 付款备注
     */
    private String payCertNote;

    private String payCertSrc;

    private String payCertStateText;

    /**
     * 发布海外购產品
     */
    private int allowForeignGoods;

    public int getRenewId() {
        return renewId;
    }

    public void setRenewId(int renewId) {
        this.renewId = renewId;
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

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public BigDecimal getRenewPrice() {
        return renewPrice;
    }

    public void setRenewPrice(BigDecimal renewPrice) {
        this.renewPrice = renewPrice;
    }

    public int getRenewYear() {
        return renewYear;
    }

    public void setRenewYear(int renewYear) {
        this.renewYear = renewYear;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public int getRenewState() {
        return renewState;
    }

    public void setRenewState(int renewState) {
        this.renewState = renewState;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPayCert() {
        return payCert;
    }

    public void setPayCert(String payCert) {
        this.payCert = payCert;
    }

    public String getPayCertNote() {
        return payCertNote;
    }

    public void setPayCertNote(String payCertNote) {
        this.payCertNote = payCertNote;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayCertSrc() {
        return payCert;
    }

    public void setPayCertSrc(String payCertSrc) {
        this.payCertSrc = payCertSrc;
    }

    public String getPayCertStateText() {
        return payCertStateText;
    }

    public void setPayCertStateText(String payCertStateText) {
        this.payCertStateText = payCertStateText;
    }

    public int getAllowForeignGoods() {
        return allowForeignGoods;
    }

    public void setAllowForeignGoods(int allowForeignGoods) {
        this.allowForeignGoods = allowForeignGoods;
    }

    @Override
    public String toString() {
        return "Renew{" +
                "renewId=" + renewId +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", gradeId=" + gradeId +
                ", gradeName='" + gradeName + '\'' +
                ", renewPrice=" + renewPrice +
                ", renewYear=" + renewYear +
                ", payAmount=" + payAmount +
                ", createTime=" + createTime +
                ", renewState=" + renewState +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", payCert='" + payCert + '\'' +
                ", payCertNote='" + payCertNote + '\'' +
                ", payCertSrc='" + payCertSrc + '\'' +
                ", payCertStateText='" + payCertStateText + '\'' +
                ", allowForeignGoods=" + allowForeignGoods +
                '}';
    }
}
