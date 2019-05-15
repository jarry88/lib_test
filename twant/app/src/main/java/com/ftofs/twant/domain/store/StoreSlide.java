package com.ftofs.twant.domain.store;

public class StoreSlide {
    /**
     * 店铺编号
     */
    private int slideId;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 幻灯图片
     */
    private String image = "";

    /**
     * 幻灯URL
     */
    private String url = "";

    /**
     * 幻灯片图片URL
     */
    private String imageUrl;

    public int getSlideId() {
        return slideId;
    }

    public void setSlideId(int slideId) {
        this.slideId = slideId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "StoreSlide{" +
                "slideId=" + slideId +
                ", storeId=" + storeId +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

