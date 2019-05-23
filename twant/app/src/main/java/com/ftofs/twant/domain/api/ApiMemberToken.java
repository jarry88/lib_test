package com.ftofs.twant.domain.api;

public class ApiMemberToken {
    /**
     * 令牌编号
     */
    private int tokenId=0;

    /**
     * 会员编号
     */
    private int memberId=0;

    /**
     * 会员名称
     */
    private String memberName="";

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
     * 可选值 wap android ios weixin
     */
    private String clientType;

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
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

