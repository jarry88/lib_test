package com.ftofs.twant.entity.cart;

import android.widget.ImageView;

public class BaseStatus {
    public static final boolean STATUS_CHECKED = true;
    public static final boolean STATUS_UNCHECKED = false;

    protected ImageView radio;
    protected boolean checked;
    protected int count;  // 數量
    protected float price;  // 價錢

    public ImageView getRadio() {
        return radio;
    }

    public void setRadio(ImageView radio) {
        this.radio = radio;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
