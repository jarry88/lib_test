package com.ftofs.twant.vo.comment;

import com.ftofs.twant.domain.comment.WantCommentImage;
import com.ftofs.twant.vo.goods.GoodsCommonVo;
import com.ftofs.twant.vo.member.MemberVo;
import com.ftofs.twant.vo.store.StoreVo;
import com.ftofs.twant.vo.wantpost.WantPostVo;

import java.math.BigInteger;

import java.util.List;

/**
 * @author liusf
 * @create 2019/3/27 11:55
 * @description 說說實體，用於建立搜索引擎索引
 */
public class SearchCommentVo {
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
     * 回覆說說Id
     */
    private BigInteger replyCommentId;
    /**
     * 說說人
     */
    private String createBy;
    /**
     * 說說時間
     */
    private String createTime;
    /**
     * 0-禁用,1-啟用
     */
    private int commentState;
    /**
     * 讚想說說數
     */
    private int commentLike;
    /**
     * 踩該說說數
     */
    private int commentUnlike;
    /**
     * 說說收藏數
     */
    private int commentFavor;
    /**
     * 說說回覆數
     */
    private int commentReply;
    /**
     * 說說分享數
     */
    private int commentShare;
    /**
     * 想要帖標題
     */
    private String wantPostTitle;
    /**
     * 說說圖片
     */
    private List<WantCommentImage> images;

    //以下字段不存在于搜索引擎
    /**
     * 讚想狀態
     */
    private int isLike = 0;

    /**
     * 說說者信息
     */
    private MemberVo memberVo;

    /**
     * 說說時間描述
     */
    private String commentStartTime;

    /**
     * 回覆的說說
     */
    private WantCommentReplyVo wantCommentReplyVo;

    /**
     * 說說透明度 默认10显示
     */
    private int opacity = 10;

    /**
     * 說說的產品信息
     */
    private GoodsCommonVo goodsCommonVo;

    /**
     * 說說的商店信息
     */
    private StoreVo storeVo;

    /**
     * 說說的想要帖信息
     */
    private WantPostVo wantPostVo;

    /**
     * 說說月份
     */
    private int month;

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

    public String getWantPostTitle() {
        return wantPostTitle;
    }

    public void setWantPostTitle(String wantPostTitle) {
        this.wantPostTitle = wantPostTitle;
    }

    public List<WantCommentImage> getImages() {
        return images;
    }

    public void setImages(List<WantCommentImage> images) {
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

    public String getCommentStartTime() {
        return commentStartTime;
    }

    public void setCommentStartTime(String commentStartTime) {
        this.commentStartTime = commentStartTime;
    }

    public WantCommentReplyVo getWantCommentReplyVo() {
        return wantCommentReplyVo;
    }

    public void setWantCommentReplyVo(WantCommentReplyVo wantCommentReplyVo) {
        this.wantCommentReplyVo = wantCommentReplyVo;
    }

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public GoodsCommonVo getGoodsCommonVo() {
        return goodsCommonVo;
    }

    public void setGoodsCommonVo(GoodsCommonVo goodsCommonVo) {
        this.goodsCommonVo = goodsCommonVo;
    }

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }

    public WantPostVo getWantPostVo() {
        return wantPostVo;
    }

    public void setWantPostVo(WantPostVo wantPostVo) {
        this.wantPostVo = wantPostVo;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
