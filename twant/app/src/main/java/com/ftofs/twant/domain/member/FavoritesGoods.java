package com.ftofs.twant.domain.member;

import java.io.Serializable;
import java.math.BigDecimal;

public class FavoritesGoods implements Serializable {
    /**
     * 自增编码
     */
    private int favoritesId;

    /**
     * 会员编码
     */
    private int memberId = 0;

    /**
     * 產品SPU编码
     */
    private int commonId = 0;

    /**
     * 店铺编码
     */
    private int storeId = 0;

    /**
     * 產品分类ID
     */
    private int categoryId = 0;

    /**
     * 關注时间
     */
    private String addTime;

    /**
     * 產品關注时价格
     */
    private BigDecimal favGoodsPrice = new BigDecimal(0);

    /**
     * bycj -- 置顶序号
     */
    private int setTop = 0 ;

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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public BigDecimal getFavGoodsPrice() {
        return favGoodsPrice;
    }

    public void setFavGoodsPrice(BigDecimal favGoodsPrice) {
        this.favGoodsPrice = favGoodsPrice;
    }

    public int getSetTop() {
        return setTop;
    }

    public void setSetTop(int setTop) {
        this.setTop = setTop;
    }

    @Override
    public String toString() {
        return "FavoritesGoods{" +
                "favoritesId=" + favoritesId +
                ", memberId=" + memberId +
                ", commonId=" + commonId +
                ", storeId=" + storeId +
                ", categoryId=" + categoryId +
                ", addTime=" + addTime +
                ", favGoodsPrice=" + favGoodsPrice +
                ", setTop=" + setTop +
                '}';
    }
}
