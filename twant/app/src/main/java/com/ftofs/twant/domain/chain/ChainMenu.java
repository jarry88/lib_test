package com.ftofs.twant.domain.chain;

import java.util.ArrayList;
import java.util.List;

public class ChainMenu {
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

    private List<ChainMenu> chainMenuList = new ArrayList<>();

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

    public List<ChainMenu> getChainMenuList() {
        return chainMenuList;
    }

    public void setChainMenuList(List<ChainMenu> chainMenuList) {
        this.chainMenuList = chainMenuList;
    }

    @Override
    public String toString() {
        return "ChainMenu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", permission='" + permission + '\'' +
                ", parentId=" + parentId +
                ", chainMenuList=" + chainMenuList +
                '}';
    }
}
