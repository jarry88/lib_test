package com.ftofs.twant.domain.advertorial;

import java.io.Serializable;

public class AdvertorialArticleLike implements Serializable {
    /**
     * id
     */
    private int likeId;

    /**
     * 会员id
     */
    private int memberId;

    /**
     * 推文分类排序
     */
    private int articleId;

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
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
        return "AdvertorialArticleLike{" +
                "likeId=" + likeId +
                ", memberId=" + memberId +
                ", articleId=" + articleId +
                '}';
    }
}
