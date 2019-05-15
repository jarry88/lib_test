package com.ftofs.twant.domain.show;

import java.io.Serializable;
import java.sql.Timestamp;

public class ShowOrdersComment implements Serializable {
    /**
     * 回复id
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
     * 作者会员id
    */
    private int authorMemberId;

    /**
     * 评论的会员ID
     */
    private int memberId;

    /**
     * 评论的会员名称
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


    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
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

    public String getShowOrdersTitle() {
        return showOrdersTitle;
    }

    public void setShowOrdersTitle(String showOrdersTitle) {
        this.showOrdersTitle = showOrdersTitle;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getAuthorMemberId() {
        return authorMemberId;
    }

    public void setAuthorMemberId(int authorMemberId) {
        this.authorMemberId = authorMemberId;
    }

    @Override
    public String toString() {
        return "ShowOrdersComment{" +
                "commentId=" + commentId +
                ", showOrdersId=" + showOrdersId +
                ", showOrdersTitle='" + showOrdersTitle + '\'' +
                ", authorMemberId=" + authorMemberId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", content='" + content + '\'' +
                ", addTime=" + addTime +
                ", state=" + state +
                '}';
    }
}
