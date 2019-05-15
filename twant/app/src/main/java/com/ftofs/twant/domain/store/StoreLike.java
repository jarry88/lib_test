package com.ftofs.twant.domain.store;

import java.io.Serializable;
import java.sql.Timestamp;

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
    private Timestamp createTime;

    public  StoreLike(){}

    public StoreLike(int id, int memberId, int storeId, Timestamp createTime) {
        this.id = id;
        this.memberId = memberId;
        this.storeId = storeId;
        this.createTime = createTime;
    }

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
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
