package com.ftofs.twant.domain.chain;

import java.util.ArrayList;
import java.util.List;

public class ClerkGroup {
    /**
     * 编号
     */
    private int groupId;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 门店编号
     */
    private int chainId;

    /**
     * 权限列表
     */
    private List<ClerkGroupPermission> clerkGroupPermissionList = new ArrayList<>();

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

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public List<ClerkGroupPermission> getClerkGroupPermissionList() {
        return clerkGroupPermissionList;
    }

    public void setClerkGroupPermissionList(List<ClerkGroupPermission> clerkGroupPermissionList) {
        this.clerkGroupPermissionList = clerkGroupPermissionList;
    }

    @Override
    public String toString() {
        return "ClerkGroup{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", chainId=" + chainId +
                ", clerkGroupPermissionList=" + clerkGroupPermissionList +
                '}';
    }
}
