package com.ftofs.twant.domain.wantpost;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

public class WantPostFavor implements Serializable {
    /**
     * 貼文id
     */
    private BigInteger postId;

    /**
     * 會員名稱
     */
    private String memberName;

    /**
     * 關注時間
     */
    private Timestamp createTime;

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "WantPostFavor{" +
                "postId=" + postId +
                ", memberName='" + memberName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
