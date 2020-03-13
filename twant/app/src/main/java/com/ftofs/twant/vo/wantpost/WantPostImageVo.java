package com.ftofs.twant.vo.wantpost;

import java.math.BigInteger;

/**
 * @Description: 想要帖圖片視圖對象
 * @Auther: yangjian
 * @Date: 2019/3/21 16:00
 */
public class WantPostImageVo{

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
}
