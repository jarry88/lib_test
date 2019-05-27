package com.ftofs.twant.vo.favorites;

import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.vo.store.StoreServiceStaffVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 店铺關注
 * 
 * @author zxy
 * Created 2017/4/13 11:00
 */
public class FavoritesStoreVo {
    /**
     * 自增编码
     */
    private int favoritesId = 0;
    /**
     * 会员编码
     */
    private int memberId = 0;

    /**
     * 會員名稱
     */
    private String memberName;
    /**
     * 會員暱稱
     */
    private String nickName;
    /**
     * 會員頭像
     */
    private String avatar;
    /**
     * 店铺编码
     */
    private int storeId = 0;
    /**
     * 關注时间
     */

    private String addTime = "";
    /**
     * 店铺详情
     */
    private Store store = null;
    /**
     * 店铺商品总数
     */
    private long goodsCommonCount = 0L;
    /**
     * bycj -- 店铺新品
     */
    private List<GoodsCommon> newGoodsList  = new ArrayList<>() ;

    /**
     * bycj -- 商品上新数量
     * 根数關注时间判断
     */
    private long newGoodsCount = 0 ;

    /**
     * bycj -- 置顶
     */
    private int setTop = 0 ;
    /**
     * bycj -- 店铺评分
     */
    private int avgStoreEvalRate = 0 ;

    /**
     * bycj -- 店铺關注数量
     */
    private int storeCollect = 0 ;
    /**
     * 是否自营店铺
     * 0-否 1-是
     */
    private int isOwnShop =  0 ;

    /**
     * 店鋪門店位置 高德經度
     */
    private double lng = 0;

    /**
     * 店鋪門店位置 高德緯度
     */
    private double lat = 0;

    /**
     * 當前位置與店鋪閒的距離(格式化為字符串顯示，單位m)
     */
    private String distance = "0";

    /**
     * 客服列表
     */
    private List<StoreServiceStaffVo> storeServiceStaffVoList;

    /**
     * 头像路径
     */
    private String avatarUrl = "";

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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public long getGoodsCommonCount() {
        return goodsCommonCount;
    }

    public void setGoodsCommonCount(long goodsCommonCount) {
        this.goodsCommonCount = goodsCommonCount;
    }

    public List<GoodsCommon> getNewGoodsList() {
        return newGoodsList;
    }

    public void setNewGoodsList(List<GoodsCommon> newGoodsList) {
        this.newGoodsList = newGoodsList;
    }

    public long getNewGoodsCount() {
        return newGoodsCount;
    }

    public void setNewGoodsCount(long newGoodsCount) {
        this.newGoodsCount = newGoodsCount;
    }

    public int getSetTop() {
        return setTop;
    }

    public void setSetTop(int setTop) {
        this.setTop = setTop;
    }

    public int getAvgStoreEvalRate() {
        return avgStoreEvalRate;
    }

    public void setAvgStoreEvalRate(int avgStoreEvalRate) {
        this.avgStoreEvalRate = avgStoreEvalRate;
    }

    public int getStoreCollect() {
        return storeCollect;
    }

    public void setStoreCollect(int storeCollect) {
        this.storeCollect = storeCollect;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<StoreServiceStaffVo> getStoreServiceStaffVoList() {
        return storeServiceStaffVoList;
    }

    public void setStoreServiceStaffVoList(List<StoreServiceStaffVo> storeServiceStaffVoList) {
        this.storeServiceStaffVoList = storeServiceStaffVoList;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "FavoritesStoreVo{" +
                "favoritesId=" + favoritesId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", storeId=" + storeId +
                ", addTime=" + addTime +
                ", store=" + store +
                ", goodsCommonCount=" + goodsCommonCount +
                ", newGoodsList=" + newGoodsList +
                ", newGoodsCount=" + newGoodsCount +
                ", setTop=" + setTop +
                ", avgStoreEvalRate=" + avgStoreEvalRate +
                ", storeCollect=" + storeCollect +
                ", isOwnShop=" + isOwnShop +
                '}';
    }

}
