package com.ftofs.twant.vo.promotion;

import java.math.BigDecimal;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 积分產品
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:20
 */
public class PointsGoodsVo {
    /**
     * 积分產品编号
     */
    private int pointsGoodsId;
    /**
     * 所需积分
     */
    private int expendPoints = 0;
    /**
     * 领取积分產品限制的会员等级
     */
    private int limitMemberGradeLevel = 0;
    /**
     * 领取积分產品限制的会员等级名称
     */
    private String limitMemberGradeName = "";
    /**
     * 產品SPU
     */
    private int commonId;
    /**
     * 產品名称
     */
    private String goodsName;
    /**
     * 图片路径
     */
    private String imageSrc="";
    /**
     * 產品最低价
     */
    private BigDecimal webPriceMin;
    /**
     * 產品最低价
     */
    private BigDecimal appPriceMin;
    /**
     * 產品最低价
     */
    private BigDecimal wechatPriceMin;

    public int getPointsGoodsId() {
        return pointsGoodsId;
    }

    public void setPointsGoodsId(int pointsGoodsId) {
        this.pointsGoodsId = pointsGoodsId;
    }

    public int getExpendPoints() {
        return expendPoints;
    }

    public void setExpendPoints(int expendPoints) {
        this.expendPoints = expendPoints;
    }

    public int getLimitMemberGradeLevel() {
        return limitMemberGradeLevel;
    }

    public void setLimitMemberGradeLevel(int limitMemberGradeLevel) {
        this.limitMemberGradeLevel = limitMemberGradeLevel;
    }

    public String getLimitMemberGradeName() {
        return limitMemberGradeName;
    }

    public void setLimitMemberGradeName(String limitMemberGradeName) {
        this.limitMemberGradeName = limitMemberGradeName;
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

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public BigDecimal getWebPriceMin() {
        return webPriceMin;
    }

    public void setWebPriceMin(BigDecimal webPriceMin) {
        this.webPriceMin = webPriceMin;
    }

    public BigDecimal getAppPriceMin() {
        return appPriceMin;
    }

    public void setAppPriceMin(BigDecimal appPriceMin) {
        this.appPriceMin = appPriceMin;
    }

    public BigDecimal getWechatPriceMin() {
        return wechatPriceMin;
    }

    public void setWechatPriceMin(BigDecimal wechatPriceMin) {
        this.wechatPriceMin = wechatPriceMin;
    }

    @Override
    public String toString() {
        return "PointsGoodsVo{" +
                "pointsGoodsId=" + pointsGoodsId +
                ", expendPoints=" + expendPoints +
                ", limitMemberGradeLevel=" + limitMemberGradeLevel +
                ", limitMemberGradeName='" + limitMemberGradeName + '\'' +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", webPriceMin=" + webPriceMin +
                ", appPriceMin=" + appPriceMin +
                ", wechatPriceMin=" + wechatPriceMin +
                '}';
    }
}
