package com.ftofs.twant.vo.advertorial;

import com.ftofs.twant.domain.advertorial.AdvertorialArticleComment;
import com.ftofs.twant.domain.member.Member;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * copyright  Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 文章评论vo
 *
 * @author cj
 * Created 2017-10-10 下午 3:13
 */
public class AdvertorialArticleCommentVo implements Serializable {
    /**
     * 评论id
     */
    private Integer commentId;
    /**
     * 文章id
     */
    private int articleId;
    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 作者会员id
     */
    private int authorMemberId;
    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 头像地址
     */
    private String avatarUrl = "";

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

    /**
     * 是否是自己发的回复
     */
    private int isMine = 0;
    /**
     * 该评论的回复数量
     */
    private long replyNum = 0 ;
    /**
     * 作者回复列表
     */
    private List<AdvertorialArticleCommentReplyVo> replyList = new ArrayList<>();

    /**
     *
     * @param advertorialArticleComment
     */
    public AdvertorialArticleCommentVo(AdvertorialArticleComment advertorialArticleComment) {
        this.commentId = advertorialArticleComment.getCommentId();
        this.articleId = advertorialArticleComment.getArticleId();
        this.articleTitle = advertorialArticleComment.getArticleTitle();
        this.memberId = advertorialArticleComment.getMemberId();
        this.memberName = advertorialArticleComment.getMemberName();
        this.content = advertorialArticleComment.getContent();
        this.addTime = advertorialArticleComment.getAddTime();
        this.state = advertorialArticleComment.getState();
        this.authorMemberId = advertorialArticleComment.getAuthorMemberId();
    }

    /**
     *
     * @param advertorialArticleComment
     * @param member
     */
    public AdvertorialArticleCommentVo(AdvertorialArticleComment advertorialArticleComment, Member member) {
        this.commentId = advertorialArticleComment.getCommentId();
        this.articleId = advertorialArticleComment.getArticleId();
        this.articleTitle = advertorialArticleComment.getArticleTitle();
        this.memberId = advertorialArticleComment.getMemberId();
        this.memberName = advertorialArticleComment.getMemberName();
        this.content = advertorialArticleComment.getContent();
        this.addTime = advertorialArticleComment.getAddTime();
        this.state = advertorialArticleComment.getState();
        this.authorMemberId = advertorialArticleComment.getAuthorMemberId();
        this.avatarUrl = member.getAvatarUrl();
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public int getIsMine() {
        return isMine;
    }

    public void setIsMine(int isMine) {
        this.isMine = isMine;
    }

    public List<AdvertorialArticleCommentReplyVo> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<AdvertorialArticleCommentReplyVo> replyList) {
        this.replyList = replyList;
    }

    public long getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(long replyNum) {
        this.replyNum = replyNum;
    }

    @Override
    public String toString() {
        return "AdvertorialArticleCommentVo{" +
                "commentId=" + commentId +
                ", articleId=" + articleId +
                ", articleTitle='" + articleTitle + '\'' +
                ", authorMemberId=" + authorMemberId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", content='" + content + '\'' +
                ", addTime=" + addTime +
                ", state=" + state +
                ", isMine=" + isMine +
                ", replyNum=" + replyNum +
                ", replyList=" + replyList +
                '}';
    }
}
