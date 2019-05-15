package com.ftofs.twant.vo.promotion.platform.coupon;

import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.promotion.platform.coupon.CouponUseGoods;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 平台券可使用的商品
 *
 * @author zxy
 * Created 2017/9/1 10:46
 */
public class CouponUseGoodsVo {
    /**
     * 自增ID
     */
    private int id = 0;
    /**
     * 平台券活动ID
     */
    private int activityId = 0;
    /**
     * 商品分类编号
     */
    private int categoryId = 0;
    /**
     * 一级分类
     */
    private int categoryId1 = 0;
    /**
     * 二级分类
     */
    private int categoryId2 = 0;
    /**
     * 三级分类
     */
    private int categoryId3 = 0;
    /**
     * 一级分类名称
     */
    private String categoryName1 = "";
    /**
     * 二级分类名称
     */
    private String categoryName2 = "";
    /**
     * 三级分类名称
     */
    private String categoryName3 = "";
    /**
     * 商品SPU编号
     */
    private int commonId = 0;
    /**
     * 商品SKU编号
     */
    private int goodsId = 0;
    /**
     * 商品Spu详情
     */
    private GoodsCommon goodsCommon = null;

    public CouponUseGoodsVo(CouponUseGoods couponUseGoods, GoodsCommon goodsCommon) {
        this.id = couponUseGoods.getId();
        this.activityId = couponUseGoods.getActivityId();
        this.categoryId = couponUseGoods.getCategoryId();
        this.categoryId1 = couponUseGoods.getCategoryId1();
        this.categoryId2 = couponUseGoods.getCategoryId2();
        this.categoryId3 = couponUseGoods.getCategoryId3();
        this.categoryName1 = couponUseGoods.getCategoryName1();
        this.categoryName2 = couponUseGoods.getCategoryName2();
        this.categoryName3 = couponUseGoods.getCategoryName3();
        this.commonId = couponUseGoods.getCommonId();
        this.goodsCommon = goodsCommon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(int categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public int getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(int categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public int getCategoryId3() {
        return categoryId3;
    }

    public void setCategoryId3(int categoryId3) {
        this.categoryId3 = categoryId3;
    }

    public String getCategoryName1() {
        return categoryName1;
    }

    public void setCategoryName1(String categoryName1) {
        this.categoryName1 = categoryName1;
    }

    public String getCategoryName2() {
        return categoryName2;
    }

    public void setCategoryName2(String categoryName2) {
        this.categoryName2 = categoryName2;
    }

    public String getCategoryName3() {
        return categoryName3;
    }

    public void setCategoryName3(String categoryName3) {
        this.categoryName3 = categoryName3;
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

    public GoodsCommon getGoodsCommon() {
        return goodsCommon;
    }

    public void setGoodsCommon(GoodsCommon goodsCommon) {
        this.goodsCommon = goodsCommon;
    }

    @Override
    public String toString() {
        return "CouponUseGoodsVo{" +
                "id=" + id +
                ", activityId=" + activityId +
                ", categoryId=" + categoryId +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", categoryId3=" + categoryId3 +
                ", categoryName1='" + categoryName1 + '\'' +
                ", categoryName2='" + categoryName2 + '\'' +
                ", categoryName3='" + categoryName3 + '\'' +
                ", commonId=" + commonId +
                ", goodsId=" + goodsId +
                ", goodsCommon=" + goodsCommon +
                '}';
    }
}
