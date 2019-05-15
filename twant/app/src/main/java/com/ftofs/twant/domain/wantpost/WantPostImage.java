package com.ftofs.twant.domain.wantpost;

import java.math.BigInteger;

public class WantPostImage {
    /**
     * 主键id
     */
    private BigInteger imageId;

    /**
     * 贴文id
     */
    private BigInteger postId;

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

    public BigInteger getPostId() {
        return postId;
    }

    public void setPostId(BigInteger postId) {
        this.postId = postId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "WantPostImage{" +
                "imageId=" + imageId +
                ", postId=" + postId +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
