package com.ftofs.twant.vo.advertorial;

import java.io.Serializable;

/**
 * copyright  Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 文章评论回复vo
 *
 * @author cj
 * Created 2017-10-10 下午 3:13
 */
public class AdvertorialArticleCommentReplyVo implements Serializable {
    /**
     * 回复id
     */
    private Integer replyId;
    /**
     * 评论id
     */
    private int commentId;
    /**
     * 文章id
     */
    private int articleId;
    /**
     * 文章 标题
     */
    private String  articleTitle;

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
    private int replySuperId ;
    /**
     * 回复回复的内容
     */
    private String replySuperContent ;
    /**
     * 回复的会员id
     */
    private int replyMemberId ;
    /**
     * 回复的会员名称
     */
    private String replyMemberName;
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
    /**
     * 是否是自己发的回复
     */
    private int isMine = 0;
    /**
     * 头像地址
     */
    private String avatarUrl = "";

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

    public int getIsMine() {
        return isMine;
    }

    public void setIsMine(int isMine) {
        this.isMine = isMine;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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
        return "AdvertorialArticleCommentReplyVo{" +
                "replyId=" + replyId +
                ", commentId=" + commentId +
                ", articleId=" + articleId +
                ", articleTitle='" + articleTitle + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", replySuperId=" + replySuperId +
                ", replyMemberId=" + replyMemberId +
                ", replyMemberName='" + replyMemberName + '\'' +
                ", content='" + content + '\'' +
                ", addTime=" + addTime +
                ", state=" + state +
                ", isMine=" + isMine +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
