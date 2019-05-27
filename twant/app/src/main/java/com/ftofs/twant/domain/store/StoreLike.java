package com.ftofs.twant.domain.store;

import java.io.Serializable;

public class StoreLike implements Serializable {
    private int id;

    /**
     * 會員id
     */
    private int memberId;

    /**
     * 店鋪id
     */
    private int storeId;

    /**
     * 點想時間
     */
    private String createTime;

    public  StoreLike(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "StoreLike{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", storeId=" + storeId +
                ", createTime=" + createTime +
                '}';
    }
}
