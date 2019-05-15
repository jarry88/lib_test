package com.ftofs.twant.domain.statistics;

import java.io.Serializable;

public class StatSetting implements Serializable {
    /**
     * 项目名称
     */
    private String title;

    /**
     * 项目值
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
        return "StatSetting{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
