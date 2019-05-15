package com.ftofs.twant.domain.show;

import java.io.Serializable;

public class ShowOrdersMusic implements Serializable {
    /**
     * 晒宝音乐id
     * 主键、自增
     */
    private int musicId;

    /**
     * 展示名称
     */
    private String showText;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 文件地址全路径路径
     */
    private String filePath="";

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        return fileUrl;
    }

    @Override
    public String toString() {
        return "ShowOrdersMusic{" +
                "musicId=" + musicId +
                ", showText='" + showText + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
