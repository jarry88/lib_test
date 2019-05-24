package com.ftofs.twant.domain.store;

import java.io.Serializable;


public class AlbumFiles implements Serializable {
    /**
     * 文件编号
     */
    private int filesId;

    /**
     * 原名
     */
    private String originalName;

    /**
     * 文件名称
     */
    private String filesName;

    /**
     * 图片路径
     */
    private String filesSrc;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 相册编号
     */
    private int albumId = 0;

    /**
     * 上传时间
     */
    private String uploadTime;

    /**
     * 图片类型
     */
    private int filesType;

    /**
     * 高
     */
    private int filesHeight = 0;

    /**
     * 宽
     */
    private int filesWidth = 0;

    /**
     * 大小
     * 单位：字节
     */
    private long filesSize = 0;

    /**
     * 返回KB
     */
    private long filesSizeKB;

    public int getFilesId() {
        return filesId;
    }

    public void setFilesId(int filesId) {
        this.filesId = filesId;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFilesName() {
        return filesName;
    }

    public void setFilesName(String filesName) {
        this.filesName = filesName;
    }

    public String getFilesSrc() {
        return filesSrc;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public int getFilesType() {
        return filesType;
    }

    public void setFilesType(int filesType) {
        this.filesType = filesType;
    }

    public int getFilesHeight() {
        return filesHeight;
    }

    public void setFilesHeight(int filesHeight) {
        this.filesHeight = filesHeight;
    }

    public int getFilesWidth() {
        return filesWidth;
    }

    public void setFilesWidth(int filesWidth) {
        this.filesWidth = filesWidth;
    }

    public long getFilesSize() {
        return filesSize;
    }

    public void setFilesSize(long filesSize) {
        this.filesSize = filesSize;
    }

    public long getFilesSizeKB() {
        return filesSize/1024;
    }

    @Override
    public String toString() {
        return "AlbumFiles{" +
                "filesId=" + filesId +
                ", originalName='" + originalName + '\'' +
                ", filesName='" + filesName + '\'' +
                ", filesSrc='" + filesSrc + '\'' +
                ", storeId=" + storeId +
                ", albumId=" + albumId +
                ", uploadTime=" + uploadTime +
                ", filesType=" + filesType +
                ", filesHeight=" + filesHeight +
                ", filesWidth=" + filesWidth +
                ", filesSize=" + filesSize +
                ", filesSizeKB=" + filesSizeKB +
                '}';
    }
}
