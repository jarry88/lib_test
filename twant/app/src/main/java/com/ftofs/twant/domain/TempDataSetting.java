package com.ftofs.twant.domain;

import java.io.Serializable;

public class TempDataSetting implements Serializable {
    /**
     * 数据名称
     */
    private String title;

    /**
     * 数据值
     */
    private String value = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "TempDataSetting{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
