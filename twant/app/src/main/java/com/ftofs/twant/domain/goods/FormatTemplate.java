package com.ftofs.twant.domain.goods;

import java.io.Serializable;

public class FormatTemplate implements Serializable {
    /**
     * 关联板式编号
     */
    private int formatId;

    /**
     * 关联版式名称
     */
    private String formatName;

    /**
     * 关联板式位置
     */
    private int formatSite;

    /**
     * 管理板式内容
     */
    private String formatContent;

    /**
     * 管理板式内容
     */
    private String formatMobileContent;

    /**
     * 店编编号
     */
    private int storeId;

    public int getFormatId() {
        return formatId;
    }

    public void setFormatId(int formatId) {
        this.formatId = formatId;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }

    public int getFormatSite() {
        return formatSite;
    }

    public void setFormatSite(int formatSite) {
        this.formatSite = formatSite;
    }

    public String getFormatContent() {
        return formatContent;
    }

    public void setFormatContent(String formatContent) {
        this.formatContent = formatContent;
    }

    public String getFormatMobileContent() {
        return formatMobileContent;
    }

    public void setFormatMobileContent(String formatMobileContent) {
        this.formatMobileContent = formatMobileContent;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "FormatTemplate{" +
                "formatId=" + formatId +
                ", formatName='" + formatName + '\'' +
                ", formatSite=" + formatSite +
                ", formatContent='" + formatContent + '\'' +
                ", formatMobileContent='" + formatMobileContent + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
