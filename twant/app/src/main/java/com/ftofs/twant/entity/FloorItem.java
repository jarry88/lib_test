package com.ftofs.twant.entity;

public class FloorItem {
    public String imageName;
    public int imageWidth;
    public int imageHeight;
    public String linkType;
    public String linkValue;

    public FloorItem() {
    }

    public FloorItem(String imageName, int imageWidth, int imageHeight, String linkType, String linkValue) {
        this.imageName = imageName;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.linkType = linkType;
        this.linkValue = linkValue;
    }
}

