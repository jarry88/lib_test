package com.ftofs.twant.domain.promotion;

import java.io.Serializable;

import java.util.Calendar;

public class PromotionQuota implements Serializable {
    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 店铺券结束时间
     */
    private String voucherEndTime;

    /**
     * 显示折扣结束时间
     */
    private String discountEndTime;

    /**
     * 推荐组合结束时间
     */
    private String comboEndTime;

    /**
     * 满优惠结束时间
     */
    private String conformEndTime;

    /**
     * 预售商品结束时间
     */
    private String bookEndTime;

    /**
     * 优惠套装结束时间
     */
    private String bundlingEndTime;

    /**
     * 商品赠品结束时间
     */
    private String giftEndTime;

    /**
     * 多人拼团结束时间
     */
    private String groupEndTime;

    /**
     * 试用结束时间
     */
    private String trysEndTime;

    /**
     * 积分商品结束时间
     */
    private String pointsGoodsEndTime;

    /**
     * 商城活动结束时间
     */
    private String themeEndTime;

    /**
     * 秒杀活动结束时间
     */
    private String seckillEndTime;

    /**
     * 砍价活动结束时间
     */
    private String bargainEndTime;

    /**
     * 店铺券是否允许续期
     */
    private Integer voucherAllowRenewal;

    /**
     * 限时折扣是否允许续期
     */
    private Integer discountAllowRenewal;

    /**
     * 推荐组合是否允许续期
     */
    private Integer comboAllowRenewal;

    /**
     * 满优惠是否允许续期
     */
    private Integer conformAllowRenewal;

    /**
     * 预售商品是否允许续期
     */
    private Integer bookAllowRenewal;

    /**
     * 满优惠是否允许续费
     */
    private Integer bundlingAllowRenewal;

    /**
     * 商品赠品是否允许续期
     */
    private Integer giftAllowRenewal;

    /**
     * 多人拼团是否允许续期
     */
    private Integer groupAllowRenewal;

    /**
     * 试用是否允许续期
     */
    private Integer trysAllowRenewal;

    /**
     * 积分商品是否允许续期
     */
    private Integer pointsGoodsAllowRenewal;

    /**
     * 商城活动是否允许续期
     */
    private Integer themeAllowRenewal;

    /**
     * 秒杀活动是否允许续期
     */
    private Integer seckillAllowRenewal;

    /**
     * 砍价活动是否允许续期
     */
    private Integer bargainAllowRenewal;

    /**
     * 店铺券促销是否可用
     */
    private Integer voucherUsable;

    /**
     * 限时折扣是否可用
     */
    private Integer discountUsable;

    /**
     * 推荐组合是否可用
     */
    private Integer comboUsable;

    /**
     * 满优惠是否可用
     */
    private Integer conformUsable;

    /**
     * 预售商品是否可用
     */
    private Integer bookUsable;

    /**
     * 优惠套装是否可用
     */
    private Integer bundlingUsable;

    /**
     * 商品赠品是否可用
     */
    private Integer giftUsable;

    /**
     * 多人拼团是否可用
     */
    private Integer groupUsable;

    /**
     * 试用是否可用
     */
    private Integer trysUsable;

    /**
     * 积分商品是否可用
     */
    private Integer pointsGoodsUsable;

    /**
     * 商城活动是否可用
     */
    private Integer themeUsable;

    /**
     * 秒杀活动是否可用
     */
    private Integer seckillUsable;

    /**
     * 砍价活动是否可用
     */
    private Integer bargainUsable;

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

    public String getVoucherEndTime() {
        return voucherEndTime;
    }

    public void setVoucherEndTime(String voucherEndTime) {
        this.voucherEndTime = voucherEndTime;
    }

    public String getDiscountEndTime() {
        return discountEndTime;
    }

    public void setDiscountEndTime(String discountEndTime) {
        this.discountEndTime = discountEndTime;
    }

    public String getComboEndTime() {
        return comboEndTime;
    }

    public void setComboEndTime(String comboEndTime) {
        this.comboEndTime = comboEndTime;
    }

    public String getConformEndTime() {
        return conformEndTime;
    }

    public void setConformEndTime(String conformEndTime) {
        this.conformEndTime = conformEndTime;
    }

    public String getBookEndTime() {
        return bookEndTime;
    }

    public void setBookEndTime(String bookEndTime) {
        this.bookEndTime = bookEndTime;
    }

    public String getBundlingEndTime() {
        return bundlingEndTime;
    }

    public void setBundlingEndTime(String bundlingEndTime) {
        this.bundlingEndTime = bundlingEndTime;
    }

    public String getGiftEndTime() {
        return giftEndTime;
    }

    public void setGiftEndTime(String giftEndTime) {
        this.giftEndTime = giftEndTime;
    }

    public String getGroupEndTime() {
        return groupEndTime;
    }

    public void setGroupEndTime(String groupEndTime) {
        this.groupEndTime = groupEndTime;
    }

