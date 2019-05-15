package com.ftofs.twant.domain.member;

import java.io.Serializable;
import java.sql.Timestamp;

public class MemberToken implements Serializable {
    /**
     * 令牌自增编码
     */
    private int tokenId;

    /**
     * 会员编码
     */
    private int memberId = 0;

    /**
     * 登录令牌
     */
    private String token = "";

    /**
     * 添加时间
     */
    private Timestamp addTime;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "MemberToken{" +
                "tokenId=" + tokenId +
                ", memberId=" + memberId +
                ", token='" + token + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}
