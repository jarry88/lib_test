package com.ftofs.twant.domain.wantpost;

import java.io.Serializable;
import java.math.BigInteger;

public class WantPostLike implements Serializable {
    /**
     * 想要帖id
     */
    private BigInteger postId;

    /**
     * 城友名稱
     */
    private String memberName;

    /**
     * 關注時間
     */
    private String createTime;

    public BigInteger getPostId() {
        return postId;
    }

    public void setPostId( BigInteger postId) {
        this.postId = postId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName( String memberName) {
        this.memberName = memberName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "WantPostLike{" +
                "postId=" + postId +
                ", memberName='" + memberName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
