package com.ftofs.twant.domain.chain;

public class ClerkGroupPermission {
    /**
     * 编号
     */
    private int id;

    /**
     * 组编号
     */
    private int groupId;

    /**
     * 权限编号
     */
    private int menuId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "ClerkGroupPermission{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", menuId=" + menuId +
                '}';
    }
}
