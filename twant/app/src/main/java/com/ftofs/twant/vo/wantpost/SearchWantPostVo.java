package com.ftofs.twant.vo.wantpost;

import com.ftofs.twant.vo.comment.WantCommentVo;
import com.ftofs.twant.vo.member.MemberVo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author liusf
 * @create 2019/3/18 18:27
 * @description 貼文視圖類，用於搜索引擎建立索引
 */
public class SearchWantPostVo {
    /**
     * 貼文id
     */
    private BigInteger postId;
    /**
     * 標題
     */
    private String title;
    /**
     * 內容
     */
    private String content;
    /**
     * 分類
     */
    private String postCategory;
    /**
     * 封面圖
     */
    private String coverImage;
    /**
     * 類型 1通用 2求購
     */
    private int postType;
    /**
     * 標籤
     */
    private String postTag;
    /**
     * 關鍵字
     */
    private String keyWord;
    /**
     * 創建時間
     */
    private String createTime;
    /**
     * 創建人
     */
    private String createBy;
    /**
     * 貼文狀態 0審核中 1審核通過 2審核不通過
     */
    private int isPublish;
    /**
     * 是否刪除 1刪除 0上線
     */
    private int isDelete;
    /**
     * 有效期
     */
    private String expiresDate;
    /**
     * 預算價格
     */
    private BigDecimal budgetPrice;
    /**
     * 查看數
     */
    private int postView;
    /**
     * 點讚
     */
    private int postLike;
    /**
     * 踩數
     */
    private int postUnlike;
    /**
     * 收藏數
     */
    private int postFavor;
    /**
     * 回復個數
     */
    private int postReply;
    /**
     * 分享數
     */
    private int postShare;
    /**
     * 审核失败原因
     */
    private String approveReason;
    /**
     * 評論列表
     */
    private List<WantCommentVo> comments;
    /**
     * 贴文图片集合
     */
    private List<WantPostImageVo> images;

    //以下屬性不存在於搜索引擎
    /**
     * 點讚狀態
     */
    private int isLike = 0;

    /**
     * 會員信息
     */
    private MemberVo memberVo;

    public BigInteger getPostId() {
        return postId;
    }

    public void setPostId(BigInteger postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public String getPostTag() {
        return postTag;
    }

    public void setPostTag(String postTag) {
        this.postTag = postTag;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public int getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(int isPublish) {
        this.isPublish = isPublish;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(String expiresDate) {
        this.expiresDate = expiresDate;
    }

    public BigDecimal getBudgetPrice() {
        return budgetPrice;
    }

    public void setBudgetPrice(BigDecimal budgetPrice) {
        this.budgetPrice = budgetPrice;
    }

    public int getPostView() {
        return postView;
    }

    public void setPostView(int postView) {
        this.postView = postView;
    }

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public int getPostUnlike() {
        return postUnlike;
    }

    public void setPostUnlike(int postUnlike) {
        this.postUnlike = postUnlike;
    }

    public int getPostFavor() {
        return postFavor;
    }

    public void setPostFavor(int postFavor) {
        this.postFavor = postFavor;
    }

    public int getPostReply() {
        return postReply;
    }

    public void setPostReply(int postReply) {
        this.postReply = postReply;
    }

    public int getPostShare() {
        return postShare;
    }

    public void setPostShare(int postShare) {
        this.postShare = postShare;
    }

    public String getApproveReason() {
        return approveReason;
    }

    public void setApproveReason(String approveReason) {
        this.approveReason = approveReason;
    }

    public List<WantCommentVo> getComments() {
        return comments;
    }

    public void setComments(List<WantCommentVo> comments) {
        this.comments = comments;
    }

    public List<WantPostImageVo> getImages() {
        return images;
    }

    public void setImages(List<WantPostImageVo> images) {
        this.images = images;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public MemberVo getMemberVo() {
        return memberVo;
    }

    public void setMemberVo(MemberVo memberVo) {
        this.memberVo = memberVo;
    }

    @Override
    public String toString() {
        return "SearchWantPostVo{" +
                "postId=" + postId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", postCategory='" + postCategory + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", postType=" + postType +
                ", postTag='" + postTag + '\'' +
                ", keyWord='" + keyWord + '\'' +
                ", createTime='" + createTime + '\'' +
                ", createBy='" + createBy + '\'' +
                ", isPublish=" + isPublish +
                ", isDelete=" + isDelete +
                ", expiresDate='" + expiresDate + '\'' +
                ", budgetPrice=" + budgetPrice +
                ", postView=" + postView +
                ", postLike=" + postLike +
                ", postUnlike=" + postUnlike +
                ", postFavor=" + postFavor +
                ", postReply=" + postReply +
                ", postShare=" + postShare +
                ", approveReason='" + approveReason + '\'' +
                ", comments=" + comments +
                ", images=" + images +
                '}';
    }
}
