package com.ftofs.twant.entity;

public class SpecButtonData {
    public SpecButtonData(int position, int specId, int specValueId, String imageSrc, boolean isSelected) {
        this.position = position;
        this.specId = specId;
        this.specValueId = specValueId;
        this.isSelected = isSelected;
        this.imageSrc = imageSrc;
    }

    public int position;
    public int specId;
    public int specValueId;
    public String imageSrc;
    public boolean isSelected; // 是否選中
}
