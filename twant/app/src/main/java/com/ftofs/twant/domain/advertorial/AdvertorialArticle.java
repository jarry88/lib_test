package com.ftofs.twant.domain.advertorial;

import java.io.Serializable;

public class AdvertorialArticle implements Serializable {
    /**
     * 编号
     */
    private int articleId;

    /**
     * 作者会员编号
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 店铺id
     */
    private int storeId;

    /**
     * 达人昵称
     */
    private String authorName;

    /**
     * 达人头像
     */
    private String authorAvatar;

    /**
     * 推文标题
     */
    private String title;

    /**
     * 推文副标题
     */
    private String subTitle;

    /**
     * 类型
     */
    private int type;

    /**
     * 封面图
     */
    private String bannerImage;

    /**
     * 分类id
     */
    private int categoryId;

    /**
     * 文章状态
     */
    private int state;

    /**
     * 内容
     */
    private String content;

    /**
     * 内容描述
     */
    private String summary;

    /**
     * 管理员审核备注
     */
    private String remark;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 管理员处理时间
     */
    private String handleTime;

    /**
     * 浏览次数
     */
    private int pageView;

    /**
     * 进商详数
     */
    private int enterDetailPageView;

    /**
     * 分享次数
     */
    private int shareNum;

    /**
     * 评论数量
     */
    private int commentNum;

    /**
     * 点赞数
     */
    private int likeNum;

    /**
     * 商品数量
     */
    private int goodsCount;

    /**
     * 關注次数
     */
    private int followNum;

    /**
     * 商品id列表","分割
     */
    private String commonIdList = "";

    /**
     * 推荐
     */
    private int recommend;

    /**
     * 推文来源  商家2、雇员3   会员1
     */
    private int articleFrom;

    /**
     * 封面
     */
    private String bannerImageUrl = "";

    /**
     * 状态文字
     */
    private String stateText = "";

    /**
     * 文章类型文字
     */
    private String typeText = "";

    /**
     * 头像路径
     */
    private String authorAvatarUrl = "";

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }


    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }


    public int getPageView() {
        return pageView;
    }

    public void setPageView(int pageView) {
        this.pageView = pageView;
    }

    public int getEnterDetailPageView() {
        return enterDetailPageView;
    }

    public void setEnterDetailPageView(int enterDetailPageView) {
        this.enterDetailPageView = enterDetailPageView;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getCommonIdList() {
        return commonIdList;
    }

    public void setCommonIdList(String commonIdList) {
        this.commonIdList = commonIdList;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public String getStateText() {
        return stateText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
    }


    public String getAuthorAvatarUrl() {
        return authorAvatarUrl;
    }

    public void setAuthorAvatarUrl(String authorAvatarUrl) {
        this.authorAvatarUrl = authorAvatarUrl;
    }

    public int getArticleFrom() {
        return articleFrom;
    }

    public void setArticleFrom(int articleFrom) {
        this.articleFrom = articleFrom;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "AdvertorialArticle{" +
                "articleId=" + articleId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", storeId=" + storeId +
                ", authorName='" + authorName + '\'' +
                ", authorAvatar='" + authorAvatar + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", type=" + type +
                ", bannerImage='" + bannerImage + '\'' +
                ", categoryId=" + categoryId +
                ", state=" + state +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", remark='" + remark + '\'' +
                ", addTime=" + addTime +
                ", handleTime=" + handleTime +
                ", pageView=" + pageView +
                ", enterDetailPageView=" + enterDetailPageView +
                ", shareNum=" + shareNum +
                ", commentNum=" + commentNum +
                ", likeNum=" + likeNum +
                ", goodsCount=" + goodsCount +
                ", followNum=" + followNum +
                ", commonIdList='" + commonIdList + '\'' +
                ", recommend=" + recommend +
                ", articleFrom='" + articleFrom + '\'' +
                ", bannerImageUrl='" + bannerImageUrl + '\'' +
                ", stateText='" + stateText + '\'' +
                ", typeText='" + typeText + '\'' +
                ", authorAvatarUrl='" + authorAvatarUrl + '\'' +
                '}';
    }
}
