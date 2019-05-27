package com.ftofs.twant.vo.advertorial;

import com.ftofs.twant.domain.advertorial.AdvertorialAuthor;

/**
 * copyright  Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 文章作者关注
 *
 * @author cj
 * Created 2017-9-25 下午 5:44
 */
public class AdvertorialSubscribeVo {
    /**
     * id
     */
    private int subscribeId;
    /**
     * 会员id
     */
    private int memberId;
    /**
     * 关注的作者id
     */
    private int authorId;
    /**
     * 关注的作者昵称
     */
    private String authorName ;

    /**
     * 添加时间用于排序
     */
    private String addTime;

    /**
     * 作者信息
     */
    private AdvertorialAuthor authorInfo ;

    /**
     * 该作者发布的最后一篇文章 标题
     */
    private  String lastArticleTitle;

    /**
     * 该作者发布的最后一篇文章 时间
     */
    private String lastArticleAddTime ;

    public int getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(int subscribeId) {
        this.subscribeId = subscribeId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public AdvertorialAuthor getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(AdvertorialAuthor authorInfo) {
        this.authorInfo = authorInfo;
    }

    public String getLastArticleTitle() {
        return lastArticleTitle;
    }

    public void setLastArticleTitle(String lastArticleTitle) {
        this.lastArticleTitle = lastArticleTitle;
    }

    public String getLastArticleAddTime() {
        return lastArticleAddTime;
    }

    public void setLastArticleAddTime(String lastArticleAddTime) {
        this.lastArticleAddTime = lastArticleAddTime;
    }

    @Override
    public String toString() {
        return "AdvertorialSubscribeVo{" +
                "subscribeId=" + subscribeId +
                ", memberId=" + memberId +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", addTime=" + addTime +
                ", authorInfo=" + authorInfo +
                ", lastArticleTitle='" + lastArticleTitle + '\'' +
                ", lastArticleAddTime=" + lastArticleAddTime +
                '}';
    }
}
