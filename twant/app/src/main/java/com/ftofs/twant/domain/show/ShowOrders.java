package com.ftofs.twant.domain.show;

import java.io.Serializable;


public class ShowOrders implements Serializable {
    /**
     * 晒baoid
     * 主键、自增
     */
    private Integer showOrdersId;

    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 标题文字
     */
    private String titleText;

    /**
     * 标题图片
     */
    private String titleImage;

    /**
     * 背景音乐 id
     */
    private int musicId = 0;

    /**
     * 音乐是否自动播放
     */
    private int musicAuto = 0;

    /**
     * 模版id
     */
    private int templateId = 0;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 状态
     * 1.正常 0.草稿
     */
    private int state = 0;

    /**
     * 推荐申请
     * 1.申请 0.没申请
     */
    private int recommendJoin = 0;

    /**
     * 推荐
     * 1.推荐 0.不推荐
     */
    private int recommend = 0;

    /**
     * 推荐时间
     */
    private String recommendTime;

    /**
     * 阅读量
     */
    private int readCount = 0;

    /**
     * 点赞数
     */
    private int zanCount = 0;

    /**
     * 标题图片完整url
     */
    private String titleImageSrc = "";


    public Integer getShowOrdersId() {
        return showOrdersId;
    }

    public void setShowOrdersId(Integer showOrdersId) {
        this.showOrdersId = showOrdersId;
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

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(String titleImage) {
        this.titleImage = titleImage;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }


    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public int getZanCount() {
        return zanCount;
    }

    public void setZanCount(int zanCount) {
        this.zanCount = zanCount;
    }

    public int getRecommendJoin() {
        return recommendJoin;
    }

    public void setRecommendJoin(int recommendJoin) {
        this.recommendJoin = recommendJoin;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getMusicAuto() {
        return musicAuto;
    }

    public void setMusicAuto(int musicAuto) {
        this.musicAuto = musicAuto;
    }

    public String getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(String recommendTime) {
        this.recommendTime = recommendTime;
    }

    public String getTitleImageSrc() {
        return titleImageSrc;
    }

    @Override
    public String toString() {
        return "ShowOrders{" +
                "showOrdersId=" + showOrdersId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", titleText='" + titleText + '\'' +
                ", titleImage='" + titleImage + '\'' +
                ", musicId=" + musicId +
                ", musicAuto=" + musicAuto +
                ", templateId=" + templateId +
                ", addTime=" + addTime +
                ", state=" + state +
                ", recommendJoin=" + recommendJoin +
                ", recommend=" + recommend +
                ", recommendTime=" + recommendTime +
                ", readCount=" + readCount +
                ", zanCount=" + zanCount +
                ", titleImageSrc='" + titleImageSrc + '\'' +
                '}';
    }
}
