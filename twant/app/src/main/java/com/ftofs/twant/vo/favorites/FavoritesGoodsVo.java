package com.ftofs.twant.vo.favorites;

import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.member.FavoritesGoods;
import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.vo.store.StoreVo;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 商品關注
 * 
 * @author zxy
 * Created 2017/4/13 11:00
 */
public class FavoritesGoodsVo {
    /**
     * 自增编码
     */
    private int favoritesId = 0;
    /**
     * 会员编码
     */
    private int memberId = 0;
    /**
     * 商品SPU编码
     */
    private int commonId = 0;
    /**
     * 店铺编码
     */
    private int storeId = 0;
    /**
     * 關注时间
     */

    private Timestamp addTime = new Timestamp(0);
    /**
     * 商品關注时价格
     */
    private BigDecimal favGoodsPrice = new BigDecimal(0);
    /**
     * 商品名称
     */
    private String goodsName = "";
    /**
     * 商品SPU
     */
    private GoodsCommon goodsCommon = null;
    /**
     * 是否有优惠券活动 0表示没有 1表示有
     */
    private int haveCouponActivity = 0;

    /**
     * bycj -- 關注置顶
     */
    private int setTop = 0 ;

    /**
     * 店铺信息
     */
    private StoreVo storeVo = null;

    /**
     * 1是0否點贊
     */
    private int isLike;

    public FavoritesGoodsVo(FavoritesGoods fg, GoodsCommon gc) {
        this.favoritesId = fg.getFavoritesId();
        this.memberId = fg.getMemberId();
        this.commonId = fg.getCommonId();
        this.storeId = fg.getStoreId();
        this.addTime = fg.getAddTime();
        this.favGoodsPrice = fg.getFavGoodsPrice();
        this.goodsName = gc.getGoodsName();
        this.goodsCommon = gc;
        this.setTop = fg.getSetTop() ;
    }

    public FavoritesGoodsVo(FavoritesGoods fg, GoodsCommon gc, int haveCouponActivity) {
        this.favoritesId = fg.getFavoritesId();
        this.memberId = fg.getMemberId();
        this.commonId = fg.getCommonId();
        this.storeId = fg.getStoreId();
        this.addTime = fg.getAddTime();
        this.favGoodsPrice = fg.getFavGoodsPrice();
        this.goodsName = gc.getGoodsName();
        this.goodsCommon = gc;
        this.haveCouponActivity = haveCouponActivity;
        this.setTop = fg.getSetTop();
    }

    public FavoritesGoodsVo(FavoritesGoods fg, GoodsCommon gc, Store store){
        this.favoritesId = fg.getFavoritesId();
        this.memberId = fg.getMemberId();
        this.commonId = fg.getCommonId();
        this.storeId = fg.getStoreId();
        this.addTime = fg.getAddTime();
        this.favGoodsPrice = fg.getFavGoodsPrice();
        this.goodsName = gc.getGoodsName();
        this.setTop = fg.getSetTop() ;
        this.goodsCommon = gc;
        this.storeVo = new StoreVo(store);
    }

    public int getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(int favoritesId) {
        this.favoritesId = favoritesId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
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

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public BigDecimal getFavGoodsPrice() {
        return favGoodsPrice;
    }

    public void setFavGoodsPrice(BigDecimal favGoodsPrice) {
        this.favGoodsPrice = favGoodsPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public GoodsCommon getGoodsCommon() {
        return goodsCommon;
    }

    public void setGoodsCommon(GoodsCommon goodsCommon) {
        this.goodsCommon = goodsCommon;
    }

    public int getHaveCouponActivity() {
        return haveCouponActivity;
    }

    public void setHaveCouponActivity(int haveCouponActivity) {
        this.haveCouponActivity = haveCouponActivity;
    }

    public int getSetTop() {
        return setTop;
    }

    public void setSetTop(int setTop) {
        this.setTop = setTop;
    }

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}
