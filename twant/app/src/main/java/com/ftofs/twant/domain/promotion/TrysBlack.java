package com.ftofs.twant.domain.promotion;

public class TrysBlack {
    /**
     * 主键
     */
    private int memberId;

    /**
     * 试用ID
     */
    private int trysId;

    /**
     * 商品ID
     */
    private int commonId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 商家编号
     */
    private int sellerId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 进入黑名单原因,0-未提交试用报告，1-提交劣质的试用报告
     */
    private int reason = 0;

    /**
     * 创建时间
     */
    private String addTime;

    /**
     * 试用类型 1-付邮费试用 2返券试用
     */
    private int trysType = 1;

    /**
     * 图片名称
     */
    private String imageName;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 头像
     */
    private String avatar = "";

    /**
     * 图片路径
     */
    private String imageSrc;

    /**
     * 进入黑名单原因
     */
    private String reasonText;

    /**
     * 头像路径
     */
    private String avatarUrl = "";

    private String trysTypeText;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getTrysId() {
        return trysId;
    }

    public void setTrysId(int trysId) {
        this.trysId = trysId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getTrysType() {
        return trysType;
    }

    public void setTrysType(int trysType) {
        this.trysType = trysType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getTrysTypeText() {
        return trysTypeText;
    }

    public void setTrysTypeText(String trysTypeText) {
        this.trysTypeText = trysTypeText;
    }

    @Override
    public String toString() {
        return "TrysBlack{" +
                "memberId=" + memberId +
                ", trysId=" + trysId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", storeId=" + storeId +
                ", sellerId=" + sellerId +
                ", storeName='" + storeName + '\'' +
                ", reason=" + reason +
                ", addTime=" + addTime +
                ", trysType=" + trysType +
                ", imageName='" + imageName + '\'' +
                ", memberName='" + memberName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", reasonText='" + reasonText + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", trysTypeText='" + trysTypeText + '\'' +
                '}';
    }
}
