package com.ftofs.twant.domain.chain;

public class Clerk {
    /**
     * 店员编号
     */
    private int clerkId;

    /**
     * 店员名称
     */
    private String clerkName;

    /**
     * 登录密码
     */
    private String clerkPassword;

    /**
     * 门店编号
     */
    private int chainId;

    /**
     * 是否店主
     */
    private int isOwner = 0;

    /**
     * 用户组ID
     */
    private int groupId = 0;

    /**
     * 用户组ID
     */
    private String groupName;

    /**
     * 店员头像
     */
    private String avatar;

    /**
     * 店员头像
     */
    private String avatarSrc;

    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
    }

    public String getClerkName() {
        return clerkName;
    }

    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    public String getClerkPassword() {
        return clerkPassword;
    }

    public void setClerkPassword(String clerkPassword) {
        this.clerkPassword = clerkPassword;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarSrc() {
        return avatarSrc;
    }

    public void setAvatarSrc(String avatarSrc) {
        this.avatarSrc = avatarSrc;
    }

    @Override
    public String toString() {
        return "Clerk{" +
                "clerkId=" + clerkId +
                ", clerkName='" + clerkName + '\'' +
                ", clerkPassword='" + clerkPassword + '\'' +
                ", chainId=" + chainId +
                ", isOwner=" + isOwner +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatarSrc='" + avatarSrc + '\'' +
                '}';
    }
}
