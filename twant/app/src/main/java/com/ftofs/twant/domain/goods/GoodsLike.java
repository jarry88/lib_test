package com.ftofs.twant.domain.goods;

import java.io.Serializable;


public class GoodsLike implements Serializable {
    /**
     * 主鍵ID
     */
    private int goodsLikeId;

    /**
     * 城友ID
     */
    private int memberId;

    /**
     * 產品SPU編號
     */
    private int commonId;

    /**
     * 讚想狀態 1是0否
     */
    private int state;

    /**
     * 創建時間
     */
    private String createTime;

    /**
     * 修改時間
     */
    private String modifyTime;

    public GoodsLike(){
    }

    public int getGoodsLikeId() {
        return goodsLikeId;
    }

    public void setGoodsLikeId(int goodsLikeId) {
        this.goodsLikeId = goodsLikeId;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        return "GoodsLike{" +
                "goodsLikeId=" + goodsLikeId +
                ", memberId=" + memberId +
                ", commonId=" + commonId +
                ", state=" + state +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                '}';
    }
}
