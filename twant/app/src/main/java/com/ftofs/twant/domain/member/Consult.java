package com.ftofs.twant.domain.member;

import java.io.Serializable;

public class Consult implements Serializable {
    /**
     * 自增编码
     */
    private int consultId;

    /**
     * 商品编号
     */
    private int commonId = 0;

    /**
     * 会员ID
     */
    private int memberId = 0;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 头像
     */
    private String avatar = "";

    /**
     * 头像路径
     */
    private String avatarUrl = "";

    /**
     * 店铺ID
     */
    private int storeId = 0;

    /**
     * 店铺名称
     */
    private String storeName = "";

    /**
     * 分类ID
     */
    private int classId = 0;

    /**
     * 分类名称
     */
    private String className = "";

    /**
     * 咨询内容
     */
    private String consultContent = "";

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 卖家是否已回复
     * 0未回复 1已回复
     */
    private int consultReplyState = 0;

    /**
     * 回复内容
     */
    private String consultReply = "";

    /**
     * 回复时间
     */
    private String replyTime;

    /**
     * 会员已读回复
     * 0未读 1已读
     */
    private int readState = 0;

    /**
     * 会员名称（匿名部分名称替换为*）
     */
    private String memberNameFinal = "";

    public int getConsultId() {
        return consultId;
    }

    public void setConsultId(int consultId) {
        this.consultId = consultId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

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

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getConsultContent() {
        return consultContent;
    }

    public void setConsultContent(String consultContent) {
        this.consultContent = consultContent;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getConsultReplyState() {
        return consultReplyState;
    }

    public void setConsultReplyState(int consultReplyState) {
        this.consultReplyState = consultReplyState;
    }

    public String getConsultReply() {
        return consultReply;
    }

    public void setConsultReply(String consultReply) {
        this.consultReply = consultReply;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    public String getMemberNameFinal() {
        return memberNameFinal;
    }

    public void setMemberNameFinal(String memberNameFinal) {
        this.memberNameFinal = memberNameFinal;
    }

    @Override
    public String toString() {
        return "Consult{" +
                "consultId=" + consultId +
                ", commonId=" + commonId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", classId=" + classId +
                ", className='" + className + '\'' +
                ", consultContent='" + consultContent + '\'' +
                ", addTime=" + addTime +
                ", consultReplyState=" + consultReplyState +
                ", consultReply='" + consultReply + '\'' +
                ", replyTime=" + replyTime +
                ", readState=" + readState +
                ", memberNameFinal='" + memberNameFinal + '\'' +
                '}';
    }
}
