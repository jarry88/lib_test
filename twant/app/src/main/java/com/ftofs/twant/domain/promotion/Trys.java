package com.ftofs.twant.domain.promotion;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Trys {
    /**
     * 主键
     */
    private int trysId;

    /**
     * 商品SPU
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
     * 开始时间
     */
    private Timestamp startTime;

    /**
     * 结束时间
     */
    private Timestamp endTime;

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
     * 提供份数
     */
    private int provideNum;

    /**
     * 允许申请人数
     */
    private int maxNum;

    /**
     * 商品价值
     */
    private BigDecimal goodsPrice;

    /**
     * 店铺券活动ID
     */
    private int voucherTemplateId = 0;

    /**
     * 店铺券面额
     */
    private BigDecimal voucherPrice = new BigDecimal(0);

    /**
     * 当前申请人数
     */
    private int currentNum = 0;

    /**
     * 活动状态 1正在进行 2即将开始 3已经过期
     */
    private int trysState;

    /**
     * 活动创建时间
     */
    private Timestamp addTime;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 详情类型 1-使用原商品详情,0-自定义商品详情
     */
    private int contentType = 1;

    /**
     * 试用类型 1-付邮费试用 2返券试用
     */
    private int trysType = 1;

    /**
     * 图片名称
     */
    private String imageName;

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
     * 申请通过的试用报告份数
     */
    private int trysReportNum = 0;

    /**
     * 图片路径
     */
    private String imageSrc;

    /**
     * 试用状态中文
     */
    private String trysStateText;

    /**
     * 由时间得到试用状态[此状态最准确]
     */
    private int trysStateFromTime;

    /**
     * 是否允许编辑[未开始的试用可以编辑，其它情况均不可编辑]
     */
    private int allowEdit = 0;

    /**
     * 是否允许删除[未开始的试用可以删除，已经过期且没有人申请的试用可以删除，其它情况均不可删除]
     */
    private int allowDelete = 0;

    /**
     * 试用分类名称
     */
    private String categoryName;

    /**
     * 页面上显示允许操作的类型标识
     * 1 - 可以申请试用
     * 2 - 正在申请
     * 3 - 即将开始
     * 4 - 已经结束
     */
    private int showTrysButtonState;

    private String goodsFullName;

    private String trysTypeText;

    /**
     * 会员是否已申请，前台页面会员登录后判断是否已经申请用
     */
    private int isApply = 0;

    /**
     * 开始时间倒计时
     */
    private long endTimeDownTime;

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

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
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

    public int getProvideNum() {
        return provideNum;
    }

    public void setProvideNum(int provideNum) {
        this.provideNum = provideNum;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getVoucherTemplateId() {
        return voucherTemplateId;
    }

    public void setVoucherTemplateId(int voucherTemplateId) {
        this.voucherTemplateId = voucherTemplateId;
    }

    public BigDecimal getVoucherPrice() {
        return voucherPrice;
    }

    public void setVoucherPrice(BigDecimal voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
    }

    public int getTrysState() {
        return trysState;
    }

    public void setTrysState(int trysState) {
        this.trysState = trysState;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTrysType() {
        return trysType;
    }

    public void setTrysType(int trysType) {
        this.trysType = trysType;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getTrysStateText() {
        return trysStateText;
    }

    public void setTrysStateText(String trysStateText) {
        this.trysStateText = trysStateText;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(int allowEdit) {
        this.allowEdit = allowEdit;
    }

    public int getTrysStateFromTime() {
        return trysStateFromTime;
    }

    public void setTrysStateFromTime(int trysStateFromTime) {
        this.trysStateFromTime = trysStateFromTime;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(int allowDelete) {
        this.allowDelete = allowDelete;
    }

    public int getShowTrysButtonState() {
        return showTrysButtonState;
    }

    public void setShowTrysButtonState(int showTrysButtonState) {
        this.showTrysButtonState = showTrysButtonState;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public String getGoodsFullName() {
        return goodsName + " " + (goodsFullSpecs == null ? "" : goodsFullSpecs);
    }

    public void setGoodsFullName(String goodsFullName) {
        this.goodsFullName = goodsFullName;
    }

    public String getTrysTypeText() {
        return trysTypeText;
    }

    public void setTrysTypeText(String trysTypeText) {
        this.trysTypeText = trysTypeText;
    }

    public long getEndTimeDownTime() {
        return 0;
    }

    public void setEndTimeDownTime(long endTimeDownTime) {
        this.endTimeDownTime = endTimeDownTime;
    }

    public int getTrysReportNum() {
        return trysReportNum;
    }

    public void setTrysReportNum(int trysReportNum) {
        this.trysReportNum = trysReportNum;
    }

    public int getIsApply() {
        return isApply;
    }

    public void setIsApply(int isApply) {
        this.isApply = isApply;
    }

    @Override
    public String toString() {
        return "Trys{" +
                "trysId=" + trysId +
                ", commonId=" + commonId +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", storeId=" + storeId +
                ", sellerId=" + sellerId +
                ", storeName='" + storeName + '\'' +
                ", provideNum=" + provideNum +
                ", maxNum=" + maxNum +
                ", goodsPrice=" + goodsPrice +
                ", voucherTemplateId=" + voucherTemplateId +
                ", voucherPrice=" + voucherPrice +
                ", currentNum=" + currentNum +
                ", trysState=" + trysState +
                ", addTime=" + addTime +
                ", content='" + content + '\'' +
                ", contentType=" + contentType +
                ", trysType=" + trysType +
                ", imageName='" + imageName + '\'' +
                ", categoryId=" + categoryId +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", trysReportNum=" + trysReportNum +
                ", imageSrc='" + imageSrc + '\'' +
                ", trysStateText='" + trysStateText + '\'' +
                ", trysStateFromTime=" + trysStateFromTime +
                ", allowEdit=" + allowEdit +
                ", allowDelete=" + allowDelete +
                ", categoryName='" + categoryName + '\'' +
                ", showTrysButtonState=" + showTrysButtonState +
                ", goodsFullName='" + goodsFullName + '\'' +
                ", trysTypeText='" + trysTypeText + '\'' +
                ", isApply=" + isApply +
                ", endTimeDownTime=" + endTimeDownTime +
                '}';
    }
}
