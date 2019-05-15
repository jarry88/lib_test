package com.ftofs.twant.vo.wantpost;

import com.ftofs.twant.domain.wantpost.WantPost;
import com.ftofs.twant.vo.comment.SearchCommentVo;
import com.ftofs.twant.vo.comment.WantCommentVo;
import com.ftofs.twant.vo.member.MemberVo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @Description: 贴文视图类
 * @Auther: yangjian
 * @Date: 2019/1/29 12:27
 */
public class WantPostVo {
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
     * 是否刪除 0 刪除 1上線
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
     *分享數
     */
    private int postShare;
    /**
     * 发布人信息
     */
    private MemberVo memberVo;
    /**
     * 是否收藏過
     */
    private int isFavor;
    /**
     * 是否點讚過
     */
    private int isLike;
    /**
     * 是否踩過
     */
    private int isUnlike;
    /**
     * 审核失败原因
     */
    private String approveReason;
    /**
     * 是否顯示評論查看更多
     */
    private int isMore;
    /**
     * 贴文图片集合
     */
    private List<WantPostImageVo> wantPostImages;
    /**
     * 貼文評論集合
     */
    private List<WantCommentVo> wantCommentVoList;

    /**
     * 評論列表
     */
    private List<SearchCommentVo> searchCommentVoList;

    public WantPostVo(){}

    public WantPostVo(WantPost w) {
        this.postId = w.getPostId();
        this.title = w.getTitle();
        this.content = w.getContent();
        this.postCategory = w.getPostCategory();
        this.coverImage = w.getCoverImage();
        this.postType = w.getPostType();
        this.postTag = w.getPostTag();
        this.keyWord = w.getKeyWord();
        this.createBy = w.getCreateBy();
        this.isPublish = w.getIsPublish();
        this.isDelete = w.getIsDelete();
        this.budgetPrice = w.getBudgetPrice();
        this.postView = w.getPostView();
        this.postLike = w.getPostLike();
        this.postUnlike = w.getPostUnlike();
        this.postFavor = w.getPostFavor();
        this.postReply = w.getPostReply();
        this.postShare = w.getPostShare();
        this.approveReason= w.getApproveReason();
    }

    public WantPostVo(SearchWantPostVo w) {
        this.postId = w.getPostId();
        this.title = w.getTitle();
        this.content = w.getContent();
        this.postCategory = w.getPostCategory();
        this.coverImage = w.getCoverImage();
        this.postType = w.getPostType();
        this.postTag = w.getPostTag();
        this.keyWord = w.getKeyWord();
        this.createBy = w.getCreateBy();
        this.isPublish = w.getIsPublish();
        this.isDelete = w.getIsDelete();
        this.expiresDate = w.getExpiresDate();
        this.budgetPrice = w.getBudgetPrice();
        this.postView = w.getPostView();
        this.postLike = w.getPostLike();
        this.postUnlike = w.getPostUnlike();
        this.postFavor = w.getPostFavor();
        this.postReply = w.getPostReply();
        this.postShare = w.getPostShare();
        this.approveReason = w.getApproveReason();
        this.wantPostImages = w.getImages();
        this.wantCommentVoList = w.getComments();
        this.createTime = w.getCreateTime();

    }

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

    public MemberVo getMemberVo() {
        return memberVo;
    }

    public void setMemberVo(MemberVo memberVo) {
        this.memberVo = memberVo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(String expiresDate) {
        this.expiresDate = expiresDate;
    }

    public List<WantPostImageVo> getWantPostImages() {
        return wantPostImages;
    }

    public void setWantPostImages(List<WantPostImageVo> wantPostImages) {
        this.wantPostImages = wantPostImages;
    }

    public int getIsFavor() {
        return isFavor;
    }

    public void setIsFavor(int isFavor) {
        this.isFavor = isFavor;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getIsUnlike() {
        return isUnlike;
    }

    public void setIsUnlike(int isUnlike) {
        this.isUnlike = isUnlike;
    }

    public List<WantCommentVo> getWantCommentVoList() {
        return wantCommentVoList;
    }

    public void setWantCommentVoList(List<WantCommentVo> wantCommentVoList) {
        this.wantCommentVoList = wantCommentVoList;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getApproveReason() {
        return approveReason;
    }

    public void setApproveReason(String approveReason) {
        this.approveReason = approveReason;
    }

    public int getIsMore() {
        return isMore;
    }

    public void setIsMore(int isMore) {
        this.isMore = isMore;
    }

    public List<SearchCommentVo> getSearchCommentVoList() {
        return searchCommentVoList;
    }

    public void setSearchCommentVoList(List<SearchCommentVo> searchCommentVoList) {
        this.searchCommentVoList = searchCommentVoList;
    }

    @Override
    public String toString() {
        return "WantPostVo{" +
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
                ", memberVo=" + memberVo +
                ", isFavor=" + isFavor +
                ", isLike=" + isLike +
                ", isUnlike=" + isUnlike +
                ", approveReason='" + approveReason + '\'' +
                ", isMore=" + isMore +
                ", wantPostImages=" + wantPostImages +
                ", wantCommentVoList=" + wantCommentVoList +
                '}';
    }
}
