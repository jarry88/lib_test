package com.ftofs.twant.domain.store;

import java.io.Serializable;

public class SellerMenu implements Serializable {
    /**
     * 菜单编号
     */
    private int id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单链接
     */
    private String url;

    /**
     * 父级编号
     */
    private int parentId;

    /**
     * 权限组编号
     * 如果几个菜单的权限有关联性需要同时被选中时需要设置相同的编号
     */
    private int groupId;

    /**
     * 自营店铺专属菜单
     */
    private int isOwn;

    /**
     * 是否禁用
     */
    private int isActive;//Modify By Nick.Chung 2018/7/27 14:03 菜單是否禁用

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getIsOwn() {
        return isOwn;
    }

    public void setIsOwn(int isOwn) {
        this.isOwn = isOwn;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "SellerMenu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", parentId=" + parentId +
                ", groupId=" + groupId +
                ", isOwn=" + isOwn +
                '}';
    }
}

