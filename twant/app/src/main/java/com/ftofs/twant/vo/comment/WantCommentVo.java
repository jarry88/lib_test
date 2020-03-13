package com.ftofs.twant.vo.comment;

import com.ftofs.twant.domain.comment.WantCommentImage;
import com.ftofs.twant.vo.goods.goodsdetail.GoodsDetailVo;
import com.ftofs.twant.vo.member.MemberVo;
import com.ftofs.twant.vo.store.StoreVo;
import com.ftofs.twant.vo.wantpost.WantPostVo;

import java.math.BigInteger;

import java.util.List;

/**
 * @Description: 說說視圖類
 * @Auther: yangjian
 * @Date: 2019/2/13 16:22
 */
public class WantCommentVo {
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
     * 訂單編號
     */
    private int ordersId;
    /**
     * 訂單類型
     */
    private int ordersType;
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
     * 格式化說說時間
     */
    private String formatCreateTime;
    /**
     * 0-禁用,1-啟用
     */
    private int commentState;
    /**
     * 讚想說說數
     */
    private int commentLike = 0;
    /**
     * 踩該說說數
     */
    private int commentUnlike = 0;
    /**
     * 說說收藏數
     */
    private int commentFavor = 0;
    /**
     * 說說回覆數
     */
    private int commentReply = 0;
    /**
     * 說說分享數
     */
    private int commentShare = 0;
    /**
     * 是否讚想過
     */
    private int isLike = 0;
    /**
     * 說說時長
     */
    private String commentStartTime;
    /**
     * 說說图片集合
     */
    private List<WantCommentImage> images;
    /**
     * 评论会员详情
     */
    private MemberVo memberVo;
    /**
     * 回覆的說說
     */
    private WantCommentReplyVo wantCommentReplyVo;

    /**
     * 评论的贴文数据
     */
    private WantPostVo wantPostVo;
    /**
     * 评论的店铺数据
     */
    private StoreVo storeVo;
    /**
     * 评论的產品詳情数据
     */
    private GoodsDetailVo goodsDetailVo;
    /**
     * 說說者暱稱
     */
    private String nickName;
    /**
     * 想要帖標題
     */
    private String wantPostTitle;
    /**
     * 說說透明度 默认10显示
     */
    private int opacity = 10;

    /**
     * 最上級說說ID
     */
    private BigInteger parentCommentId;

    /**
     * 主體說說數
     */
    private long commentCount = 0;

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

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public List<WantCommentImage> getImages() {
        return images;
    }

    public void setImages(List<WantCommentImage> images) {
        this.images = images;
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

    public String getFormatCreateTime() {
        return formatCreateTime;
    }

    public void setFormatCreateTime(String formatCreateTime) {
        this.formatCreateTime = formatCreateTime;
    }

    public WantPostVo getWantPostVo() {
        return wantPostVo;
    }

    public void setWantPostVo(WantPostVo wantPostVo) {
        this.wantPostVo = wantPostVo;
    }

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }

    public GoodsDetailVo getGoodsDetailVo() {
        return goodsDetailVo;
    }

    public void setGoodsDetailVo(GoodsDetailVo goodsDetailVo) {
        this.goodsDetailVo = goodsDetailVo;
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

    public int getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
    }

    public BigInteger getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(BigInteger parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "WantCommentVo{" +
                "commentId=" + commentId +
                ", commentType=" + commentType +
                ", commentChannel=" + commentChannel +
                ", ordersId=" + ordersId +
                ", content='" + content + '\'' +
                ", deep=" + deep +
                ", relateStoreId=" + relateStoreId +
                ", relateCommonId=" + relateCommonId +
                ", relatePostId=" + relatePostId +
                ", replyCommentId=" + replyCommentId +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", formatCreateTime='" + formatCreateTime + '\'' +
                ", commentState=" + commentState +
                ", commentLike=" + commentLike +
                ", commentUnlike=" + commentUnlike +
                ", commentFavor=" + commentFavor +
                ", commentReply=" + commentReply +
                ", commentShare=" + commentShare +
                ", isLike=" + isLike +
                ", commentStartTime='" + commentStartTime + '\'' +
                ", images=" + images +
                ", memberVo=" + memberVo +
                ", wantCommentReplyVo=" + wantCommentReplyVo +
                ", wantPostVo=" + wantPostVo +
                ", storeVo=" + storeVo +
                ", goodsDetailVo=" + goodsDetailVo +
                ", nickName='" + nickName + '\'' +
                ", wantPostTitle='" + wantPostTitle + '\'' +
                ", opacity=" + opacity +
                '}';
    }
}
