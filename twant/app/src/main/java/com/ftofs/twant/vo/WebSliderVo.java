package com.ftofs.twant.vo;

import java.util.HashMap;

/**
 * 焦点图VO
 */
public class WebSliderVo {
    /**
     * Id
     */
    private int webSliderId;

    /**
     * 类型
     */
    private String webSliderType;

    /**
     * 排序
     */
    private int webSliderSort;

    /**
     * 标识key
     */
    private String webSliderKey;

    /**
     * 保存的json数据
     */
    private String webSliderJson;

    private HashMap<String,Object> webSliderJsonMap = new HashMap<>();

    public int getWebSliderId() {
        return webSliderId;
    }

    public void setWebSliderId(int webSliderId) {
        this.webSliderId = webSliderId;
    }

    public String getWebSliderType() {
        return webSliderType;
    }

    public void setWebSliderType(String webSliderType) {
        this.webSliderType = webSliderType;
    }

    public int getWebSliderSort() {
        return webSliderSort;
    }

    public void setWebSliderSort(int webSliderSort) {
        this.webSliderSort = webSliderSort;
    }

    public String getWebSliderKey() {
        return webSliderKey;
    }

    public void setWebSliderKey(String webSliderKey) {
        this.webSliderKey = webSliderKey;
    }

    public String getWebSliderJson() {
        return webSliderJson;
    }

    public void setWebSliderJson(String webSliderJson) {
        this.webSliderJson = webSliderJson;
    }

    public HashMap<String, Object> getWebSliderJsonMap() {
        return webSliderJsonMap;
    }

    public void setWebSliderJsonMap(HashMap<String, Object> webSliderJsonMap) {
        this.webSliderJsonMap = webSliderJsonMap;
    }

}
