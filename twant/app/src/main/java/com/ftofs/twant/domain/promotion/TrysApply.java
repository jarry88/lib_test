package com.ftofs.twant.domain.promotion;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

public class TrysApply {
    /**
     * 主键
     */
    private int applyId;

    /**
     * 试用表主键
     */
    private int trysId;

    /**
     * 申请时间
     */
    private String applyTime;

    /**
     * 会员Id
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

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
     * 申请状态 0-申请中，1-申请成功，2-申请失败
     * 付邮试用申请成功后送券，返券试用审核成功后可以以返券资格下单
     */
    private int applyState = 0;

    /**
     * 会员手机号
     */
    private String memberMobile;

    /**
     * 会员Email
     */
    private String memberEmail;

    /**
     * 试用人性别
     */
    private Integer sex;

    /**
     * 试用人年龄
     */
    private Integer age;

    /**
     * 评价星级
     */
    private Integer star;

    /**
     * 评价亮点与建议
     */
    private String suggest;

    /**
     * 上传的第一张图片作为主图
     */
    private String mainImage;

    /**
     * 试用过程及感受[文字]
     */
    private String content;

    /**
     * 试用过程及感受[图片]
     */
    private String contentImage;

    /**
     * 评价相关订单Id
     */
    private Integer ordersId;

    /**
     * 提交评价时间
     */
    private String reportTime;

    /**
     * 申请状态 0-审核中，1-审核成功，2-审核失败
     * 返券试用审核成功后送券
     */
    private Integer reportState;

    /**
     * 审核失败原因
     */
    private String reportFailReason;

    /**
     * 商品ID
     */
    private int commonId;

    /**
     * 商品ID
     */
    private int goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 价格
     */
    private BigDecimal goodsPrice;

    /**
     * 试用类型 1-付邮费试用 2返券试用
     */
    private int trysType = 1;

    /**
     * 是否以试用资格购买过商品，0-否，1-是
     */
    private int buyState = 0;

    /**
     * 试用资格购买商品的最后期限
     */
    private String buyEndTime;

    /**
     * 获得试用资格后，提交试用报告的最后期限
     */
    private String reportEndTime;

    /**
     * 是否送出过店铺券(只针对返券试用有效)
     */
    private int sendVoucherState = 0;

    /**
     * 图片名称
     */
    private String imageName;

    /**
     * 头像
     */
    private String avatar = "";

    /**
     * 试用商品分类
     */
    private int categoryId;

    /**
     * 完整规格
     * 例“颜色：红色，尺码：L”
     */
    private String goodsFullSpecs;

    /**
     * 是否被系统自动加入黑名单
     */
    private int autoAddBlackState = 0;

    /**
     * 头像路径
     */
    private String avatarUrl = "";

    /**
     * 申请状态描述
     */
    private String applyStateText;

    /**
     * 试用报告审核状态
     */
    private String reportStateText;

    private String trysTypeText;

    private String memberNameEncrypt;

    private String sexText;

    private String ageText;

    private String goodsFullName;

    /**
     * 图片路径
     */
    private String imageSrc;

    /**
     * 图片路径
     */
    private String mainImageSrc;

    private List<String> contentList = new ArrayList<>();

    private List<String> imageList = new ArrayList<>();

    /**
     * 会员中心试用申请列表显示状态描述
     */
    private String showMemberStateText;

    /**
     * 会员中心试用申请列表 是否显示提交试用报告按钮
     */
    private int showMemberAddReportState;

    /**
     * 会员中心试用申请列表 是否显示编辑试用报告按钮
     */
    private int showMemberEditReportState;

    /**
     * 会员中心试用申请列表 是否显示查看试用报告按钮
     */
    private int showMemberViewReportState;

    /**
     * 会员中心试用申请列表 是否显示购买试用商品按钮
     */
    private int showMemberBuyState;

    /**
     * 会员中心试用申请列表 是否显示查看订单按钮
     */
    private int showMemberViewOrdersState;

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public int getTrysId() {
        return trysId;
    }

