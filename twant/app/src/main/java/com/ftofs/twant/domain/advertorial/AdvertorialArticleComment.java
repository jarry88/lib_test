package com.ftofs.twant.domain.advertorial;

import java.io.Serializable;

public class AdvertorialArticleComment implements Serializable {
    /**
     * 回复id
     */
    private int commentId;

    /**
     * 想要帖id
     */
    private int articleId;

    /**
     * 想要帖标题
     */
    private String  articleTitle;

    /**
     * 想要帖作者会员id
    */
    private int authorMemberId;

    /**
     * 评论发布者的会员ID
     */
    private int memberId;

    /**
     * 评论发布者的会员名称
     */
    private String memberName;

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

    public int getAuthorMemberId() {
        return authorMemberId;
    }

    public void setAuthorMemberId(int authorMemberId) {
        this.authorMemberId = authorMemberId;
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

    @Override
    public String toString() {
        return "AdvertorialArticleComment{" +
                "commentId=" + commentId +
                ", articleId=" + articleId +
                ", articleTitle='" + articleTitle + '\'' +
                ", authorMemberId=" + authorMemberId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", content='" + content + '\'' +
                ", addTime=" + addTime +
                ", state=" + state +
                '}';
    }
}
