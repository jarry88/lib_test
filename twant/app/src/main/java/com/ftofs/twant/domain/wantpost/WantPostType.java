package com.ftofs.twant.domain.wantpost;

public class WantPostType {
    /**
     * 想要帖id
     */
    private int postTypeId;

    /**
     * 贴文类型名称
     */
    private String postTypeName;

    public int getPostTypeId() {
        return postTypeId;
    }

    public void setPostTypeId(int postTypeId) {
        this.postTypeId = postTypeId;
    }

    public String getPostTypeName() {
        return postTypeName;
    }

    public void setPostTypeName(String postTypeName) {
        this.postTypeName = postTypeName;
    }

    @Override
    public String toString() {
        return "WantPostType{" +
                "postTypeId=" + postTypeId +
                ", postTypeName='" + postTypeName + '\'' +
                '}';
    }
}
