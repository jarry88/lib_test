package com.ftofs.twant.domain.suggest;

public class MemberSuggestImage {
    /**
     * 反饋圖片id
     */
    private int imageId;

    /**
     * 反饋建議id
     */
    private int suggestId;

    /**
     * 圖片地址
     */
    private String imageUrl;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSuggestId() {
        return suggestId;
    }

    public void setSuggestId(int suggestId) {
        this.suggestId = suggestId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "MemberSuggestImage{" +
                "imageId=" + imageId +
                ", suggestId=" + suggestId +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
