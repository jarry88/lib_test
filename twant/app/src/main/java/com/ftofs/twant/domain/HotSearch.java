package com.ftofs.twant.domain;

public class HotSearch {
    /**
     * 主键、自增
     */
    private Integer hotId;

    /**
     * 显示词
     */
    private String hotName = "";

    /**
     * 搜索词
     */
    private String hotValue = "";

    public Integer getHotId() {
        return hotId;
    }

    public void setHotId(Integer hotId) {
        this.hotId = hotId;
    }

    public String getHotName() {
        return hotName;
    }

    public void setHotName(String hotName) {
        this.hotName = hotName;
    }

    public String getHotValue() {
        return hotValue;
    }

    public void setHotValue(String hotValue) {
        this.hotValue = hotValue;
    }

    @Override
    public String toString() {
        return "HotSearch{" +
                "hotId=" + hotId +
                ", hotName='" + hotName + '\'' +
                ", hotValue='" + hotValue + '\'' +
                '}';
    }
}
