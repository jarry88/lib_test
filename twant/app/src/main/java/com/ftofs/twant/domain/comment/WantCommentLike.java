package com.ftofs.twant.domain.comment;

import java.math.BigInteger;

public class WantCommentLike {
    /**
     * 評論id
     */
    private BigInteger commentId;

    /**
     * 會員名稱
     */
    private String memberName;

    public BigInteger getCommentId() {
        return commentId;
    }

    public void setCommentId(BigInteger commentId) {
        this.commentId = commentId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public String toString() {
        return "WantCommentLike{" +
                "commentId=" + commentId +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}
