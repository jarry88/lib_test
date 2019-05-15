package com.ftofs.twant.domain.show;

import java.io.Serializable;

public class ShowOrdersZan implements Serializable {
    /**
     * 晒baoid
     * 主键、自增
     */
    private Integer showOrdersReadId;

    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 晒宝id
     */
    private int showOrdersId;

    public Integer getShowOrdersReadId() {
        return showOrdersReadId;
    }

    public void setShowOrdersReadId(Integer showOrdersReadId) {
        this.showOrdersReadId = showOrdersReadId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getShowOrdersId() {
        return showOrdersId;
    }

    public void setShowOrdersId(int showOrdersId) {
        this.showOrdersId = showOrdersId;
    }

    @Override
    public String toString() {
        return "ShowOrdersRead{" +
                "showOrdersReadId=" + showOrdersReadId +
                ", memberId=" + memberId +
                ", showOrdersId=" + showOrdersId +
                '}';
    }
}
