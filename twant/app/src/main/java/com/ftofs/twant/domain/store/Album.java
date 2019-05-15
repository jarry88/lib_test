package com.ftofs.twant.domain.store;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    /**
     * 相册编号
     */
    private int albumId;

    /**
     * 相册名称
     */
    private String albumName;

    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 父级相册编号
     */
    private int parentId = 0;

    /**
     * 子集相册列表
     */
    private List<Album> children = new ArrayList<>();

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<Album> getChildren() {
        return children;
    }

    public void setChildren(List<Album> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", storeId=" + storeId +
                ", createTime=" + createTime +
                ", parentId=" + parentId +
                ", children=" + children +
                '}';
    }
}
