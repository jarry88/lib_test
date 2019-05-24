package com.ftofs.twant.domain.store;

import com.ftofs.twant.domain.member.Member;



public class Seller {
    /**
     * 卖家编号
     */
    private int sellerId;

    /**
     * 卖家用户名
     */
    private String sellerName;

    /**
     * 卖家密码
     */
    private String sellerPassword;

    /**
     * 卖家邮箱
     */
    private String sellerEmail;

    /**
     * 卖家手机
     */
    private String sellerMobile;

    /**
     * 店铺ID
     */
    private int storeId = 0;

    /**
     * 店铺名称
     */
    private String storeName = "";

    /**
     * 是否是管理员
     * 0-不是 1-是
     */
    private int isStoreOwner = 0;

    /**
     * 卖家用户组ID
     */
    private int groupId = 0;

    /**
     * 卖家用户组ID
     */
    private String groupName;

    /**
     * 注册时间
     */
    private String joinDate;

    /**
     * 最后登录时间
     */
    private String lastLoginTime;

    /**
     * 是否可以登录 0-禁止登陆 1-可以登录
     */
    private int allowLogin = 1;

    /**
     * 商家头像
     */
    private String avatar;

    /**
     * 商家头像URL
     */
    private String avatarUrl;

    /**
     * 是否可登录App 1是 0否
     */
    private int allowAppLogin = 0;

    /**
     * 是否接收
     */
    private int isReceive = 0;

    /**
     * 會員資料
     */
    private Member member;

    /**
     * Modify By liusf 2018/9/6 11:11
     * 店員狀態(0初始狀態，1審核中，2正式員工，3離職，4拒絕)
     */
    private int employState;

    /**
     * 商家完善资料引导提示
     */
    private int isBoot;

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPassword() {
        return sellerPassword;
    }

    public void setSellerPassword(String sellerPassword) {
        this.sellerPassword = sellerPassword;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getSellerMobile() {
        return sellerMobile;
    }

    public void setSellerMobile(String sellerMobile) {
        this.sellerMobile = sellerMobile;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getIsStoreOwner() {
        return isStoreOwner;
    }

    public void setIsStoreOwner(int isStoreOwner) {
        this.isStoreOwner = isStoreOwner;
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

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getAllowLogin() {
        return allowLogin;
    }

    public void setAllowLogin(int allowLogin) {
        this.allowLogin = allowLogin;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getAllowAppLogin() {
        return allowAppLogin;
    }

    public void setAllowAppLogin(int allowAppLogin) {
        this.allowAppLogin = allowAppLogin;
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getEmployState() {
        return employState;
    }

    public void setEmployState(int employState) {
        this.employState = employState;
    }

    public int getIsBoot() {
        return isBoot;
    }

    public void setIsBoot(int isBoot) {
        this.isBoot = isBoot;
    }

    @Override
    public String toString() {
        return "Seller{" +
                "sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", sellerPassword='" + sellerPassword + '\'' +
                ", sellerEmail='" + sellerEmail + '\'' +
                ", sellerMobile='" + sellerMobile + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", isStoreOwner=" + isStoreOwner +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", joinDate=" + joinDate +
                ", lastLoginTime=" + lastLoginTime +
                ", allowLogin=" + allowLogin +
                ", avatar='" + avatar + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", allowAppLogin=" + allowAppLogin +
                ", isReceive=" + isReceive +
                ", member=" + member +
                ", employState=" + employState +
                '}';
    }
}


