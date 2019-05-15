package com.ftofs.twant.domain.show;

import java.io.Serializable;
import java.sql.Timestamp;

public class ShowOrdersAuthorReply implements Serializable {
    /**
     * 回复id
     */
    private Integer authorReplyId;

    /**
     * 评论id
     */
    private int commentId;

    /**
     * 晒宝id
     */
    private int showOrdersId;

    /**
     * 晒宝 标题
     */
    private String  showOrdersTitle;

    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 添加时间
     */
    private Timestamp addTime;

    /**
     * 状态
     * 1.正常 0.隐藏
     */
    private int state = 1;

    public Integer getAuthorReplyId() {
        return authorReplyId;
    }

    public void setAuthorReplyId(Integer authorReplyId) {
        this.authorReplyId = authorReplyId;
    }

    public int getShowOrdersId() {
        return showOrdersId;
    }

    public void setShowOrdersId(int showOrdersId) {
        this.showOrdersId = showOrdersId;
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

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getShowOrdersTitle() {
        return showOrdersTitle;
    }

    public void setShowOrdersTitle(String showOrdersTitle) {
        this.showOrdersTitle = showOrdersTitle;
    }

    @Override
    public String toString() {
        return "ShowOrdersAuthorReply{" +
                "authorReplyId=" + authorReplyId +
                ", commentId=" + commentId +
                ", showOrdersId=" + showOrdersId +
                ", showOrdersTitle=" + showOrdersTitle +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", content='" + content + '\'' +
                ", addTime=" + addTime +
                ", state=" + state +
                '}';
    }
}
