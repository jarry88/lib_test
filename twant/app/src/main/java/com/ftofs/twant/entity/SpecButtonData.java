package com.ftofs.twant.entity;

public class SpecButtonData {
    public SpecButtonData(int position, int specId, int specValueId, boolean isSelected) {
        this.position = position;
        this.specId = specId;
        this.specValueId = specValueId;
        this.isSelected = isSelected;
    }

    public int position;
    public int specId;
    public int specValueId;
    public boolean isSelected; // 是否選中
}
