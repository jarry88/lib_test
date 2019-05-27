package com.ftofs.twant.domain.wantpost;

import java.math.BigInteger;

public class WantPostShare {
    /**
     * 貼文id
     */
    private BigInteger postId;

    /**
     * 會員名稱
     */
    private String memberName;

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

    @Override
    public String toString() {
        return "WantPostShare{" +
                "postId=" + postId +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
