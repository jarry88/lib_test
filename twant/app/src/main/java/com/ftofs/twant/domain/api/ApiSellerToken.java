package com.ftofs.twant.domain.api;

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
    private String createTime="";
    /**
     * 最后登录时间
     */
    private String lastLoginTime;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
}

