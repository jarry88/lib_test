package com.ftofs.twant.domain.advertorial;

import java.io.Serializable;

public class AdvertorialFollow implements Serializable {
    /**
     * id
     */
    private int followId;

    /**
     * 会员id
     */
    private int memberId;

    /**
     * 想要帖id
     */
    private int articleId;

    public int getFollowId() {
        return followId;
    }

    public void setFollowId(int followId) {
        this.followId = followId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "AdvertorialFollow{" +
                "followId=" + followId +
                ", memberId=" + memberId +
                ", articleId=" + articleId +
                '}';
    }
}
