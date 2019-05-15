package com.ftofs.twant.domain.comment;

import java.math.BigInteger;

public class WantCommentImage {
    /**
     * 主键id
     */
    private BigInteger imageId;

    /**
     * 贴文id
     */
    private BigInteger commentId;

    /**
     * 图片地址
     */
    private String imageUrl;

    public BigInteger getImageId() {
        return imageId;
    }

    public void setImageId(BigInteger imageId) {
        this.imageId = imageId;
    }

    public BigInteger getCommentId() {
        return commentId;
    }

    public void setCommentId(BigInteger commentId) {
        this.commentId = commentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "WantCommentImage{" +
                "imageId=" + imageId +
                ", commentId=" + commentId +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
