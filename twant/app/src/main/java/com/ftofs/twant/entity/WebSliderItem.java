package com.ftofs.twant.entity;

public class WebSliderItem {
    public WebSliderItem(String image, String linkType, String linkValue, String goodsIds, String goodsCommons) {
        this.image = image;
        this.linkType = linkType;
        this.linkValue = linkValue;
        this.goodsIds = goodsIds;
        this.goodsCommons = goodsCommons;
    }

    public String image;
    public String linkType;
    public String linkValue;
    public String goodsIds;
    public String goodsCommons;
}
