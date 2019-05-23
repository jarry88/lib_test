package com.ftofs.twant.domain.store;



public class StoreJoinin {
    /**
     * 商家编号
     */
    private int sellerId;

    /**
     * 商家用户名
     */
    private String sellerName;

    /**
     * 申请的店铺名
     */
    private String storeName;

    /**
     * 申请状态
     * 10-新申请 15-初审失败 20-初审成功 30-缴费完成 35-缴费审核失败 40-内容更新申请 90-审核成功
     */
    private int state;

    /**
     * 管理员审核信息
     */
    private String joininMessage = "";

    /**
     * 申请年限
     */
    private int joininYear;

    /**
     * 店铺等级编号
     */
    private int gradeId;

    /**
     * 店铺分类编号
     */
    private int classId;

    /**
     * 付款总额
     */
    private int payingAmount = 0;

    /**
     * 付款凭证
     */
    private String payingCertificate = "";

    /**
     * 付款凭证说明
     */
    private String payingCertificateExp = "";

    /**
     * 申请提交时间
     */
    private String joininSubmitTime;

    /**
     * 发布海外购商品
     */
    private int allowForeignGoods;

    /**
     * 店鋪形象圖
     */
    private String storeFigureImage;

    /**
     * 入駐類型 0：個人 1：企業
     */
    private int joininType;

    /**
     * 實體門店開業日期
     */
    private String startBusiness;

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

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getJoininMessage() {
        return joininMessage;
    }

    public void setJoininMessage(String joininMessage) {
        this.joininMessage = joininMessage;
    }

    public int getJoininYear() {
        return joininYear;
    }

    public void setJoininYear(int joininYear) {
        this.joininYear = joininYear;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getPayingAmount() {
        return payingAmount;
    }

    public void setPayingAmount(int payingAmount) {
        this.payingAmount = payingAmount;
    }

    public String getPayingCertificate() {
        return payingCertificate;
    }

    public String getPayingCertificateUrl() {
        return payingCertificate;
    }

    public void setPayingCertificate(String payingCertificate) {
        this.payingCertificate = payingCertificate;
    }

    public String getPayingCertificateExp() {
        return payingCertificateExp;
    }

    public void setPayingCertificateExp(String payingCertificateExp) {
        this.payingCertificateExp = payingCertificateExp;
    }

    public String getJoininSubmitTime() {
        return joininSubmitTime;
    }

    public void setJoininSubmitTime(String joininSubmitTime) {
        this.joininSubmitTime = joininSubmitTime;
    }

    public int getAllowForeignGoods() {
        return allowForeignGoods;
    }

    public void setAllowForeignGoods(int allowForeignGoods) {
        this.allowForeignGoods = allowForeignGoods;
    }

    public String getStoreFigureImage() {
        return storeFigureImage;
    }

    public void setStoreFigureImage(String storeFigureImage) {
        this.storeFigureImage = storeFigureImage;
    }

    public int getJoininType() {
        return joininType;
    }

    public void setJoininType(int joininType) {
        this.joininType = joininType;
    }

    public String getStartBusiness() {
        return startBusiness;
    }

    public void setStartBusiness(String startBusiness) {
        this.startBusiness = startBusiness;
    }

    @Override
    public String toString() {
        return "StoreJoinin{" +
                "sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", state=" + state +
                ", joininMessage='" + joininMessage + '\'' +
                ", joininYear=" + joininYear +
                ", gradeId=" + gradeId +
                ", classId=" + classId +
                ", payingAmount=" + payingAmount +
                ", payingCertificate='" + payingCertificate + '\'' +
                ", payingCertificateExp='" + payingCertificateExp + '\'' +
                ", joininSubmitTime=" + joininSubmitTime +
                ", allowForeignGoods=" + allowForeignGoods +
                ", joininType=" + joininType +
                '}';
    }
}

