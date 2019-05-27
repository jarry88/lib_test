package com.ftofs.twant.vo.advertorial;

import com.ftofs.twant.domain.advertorial.AdvertorialAuthor;
import com.ftofs.twant.vo.goods.ArticleGoodsCommonVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * copyright  Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 推文vo
 *
 * @author cj
 * Created 2017-9-11 下午 4:19
 */
public class AdvertorialArticleVo {
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
     * 作者昵称
     */
    private String authorName;

    /**
     * 作者图像头像地址
     */
    private String authorAvatarUrl;

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
     * 分类名称
     */
    private String categoryName;
    /**
     * 文章状态
     */

    private int state;

    /**
     * 状态文字
     */
    private String stateText = "";

    /**
     * 内容
     */

    private String content;
    /**
     * 简介
     */

    private String summary ;

    /**
     * 管理员审核备注
     */

    private String remark;

    /**
     * 添加时间
     */

    private String addTime;

    /**
     *  处理时间
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
     * 關注数量
     */
    private int followNum ;
    /**
     * 推荐
     */
    private int recommend ;
    /**
     * 封面
     */
    private String bannerImageUrl = "";
    /**
     * 类型文字
     */
    private String typeText = "";

    /**
     * 包含的商品id
     */
    private String commonIdList = "";

    /**
     * 商品图片
     */
    private String commonImageList = "" ;

    /**
     * 商品列表 ,id与商品对应
     */
    private HashMap<Integer, ArticleGoodsCommonVo> goodsCommonHashMap = new HashMap<>();

    /**
     * 推广商品
     */
    private HashMap<Integer, Integer> distributorGoodsHashMap = new HashMap<>();

    /**
     * 作者相关信息
     */
    private AdvertorialAuthor advertorialAuthor ;

    /**
     * 图集的数据(type ==2 )
     */
    private List<AdvertorialArticleItemAlbumVo> albumContentJson = new ArrayList<>() ;

    /**
     * 长图文数据
     */
    private List<AdvertorialArticleItemVo> normalContentJson  = new ArrayList<>() ;
    /**
     * 文章评论总数
     */
    private int commentAmount ;

    /**
     * 作者发布的图集数量
     */
    private int authorAlbumNum  ;

    /**
     * 作者发布的长文数量
     */
    private int authorNormalNum  ;
    /**
     * 最后评论的时间
     */

    private String lastCommentTime;
    /**
     * 推文来源  推文来源  商家2、雇员3   会员1
     * Modify By yangjian 2018/12/22 14:26
     */
    private int articleFrom;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatarUrl() {
        return authorAvatarUrl;
    }

    public void setAuthorAvatarUrl(String authorAvatarUrl) {
        this.authorAvatarUrl = authorAvatarUrl;
    }

    public String getStateText() {
        return stateText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getBannerImageUrl() {
        return bannerImageUrl;
    }

    public void setBannerImageUrl(String bannerImageUrl) {
        this.bannerImageUrl = bannerImageUrl;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
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

    public HashMap<Integer, ArticleGoodsCommonVo> getGoodsCommonHashMap() {
        return goodsCommonHashMap;
    }

    public void setGoodsCommonHashMap(HashMap<Integer, ArticleGoodsCommonVo> goodsCommonHashMap) {
        this.goodsCommonHashMap = goodsCommonHashMap;
    }

    public HashMap<Integer, Integer> getDistributorGoodsHashMap() {
        return distributorGoodsHashMap;
    }

    public void setDistributorGoodsHashMap(HashMap<Integer, Integer> distributorGoodsHashMap) {
        this.distributorGoodsHashMap = distributorGoodsHashMap;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public AdvertorialAuthor getAdvertorialAuthor() {
        return advertorialAuthor;
    }

    public void setAdvertorialAuthor(AdvertorialAuthor advertorialAuthor) {
        this.advertorialAuthor = advertorialAuthor;
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

    public String getCommonImageList() {
        return commonImageList;
    }

    public void setCommonImageList(String commonImageList) {
        this.commonImageList = commonImageList;
    }


    public List<AdvertorialArticleItemAlbumVo> getAlbumContentJson() {
        return albumContentJson;
    }

    public void setAlbumContentJson(List<AdvertorialArticleItemAlbumVo> albumContentJson) {
        this.albumContentJson = albumContentJson;
    }

    public List<AdvertorialArticleItemVo> getNormalContentJson() {
        return normalContentJson;
    }

    public void setNormalContentJson(List<AdvertorialArticleItemVo> normalContentJson) {
        this.normalContentJson = normalContentJson;
    }

    public int getCommentAmount() {
        return commentAmount;
    }

    public void setCommentAmount(int commentAmount) {
        this.commentAmount = commentAmount;
    }

    public String getLastCommentTime() {
        return lastCommentTime;
    }

    public void setLastCommentTime(String lastCommentTime) {
        this.lastCommentTime = lastCommentTime;
    }

    public int getAuthorAlbumNum() {
        return authorAlbumNum;
    }

    public void setAuthorAlbumNum(int authorAlbumNum) {
        this.authorAlbumNum = authorAlbumNum;
    }

    public int getAuthorNormalNum() {
        return authorNormalNum;
    }

    public void setAuthorNormalNum(int authorNormalNum) {
        this.authorNormalNum = authorNormalNum;
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
        return "AdvertorialArticleVo{" +
                "articleId=" + articleId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", storeId=" + storeId +
                ", authorName='" + authorName + '\'' +
                ", authorAvatarUrl='" + authorAvatarUrl + '\'' +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", type=" + type +
                ", bannerImage='" + bannerImage + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", state=" + state +
                ", stateText='" + stateText + '\'' +
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
                ", recommend=" + recommend +
                ", bannerImageUrl='" + bannerImageUrl + '\'' +
                ", typeText='" + typeText + '\'' +
                ", commonIdList='" + commonIdList + '\'' +
                ", commonImageList='" + commonImageList + '\'' +
                ", goodsCommonHashMap=" + goodsCommonHashMap +
                ", distributorGoodsHashMap=" + distributorGoodsHashMap +
                ", advertorialAuthor=" + advertorialAuthor +
                ", albumContentJson=" + albumContentJson +
                ", normalContentJson=" + normalContentJson +
                ", commentAmount=" + commentAmount +
                ", authorAlbumNum=" + authorAlbumNum +
                ", authorNormalNum=" + authorNormalNum +
                ", lastCommentTime=" + lastCommentTime +
                ", articleFrom='" + articleFrom + '\'' +
                '}';
    }
}
