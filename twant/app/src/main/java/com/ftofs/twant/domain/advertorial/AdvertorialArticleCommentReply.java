package com.ftofs.twant.domain.advertorial;

import java.io.Serializable;

public class AdvertorialArticleCommentReply implements Serializable {
    /**
     * 回复id
     */
    private Integer replyId;

    /**
     * 评论id
     */
    private int commentId;

    /**
     * 想要帖id
     */
    private int articleId;

    /**
     * 想要帖 标题
     */
    private String articleTitle;

    /**
     * 发布者会员ID
     */
    private int memberId;

    /**
     * 发布者会员名称
     */
    private String memberName;

    /**
     * 回复回复的id
     */
    private int replySuperId;

    /**
     * 回复的会员id
     */
    private int replyMemberId;

    /**
     * 回复的会员名称
     */
    private String replyMemberName;

    /**
     * 回复回复的内容
     */
    private String replySuperContent;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 状态
     * 1.正常 0.隐藏
     */
    private int state = 1;

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getReplyMemberId() {
        return replyMemberId;
    }

    public void setReplyMemberId(int replyMemberId) {
        this.replyMemberId = replyMemberId;
    }

    public String getReplyMemberName() {
        return replyMemberName;
    }

    public void setReplyMemberName(String replyMemberName) {
        this.replyMemberName = replyMemberName;
    }

    public int getReplySuperId() {
        return replySuperId;
    }

    public void setReplySuperId(int replySuperId) {
        this.replySuperId = replySuperId;
    }

    public String getReplySuperContent() {
        return replySuperContent;
    }

    public void setReplySuperContent(String replySuperContent) {
        this.replySuperContent = replySuperContent;
    }

    @Override
    public String toString() {
        return "AdvertorialArticleCommentReply{" +
                "replyId=" + replyId +
                ", commentId=" + commentId +
                ", articleId=" + articleId +
                ", articleTitle='" + articleTitle + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", replySuperId=" + replySuperId +
                ", replyMemberId=" + replyMemberId +
                ", replyMemberName='" + replyMemberName + '\'' +
                ", replySuperContent='" + replySuperContent + '\'' +
                ", content='" + content + '\'' +
                ", addTime=" + addTime +
                ", state=" + state +
                '}';
    }
}