    public String getTrysEndTime() {
        return trysEndTime;
    }

    public void setTrysEndTime(String trysEndTime) {
        this.trysEndTime = trysEndTime;
    }

    public String getPointsGoodsEndTime() {
        return pointsGoodsEndTime;
    }

    public void setPointsGoodsEndTime(String pointsGoodsEndTime) {
        this.pointsGoodsEndTime = pointsGoodsEndTime;
    }

    public String getThemeEndTime() {
        return themeEndTime;
    }

    public void setThemeEndTime(String themeEndTime) {
        this.themeEndTime = themeEndTime;
    }

    public String getSeckillEndTime() {
        return seckillEndTime;
    }

    public void setSeckillEndTime(String seckillEndTime) {
        this.seckillEndTime = seckillEndTime;
    }

    public String getBargainEndTime() {
        return bargainEndTime;
    }

    public void setBargainEndTime(String bargainEndTime) {
        this.bargainEndTime = bargainEndTime;
    }

    public Integer getVoucherAllowRenewal() {
        return getAllowRenewal(voucherAllowRenewal, voucherEndTime);
    }

    public void setVoucherAllowRenewal(Integer voucherAllowRenewal) {
        this.voucherAllowRenewal = voucherAllowRenewal;
    }

    public Integer getDiscountAllowRenewal() {
        return getAllowRenewal(discountAllowRenewal, discountEndTime);
    }

    public void setDiscountAllowRenewal(Integer discountAllowRenewal) {
        this.discountAllowRenewal = discountAllowRenewal;
    }

    public Integer getComboAllowRenewal() {
        return getAllowRenewal(comboAllowRenewal, comboEndTime);
    }

    public void setComboAllowRenewal(Integer comboAllowRenewal) {
        this.comboAllowRenewal = comboAllowRenewal;
    }

    public Integer getConformAllowRenewal() {
        return getAllowRenewal(conformAllowRenewal, conformEndTime);
    }

    public void setConformAllowRenewal(Integer conformAllowRenewal) {
        this.conformAllowRenewal = conformAllowRenewal;
    }

    public Integer getBookAllowRenewal() {
        return getAllowRenewal(bookAllowRenewal, bookEndTime);
    }

    public void setBookAllowRenewal(Integer bookAllowRenewal) {
        this.bookAllowRenewal = bookAllowRenewal;
    }

    public Integer getBundlingAllowRenewal() {
        return getAllowRenewal(bundlingAllowRenewal, bundlingEndTime);
    }

    public void setBundlingAllowRenewal(Integer bundlingAllowRenewal) {
        this.bundlingAllowRenewal = bundlingAllowRenewal;
    }

    public Integer getGiftAllowRenewal() {
        return getAllowRenewal(giftAllowRenewal, giftEndTime);
    }

    public void setGiftAllowRenewal(Integer giftAllowRenewal) {
        this.giftAllowRenewal = giftAllowRenewal;
    }

    public Integer getGroupAllowRenewal() {
        return getAllowRenewal(groupAllowRenewal, groupEndTime);
    }

    public void setGroupAllowRenewal(Integer groupAllowRenewal) {
        this.groupAllowRenewal = groupAllowRenewal;
    }

    public Integer getTrysAllowRenewal() {
        return getAllowRenewal(trysAllowRenewal, trysEndTime);
    }

    public void setTrysAllowRenewal(Integer trysAllowRenewal) {
        this.trysAllowRenewal = trysAllowRenewal;
    }

    public Integer getPointsGoodsAllowRenewal() {
        return getAllowRenewal(pointsGoodsAllowRenewal, pointsGoodsEndTime);
    }

    public void setPointsGoodsAllowRenewal(Integer pointsGoodsAllowRenewal) {
        this.pointsGoodsAllowRenewal = pointsGoodsAllowRenewal;
    }

    public Integer getThemeAllowRenewal() {
        return getAllowRenewal(themeAllowRenewal, themeEndTime);
    }

    public void setThemeAllowRenewal(Integer themeAllowRenewal) {
        this.themeAllowRenewal = themeAllowRenewal;
    }

    public Integer getSeckillAllowRenewal() {
        return getAllowRenewal(seckillAllowRenewal, seckillEndTime);
    }

    public void setSeckillAllowRenewal(Integer seckillAllowRenewal) {
        this.seckillAllowRenewal = seckillAllowRenewal;
    }

    public Integer getBargainAllowRenewal() {
        return getAllowRenewal(bargainAllowRenewal, bargainEndTime);
    }

    public void setBargainAllowRenewal(Integer bargainAllowRenewal) {
        this.bargainAllowRenewal = bargainAllowRenewal;
    }

    public Integer getVoucherUsable() {
        return getUsable(voucherUsable, voucherEndTime);
    }

    public void setVoucherUsable(Integer voucherUsable) {
        this.voucherUsable = voucherUsable;
    }

    public Integer getDiscountUsable() {
        return getUsable(discountUsable, discountEndTime);
    }

