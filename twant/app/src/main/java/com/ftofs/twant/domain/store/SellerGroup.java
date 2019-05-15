package com.ftofs.twant.domain.store;

public class SellerGroup {
    /**
     * 权限组编号
     */
    private int groupId;

    /**
     * 权限组名称
     */
    private String groupName;

    /**
     * 店铺编号
     */
    private int storeId;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "SellerGroup{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
