package com.ftofs.twant.entity;

/**
 * 彈出框的數據項
 * @author zwm
 */
public class ListPopupItem {
    public ListPopupItem(int id, String title, Object data) {
        this.id = id;
        this.title = title;
        this.data = data;
    }

    public int id;
    public String title;
    public Object data;
}
