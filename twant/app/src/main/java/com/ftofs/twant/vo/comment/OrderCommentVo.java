package com.ftofs.twant.vo.comment;

import java.util.List;

/**
 * @Description: 訂單說說
 * @Auther: yangjian
 * @Date: 2019/4/19 16:36
 */
public class OrderCommentVo {
    /**
     * 訂單id
     */
    private int ordersId;
    /**
     * 訂單類型
     */
    private int ordersType;
    /**
     * 產品說說列表
     */
    private List<WantCommentVo> wantCommentVoList;

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
    }

    public List<WantCommentVo> getWantCommentVoList() {
        return wantCommentVoList;
    }

    public void setWantCommentVoList(List<WantCommentVo> wantCommentVoList) {
        this.wantCommentVoList = wantCommentVoList;
    }

    @Override
    public String toString() {
        return "OrderCommentVo{" +
                "ordersId=" + ordersId +
                ", ordersType=" + ordersType +
                ", wantCommentVoList=" + wantCommentVoList +
                '}';
    }
}
