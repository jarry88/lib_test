package com.ftofs.twant.domain.member;

import java.io.Serializable;


public class FavoritesStore implements Serializable {
    /**
     * 自增编码
     */
    private int favoritesId;

    /**
     * 会员编码
     */
    private int memberId = 0;

    /**
     * 店铺编码
     */
    private int storeId = 0;

    /**
     * 關注时间
     */
    private String addTime;

    /**
     * bycj -- 關注置顶
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

    public int getSetTop() {
        return setTop;
    }

    public void setSetTop(int setTop) {
        this.setTop = setTop;
    }

    @Override
    public String toString() {
        return "FavoritesStore{" +
                "favoritesId=" + favoritesId +
                ", memberId=" + memberId +
                ", storeId=" + storeId +
                ", addTime=" + addTime +
                ", setTop=" + setTop +
                '}';
    }
}
