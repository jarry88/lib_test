package com.ftofs.twant.domain.comment;

import java.math.BigInteger;


public class WantComment {
    /**
     * 說說id
     */
    private BigInteger commentId;

    /**
     * 說說類型 1全部 2視頻 3文本
     */
    private int commentType;

    /**
     * 想要帖渠道 1全部 2商店 3產品 4想要帖 5推文
     */
    private int commentChannel;

    /**
     * 說說類容
     */
    private String content;

    /**
     * 層級，1-回覆主題，2-回覆說說
     */
    private int deep;

    /**
     * 關聯商店Id
     */
    private int relateStoreId;

    /**
     * 關聯產品SPU
     */
    private int relateCommonId;

    /**
     * 關聯想要帖Id
     */
    private BigInteger relatePostId;

    /**
     * 回覆評論Id
     */
    private BigInteger replyCommentId;

    /**
     * 評論人
     */
    private String createBy;

    /**
     * 評論時間
     */
    private String createTime;

    /**
     * 0-禁用,1-啟用
     */
    private int commentState;

    /**
     * 讚想評論數
     */
    private int commentLike;

    /**
     * 踩該評論數
     */
    private int commentUnlike;

    /**
     * 評論收藏數
     */
    private int commentFavor;

    /**
     * 評論回覆數
     */
    private int commentReply;

    /**
     * 評論分享數
     */
    private int commentShare;

    /**
     * 評論者暱稱
     */
    private String nickName;

    /**
     * 想要帖標題
     */
    private String wantPostTitle;

    /**
     * 評論圖片
     */
    private String images;

    public WantComment(){}

    public BigInteger getCommentId() {
        return commentId;
    }

    public void setCommentId(BigInteger commentId) {
        this.commentId = commentId;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public int getCommentChannel() {
        return commentChannel;
    }

    public void setCommentChannel(int commentChannel) {
        this.commentChannel = commentChannel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public int getRelateStoreId() {
        return relateStoreId;
    }

    public void setRelateStoreId(int relateStoreId) {
        this.relateStoreId = relateStoreId;
    }

    public int getRelateCommonId() {
        return relateCommonId;
    }

    public void setRelateCommonId(int relateCommonId) {
        this.relateCommonId = relateCommonId;
    }

    public BigInteger getRelatePostId() {
        return relatePostId;
    }

    public void setRelatePostId(BigInteger relatePostId) {
        this.relatePostId = relatePostId;
    }

    public BigInteger getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(BigInteger replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCommentState() {
        return commentState;
    }

    public void setCommentState(int commentState) {
        this.commentState = commentState;
    }

    public int getCommentLike() {
        return commentLike;
    }

    public void setCommentLike(int commentLike) {
        this.commentLike = commentLike;
    }

    public int getCommentUnlike() {
        return commentUnlike;
    }

    public void setCommentUnlike(int commentUnlike) {
        this.commentUnlike = commentUnlike;
    }

    public int getCommentFavor() {
        return commentFavor;
    }

    public void setCommentFavor(int commentFavor) {
        this.commentFavor = commentFavor;
    }

    public int getCommentReply() {
        return commentReply;
    }

    public void setCommentReply(int commentReply) {
        this.commentReply = commentReply;
    }

    public int getCommentShare() {
        return commentShare;
    }

    public void setCommentShare(int commentShare) {
        this.commentShare = commentShare;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWantPostTitle() {
        return wantPostTitle;
    }

    public void setWantPostTitle(String wantPostTitle) {
        this.wantPostTitle = wantPostTitle;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "WantComment{" +
                "commentId=" + commentId +
                ", commentType=" + commentType +
                ", commentChannel=" + commentChannel +
                ", content='" + content + '\'' +
                ", deep=" + deep +
                ", relateStoreId=" + relateStoreId +
                ", relateCommonId=" + relateCommonId +
                ", relatePostId=" + relatePostId +
                ", replyCommentId=" + replyCommentId +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", commentState=" + commentState +
                ", commentLike=" + commentLike +
                ", commentUnlike=" + commentUnlike +
                ", commentFavor=" + commentFavor +
                ", commentReply=" + commentReply +
                ", commentShare=" + commentShare +
                ", nickName='" + nickName + '\'' +
                ", wantPostTitle='" + wantPostTitle + '\'' +
                ", images='" + images + '\'' +
                '}';
    }
}
