package com.ftofs.twant.domain.api;

import java.sql.Timestamp;

public class ApiSellerToken {
    /**
     * 令牌编号
     */
    private int tokenId=0;

    /**
     * 卖家编号
     */
    private int sellerId=0;

    /**
     * 卖家名称
     */
    private String sellerName="";

    /**
     * token
     */
    private String token="";

    /**
     * 创建时间
     */
    private Timestamp createTime=new Timestamp(0);

    /**
     * 最后登录时间
     */
    private Timestamp lastLoginTime;

    /**
     * 客户端类型
     * 可选值 wap android ios weixin webIm
     */
    private String clientType;

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}

