package com.ftofs.twant.domain;

import java.io.Serializable;

public class ImLinkMan implements Serializable {
    private int linkId;

    /**
     * 会员id
     */
    private int userId = 0;

    /**
     * 头像
     */
    private String userAvatar = "";

    /**
     * 会员类型，1：买家， 2： 卖家
     */
    private Integer userType = 1;

    /**
     * 会员名称
     */
    private String userName = "";

    /**
     * 联系人状态，1：正在联系， 2：历史联系人
     */
    private Integer linkState = 1;

    /**
     *  联系人会员id
     */
    private int linkManId = 0;

    /**
     * 联系人头像
     */
    private String linkManAvatar = "";

    /**
     * 联系人名称
     */
    private String linkManName = "";

    /**
     * 联系人店铺ID
     */
    private int linkManStoreId = 0;

    /**
     * 联系人店铺名称
     */
    private String linkManStoreName = "";

    /**
     * 买家删除 , 1.未删除 2.删除
     */
    private Integer memberDel = 1;

    /**
     * 卖家删除, 1.未删除 2.删除
     */
    private Integer sellerDel = 1;

    /**
     * 商品spu编号
     */
    private int commonId;

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getLinkState() {
        return linkState;
    }

    public void setLinkState(Integer linkState) {
        this.linkState = linkState;
    }

    public int getLinkManId() {
        return linkManId;
    }

    public void setLinkManId(int linkManId) {
        this.linkManId = linkManId;
    }

    public String getLinkManAvatar() {
        return linkManAvatar;
    }

    public void setLinkManAvatar(String linkManAvatar) {
        this.linkManAvatar = linkManAvatar;
    }

    public String getLinkManName() {
        return linkManName;
    }

    public void setLinkManName(String linkManName) {
        this.linkManName = linkManName;
    }

    public int getLinkManStoreId() {
        return linkManStoreId;
    }

    public void setLinkManStoreId(int linkManStoreId) {
        this.linkManStoreId = linkManStoreId;
    }

    public String getLinkManStoreName() {
        return linkManStoreName;
    }

    public void setLinkManStoreName(String linkManStoreName) {
        this.linkManStoreName = linkManStoreName;
    }

    public Integer getMemberDel() {
        return memberDel;
    }

    public void setMemberDel(Integer memberDel) {
        this.memberDel = memberDel;
    }

    public Integer getSellerDel() {
        return sellerDel;
    }

    public void setSellerDel(Integer sellerDel) {
        this.sellerDel = sellerDel;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    @Override
    public String toString() {
        return "ImLinkMan{" +
                "linkId=" + linkId +
                ", userId=" + userId +
                ", userAvatar='" + userAvatar + '\'' +
                ", userType=" + userType +
                ", userName='" + userName + '\'' +
                ", linkState=" + linkState +
                ", linkManId=" + linkManId +
                ", linkManAvatar='" + linkManAvatar + '\'' +
                ", linkManName='" + linkManName + '\'' +
                ", linkManStoreId=" + linkManStoreId +
                ", linkManStoreName='" + linkManStoreName + '\'' +
                ", memberDel=" + memberDel +
                ", sellerDel=" + sellerDel +
                ", commonId=" + commonId +
                '}';
    }
}