    public void setTrysId(int trysId) {
        this.trysId = trysId;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
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

    public int getApplyState() {
        return applyState;
    }

    public void setApplyState(int applyState) {
        this.applyState = applyState;
    }

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentImage() {
        return contentImage;
    }

    public void setContentImage(String contentImage) {
        this.contentImage = contentImage;
    }

    public Integer getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(Integer ordersId) {
        this.ordersId = ordersId;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getReportState() {
        return reportState;
    }

    public void setReportState(Integer reportState) {
        this.reportState = reportState;
    }

    public String getReportFailReason() {
        return reportFailReason;
    }

    public void setReportFailReason(String reportFailReason) {
        this.reportFailReason = reportFailReason;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getTrysType() {
        return trysType;
    }

    public void setTrysType(int trysType) {
        this.trysType = trysType;
    }

    public int getBuyState() {
        return buyState;
    }

    public void setBuyState(int buyState) {
        this.buyState = buyState;
    }

    public String getBuyEndTime() {
        return buyEndTime;
    }

    public void setBuyEndTime(String buyEndTime) {
        this.buyEndTime = buyEndTime;
    }

    public String getReportEndTime() {
        return reportEndTime;
    }

    public void setReportEndTime(String reportEndTime) {
        this.reportEndTime = reportEndTime;
    }

    public int getSendVoucherState() {
        return sendVoucherState;
    }

    public void setSendVoucherState(int sendVoucherState) {
        this.sendVoucherState = sendVoucherState;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public int getAutoAddBlackState() {
        return autoAddBlackState;
    }

    public void setAutoAddBlackState(int autoAddBlackState) {
        this.autoAddBlackState = autoAddBlackState;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getApplyStateText() {
        return applyStateText;
    }

    public void setApplyStateText(String applyStateText) {
        this.applyStateText = applyStateText;
    }

    public String getReportStateText() {
        return reportStateText;
    }

    public void setReportStateText(String reportStateText) {
        this.reportStateText = reportStateText;
    }

    public String getTrysTypeText() {
        return trysTypeText;
    }

    public void setTrysTypeText(String trysTypeText) {
        this.trysTypeText = trysTypeText;
    }

    public String getMemberNameEncrypt() {
        return memberNameEncrypt;
    }

    public void setMemberNameEncrypt(String memberNameEncrypt) {
        this.memberNameEncrypt = memberNameEncrypt;
    }

    public String getSexText() {
        return sexText;
    }

    public void setSexText(String sexText) {
        this.sexText = sexText;
    }

    public String getAgeText() {
        return ageText;
    }

    public void setAgeText(String ageText) {
        this.ageText = ageText;
    }

    public String getGoodsFullName() {
        return goodsFullName;
    }

    public void setGoodsFullName(String goodsFullName) {
        this.goodsFullName = goodsFullName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getMainImageSrc() {
        return mainImageSrc;
    }

    public void setMainImageSrc(String mainImageSrc) {
        this.mainImageSrc = mainImageSrc;
    }

    public List<String> getContentList() {
        return contentList;
    }

    public void setContentList(List<String> contentList) {
        this.contentList = contentList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getShowMemberStateText() {
        return showMemberStateText;
    }

    public void setShowMemberStateText(String showMemberStateText) {
        this.showMemberStateText = showMemberStateText;
    }

    public int getShowMemberAddReportState() {
        return showMemberAddReportState;
    }

    public void setShowMemberAddReportState(int showMemberAddReportState) {
        this.showMemberAddReportState = showMemberAddReportState;
    }

    public int getShowMemberEditReportState() {
        return showMemberEditReportState;
    }

    public void setShowMemberEditReportState(int showMemberEditReportState) {
        this.showMemberEditReportState = showMemberEditReportState;
    }

    public int getShowMemberViewReportState() {
        return showMemberViewReportState;
    }

    public void setShowMemberViewReportState(int showMemberViewReportState) {
        this.showMemberViewReportState = showMemberViewReportState;
    }

    public int getShowMemberBuyState() {
        return showMemberBuyState;
    }

    public void setShowMemberBuyState(int showMemberBuyState) {
        this.showMemberBuyState = showMemberBuyState;
    }

    public int getShowMemberViewOrdersState() {
        return showMemberViewOrdersState;
    }

    public void setShowMemberViewOrdersState(int showMemberViewOrdersState) {
        this.showMemberViewOrdersState = showMemberViewOrdersState;
    }

    @Override
    public String toString() {
        return "TrysApply{" +
                "applyId=" + applyId +
                ", trysId=" + trysId +
                ", applyTime=" + applyTime +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", storeId=" + storeId +
                ", sellerId=" + sellerId +
                ", storeName='" + storeName + '\'' +
                ", applyState=" + applyState +
                ", memberMobile='" + memberMobile + '\'' +
                ", memberEmail='" + memberEmail + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", star=" + star +
                ", suggest='" + suggest + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", content='" + content + '\'' +
                ", contentImage='" + contentImage + '\'' +
                ", ordersId=" + ordersId +
                ", reportTime=" + reportTime +
                ", reportState=" + reportState +
                ", reportFailReason='" + reportFailReason + '\'' +
                ", commonId=" + commonId +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", trysType=" + trysType +
                ", buyState=" + buyState +
                ", buyEndTime=" + buyEndTime +
                ", reportEndTime=" + reportEndTime +
                ", sendVoucherState=" + sendVoucherState +
                ", imageName='" + imageName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", categoryId=" + categoryId +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", autoAddBlackState=" + autoAddBlackState +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", applyStateText='" + applyStateText + '\'' +
                ", reportStateText='" + reportStateText + '\'' +
                ", trysTypeText='" + trysTypeText + '\'' +
                ", memberNameEncrypt='" + memberNameEncrypt + '\'' +
                ", sexText='" + sexText + '\'' +
                ", ageText='" + ageText + '\'' +
                ", goodsFullName='" + goodsFullName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", mainImageSrc='" + mainImageSrc + '\'' +
                ", contentList=" + contentList +
                ", imageList=" + imageList +
                ", showMemberStateText='" + showMemberStateText + '\'' +
                ", showMemberAddReportState=" + showMemberAddReportState +
                ", showMemberEditReportState=" + showMemberEditReportState +
                ", showMemberViewReportState=" + showMemberViewReportState +
                ", showMemberBuyState=" + showMemberBuyState +
                ", showMemberViewOrdersState=" + showMemberViewOrdersState +
                '}';
    }

}
