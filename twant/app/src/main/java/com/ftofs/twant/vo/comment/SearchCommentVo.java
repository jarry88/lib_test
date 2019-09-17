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
 * @description 評論實體，用於建立搜索引擎索引
 */
public class SearchCommentVo {
    /**
     * 評論id
     */
    private BigInteger commentId;
    /**
     * 評論類型 1全部 2視頻 3文本
     */
    private int commentType;
    /**
     * 貼文渠道 1全部 2店鋪 3商品 4貼文 5推文
     */
    private int commentChannel;
    /**
     * 評論類容
     */
    private String content;
    /**
     * 層級，1-回復主題，2-回復評論
     */
    private int deep;
    /**
     * 關聯店鋪Id
     */
    private int relateStoreId;
    /**
     * 關聯商品SPU
     */
    private int relateCommonId;
    /**
     * 關聯文章Id
     */
    private BigInteger relatePostId;
    /**
     * 回復評論Id
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
     * 點讚評論數
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
     * 評論回復數
     */
    private int commentReply;
    /**
     * 評論分享數
     */
    private int commentShare;
    /**
     * 貼文標題
     */
    private String wantPostTitle;
    /**
     * 評論圖片
     */
    private List<WantCommentImage> images;

    //以下字段不存在于搜索引擎
    /**
     * 點讚狀態
     */
    private int isLike = 0;

    /**
     * 評論者信息
     */
    private MemberVo memberVo;

    /**
     * 評論時間描述
     */
    private String commentStartTime;

    /**
     * 回復的評論
     */
    private WantCommentReplyVo wantCommentReplyVo;

    /**
     * 評論透明度 默认10显示
     */
    private int opacity = 10;

    /**
     * 評論的商品信息
     */
    private GoodsCommonVo goodsCommonVo;

    /**
     * 評論的店鋪信息
     */
    private StoreVo storeVo;

    /**
     * 評論的貼文信息
     */
    private WantPostVo wantPostVo;

    /**
     * 評論月份
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
