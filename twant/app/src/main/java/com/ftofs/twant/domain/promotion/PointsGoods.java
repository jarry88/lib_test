package com.ftofs.twant.domain.promotion;

import java.io.Serializable;

public class PointsGoods implements Serializable {
    /**
     * 积分商品编号
     */
    private int pointsGoodsId;

    /**
     * 所需积分
     */
    private int expendPoints = 0;

    /**
     * 领取店铺券限制的会员等级
     */
    private int limitMemberGradeLevel = 0;

    /**
     * 领取店铺券限制的会员等级名称
     */
    private String limitMemberGradeName = "";

    /**
     * 商品SPU编号
     */
    private int commonId;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 积分商品创建时间
     */
    private String createTime;

    /**
     * 被点击数量
     */
    private int goodsClick = 0;

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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getGoodsClick() {
        return goodsClick;
    }

    public void setGoodsClick(int goodsClick) {
        this.goodsClick = goodsClick;
    }

    @Override
    public String toString() {
        return "PointsGoods{" +
                "pointsGoodsId=" + pointsGoodsId +
                ", expendPoints=" + expendPoints +
                ", limitMemberGradeLevel=" + limitMemberGradeLevel +
                ", limitMemberGradeName='" + limitMemberGradeName + '\'' +
                ", commonId=" + commonId +
                ", storeId=" + storeId +
                ", createTime=" + createTime +
                ", goodsClick=" + goodsClick +
                '}';
    }
}
