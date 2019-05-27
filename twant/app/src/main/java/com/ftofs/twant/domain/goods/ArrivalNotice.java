package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class ArrivalNotice implements Serializable {
    /**
     * 到货通知编号
     */
    private int arrivalId;

    /**
     * 商品SKU编号
     */
    private int goodsId;

    /**
     * 商品SPU编号
     */
    private int commonId;

    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱号码
     */
    private String email;

    /**
     * 创建时间
     */
    private String createTime;

    public int getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(int arrivalId) {
        this.arrivalId = arrivalId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ArrivalNotice{" +
                "arrivalId=" + arrivalId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", memberId=" + memberId +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
