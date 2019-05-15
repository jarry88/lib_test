package com.ftofs.twant.domain.chain;

import java.io.Serializable;

public class ChainCoupon implements Serializable,Cloneable {
    /**
     * 自增编码
     */
    private int id;

    /**
     * 门店ID
     */
    private int chainId = 0;

    /**
     * 门店名称
     */
    private String chainName = "";

    /**
     * 平台券ID
     */
    private int couponId;

    /**
     * 店员ID
     */
    private int clerkId = 0;

    /**
     * 店员
     */
    private String clerkName = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
    }

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ChainCoupon{" +
                "id=" + id +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", couponId=" + couponId +
                ", clerkId=" + clerkId +
                ", clerkName='" + clerkName + '\'' +
                '}';
    }
}
