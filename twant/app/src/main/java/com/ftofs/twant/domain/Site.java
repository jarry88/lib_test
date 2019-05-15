package com.ftofs.twant.domain;

import java.io.Serializable;

public class Site implements Serializable {
    /**
     * 配置项名称
     * 主键
     */
    private String title;

    /**
     * 配置项值
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
        return "Setting{" +
                "title='" + title + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
