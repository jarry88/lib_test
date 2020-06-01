package com.ftofs.twant.domain;

import cn.snailpad.easyjson.EasyJSONObject;

public class Format {
    /**
     * 编号
     */
    private Integer formatId;

    /**
     * 名称
     */
    private String formatName;

    /**
     * 排序
     */
    private int sort;

    public static Format parse(EasyJSONObject o) throws Exception{
        Format format = new Format();
        format.formatId = o.getInt("formatId");
        format.formatName = o.getSafeString("formatName");
        return format;
    }

    public Integer getFormatId() {
        return formatId;
    }

    public void setFormatId(Integer formatId) {
        this.formatId = formatId;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + formatId +
                ", name='" + formatName + '\'' +
                ", sort=" + sort +
                '}';
    }
}
