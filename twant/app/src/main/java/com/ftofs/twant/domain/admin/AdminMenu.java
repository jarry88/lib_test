package com.ftofs.twant.domain.admin;

import java.io.Serializable;
import java.util.List;

public class AdminMenu implements Serializable {
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
     * 权限
     */
    private String permission;

    /**
     * 父级编号
     */
    private int parentId;

    /**
     * 权限组编号
     * 如果几个菜单的权限有关联性需要同时被选中时需要设置相同的编号
     */
    private int groupId;

    private List<AdminMenu> subMenu;

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

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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

    public List<AdminMenu> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<AdminMenu> subMenu) {
        this.subMenu = subMenu;
    }

    @Override
    public String toString() {
        return "AdminMenu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", permission='" + permission + '\'' +
                ", parentId=" + parentId +
                ", groupId=" + groupId +
                ", subMenu=" + subMenu +
                '}';
    }
}

