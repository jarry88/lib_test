package com.ftofs.twant.entity;

public class ChangeColorResult {
    public String color;
    public long id;

    public ChangeColorResult(String color, long id) {
        this.color = color;
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChangeColorResult{" +
                "color='" + color + '\'' +
                ", id=" + id +
                '}';
    }
}