    public void setDiscountUsable(Integer discountUsable) {
        this.discountUsable = discountUsable;
    }

    public Integer getComboUsable() {
        return getUsable(comboUsable, comboEndTime);
    }

    public void setComboUsable(Integer comboUsable) {
        this.comboUsable = comboUsable;
    }

    public Integer getConformUsable() {
        return getUsable(conformUsable, conformEndTime);
    }

    public void setConformUsable(Integer conformUsable) {
        this.conformUsable = conformUsable;
    }

    public Integer getBookUsable() {
        return getUsable(bookUsable, bookEndTime);
    }

    public void setBookUsable(Integer bookUsable) {
        this.bookUsable = bookUsable;
    }

    public Integer getBundlingUsable() {
        return getUsable(bundlingUsable, bundlingEndTime);
    }

    public void setBundlingUsable(Integer bundlingUsable) {
        this.bundlingUsable = bundlingUsable;
    }

    public Integer getGiftUsable() {
        return getUsable(giftUsable, giftEndTime);
    }

    public void setGiftUsable(Integer giftUsable) {
        this.giftUsable = giftUsable;
    }

    public Integer getGroupUsable() {
        return getUsable(groupUsable, groupEndTime);
    }

    public void setGroupUsable(Integer groupUsable) {
        this.groupUsable = groupUsable;
    }

    public Integer getTrysUsable() {
        return getUsable(trysUsable, trysEndTime);
    }

    public void setTrysUsable(Integer trysUsable) {
        this.trysUsable = trysUsable;
    }

    public Integer getPointsGoodsUsable() {
        return getUsable(pointsGoodsUsable, pointsGoodsEndTime);
    }

    public void setPointsGoodsUsable(Integer pointsGoodsUsable) {
        this.pointsGoodsUsable = pointsGoodsUsable;
    }

    public Integer getThemeUsable() {
        return getUsable(themeUsable, themeEndTime);
    }

    public void setThemeUsable(Integer themeUsable) {
        this.themeUsable = themeUsable;
    }

    public Integer getSeckillUsable() {
        return getUsable(seckillUsable, seckillEndTime);
    }

    public void setSeckillUsable(Integer seckillUsable) {
        this.seckillUsable = seckillUsable;
    }

    public Integer getBargainUsable() {
        return getUsable(bargainUsable, bargainEndTime);
    }

    public void setBargainUsable(Integer bargainUsable) {
        this.bargainUsable = bargainUsable;
    }

    private int getAllowRenewal(Integer allowRenewal, String endTime) {
       return 0;
    }

    private int getUsable(Integer usable, String endTime) {
       return 1;
    }

    @Override
    public String toString() {
        return "PromotionQuota{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", voucherEndTime=" + voucherEndTime +
                ", discountEndTime=" + discountEndTime +
                ", comboEndTime=" + comboEndTime +
                ", conformEndTime=" + conformEndTime +
                ", bookEndTime=" + bookEndTime +
                ", bundlingEndTime=" + bundlingEndTime +
                ", giftEndTime=" + giftEndTime +
                ", groupEndTime=" + groupEndTime +
                ", trysEndTime=" + trysEndTime +
                ", pointsGoodsEndTime=" + pointsGoodsEndTime +
                ", themeEndTime=" + themeEndTime +
                ", seckillEndTime=" + seckillEndTime +
                ", bargainEndTime=" + bargainEndTime +
                ", voucherAllowRenewal=" + voucherAllowRenewal +
                ", discountAllowRenewal=" + discountAllowRenewal +
                ", comboAllowRenewal=" + comboAllowRenewal +
                ", conformAllowRenewal=" + conformAllowRenewal +
                ", bookAllowRenewal=" + bookAllowRenewal +
                ", bundlingAllowRenewal=" + bundlingAllowRenewal +
                ", giftAllowRenewal=" + giftAllowRenewal +
                ", groupAllowRenewal=" + groupAllowRenewal +
                ", trysAllowRenewal=" + trysAllowRenewal +
                ", pointsGoodsAllowRenewal=" + pointsGoodsAllowRenewal +
                ", themeAllowRenewal=" + themeAllowRenewal +
                ", seckillAllowRenewal=" + seckillAllowRenewal +
                ", bargainAllowRenewal=" + bargainAllowRenewal +
                ", voucherUsable=" + voucherUsable +
                ", discountUsable=" + discountUsable +
                ", comboUsable=" + comboUsable +
                ", conformUsable=" + conformUsable +
                ", bookUsable=" + bookUsable +
                ", bundlingUsable=" + bundlingUsable +
                ", giftUsable=" + giftUsable +
                ", groupUsable=" + groupUsable +
                ", trysUsable=" + trysUsable +
                ", pointsGoodsUsable=" + pointsGoodsUsable +
                ", themeUsable=" + themeUsable +
                ", seckillUsable=" + seckillUsable +
                ", bargainUsable=" + bargainUsable +
                '}';
    }
}
