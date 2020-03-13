package com.ftofs.twant.domain.advertorial;

import java.io.Serializable;

public class AdvertorialAuthor implements Serializable {
    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 达人昵称
     */
    private String authorName;

    /**
     * 达人头像
     */
    private String authorAvatar;

    /**
     * 简介
     */
    private String authorAbstract;

    /**
     * 审核状态 1.正常 0.删除
     */
    private int state = 1;

    /**
     * 关注数
     */
    private int subCount;

    /**
     * 想要帖数
     */
    private int articleCount;

    /**
     * 头像路径
     */
    private String authorAvatarUrl = "";

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getSubCount() {
        return subCount;
    }

    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public String getAuthorAbstract() {
        return authorAbstract;
    }

    public void setAuthorAbstract(String authorAbstract) {
        this.authorAbstract = authorAbstract;
    }

    public String getAuthorAvatarUrl() {
        return authorAvatarUrl;
    }

    public void setAuthorAvatarUrl(String authorAvatarUrl) {
        this.authorAvatarUrl = authorAvatarUrl;
    }

    @Override
    public String toString() {
        return "AdvertorialAuthor{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorAvatar='" + authorAvatar + '\'' +
                ", authorAbstract='" + authorAbstract + '\'' +
                ", state=" + state +
                ", subCount=" + subCount +
                ", articleCount=" + articleCount +
                ", authorAvatarUrl='" + authorAvatarUrl + '\'' +
                '}';
    }
}
