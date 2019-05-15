package com.ftofs.twant.domain.activity;

public class ActivityAwards {
    /**
     * 奖项编号
     * 主键、自增
     */
    private int awardsId ;

    /**
     * 活动id
     */
    private int activityId ;

    /**
     * 实际奖项id
     */
    private int awardsInfoId ;

    /**
     * 奖项的类型
     */
    private String awardsType ;

    /**
     * 奖项名称
     */
    private String awardsName ;

    /**
     * 奖项内容
     */
    private String awardsContent ;

    /**
     * 奖项的中奖概率
     */
    private Float awardsRate ;

    /**
     * 奖项的剩余数量
     */
    private int awardsCount ;

    /**
     * 该奖项发放数量
     */
    private int awardsGiveCount = 0;

    /**
     * 奖项状态
     */
    private int awardsState = 1;

    public int getAwardsId() {
        return awardsId;
    }

    public void setAwardsId(int awardsId) {
        this.awardsId = awardsId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getAwardsInfoId() {
        return awardsInfoId;
    }

    public void setAwardsInfoId(int awardsInfoId) {
        this.awardsInfoId = awardsInfoId;
    }

    public String getAwardsType() {
        return awardsType;
    }

    public void setAwardsType(String awardsType) {
        this.awardsType = awardsType;
    }

    public String getAwardsName() {
        return awardsName;
    }

    public void setAwardsName(String awardsName) {
        this.awardsName = awardsName;
    }

    public String getAwardsContent() {
        return awardsContent;
    }

    public void setAwardsContent(String awardsContent) {
        this.awardsContent = awardsContent;
    }

    public float getAwardsRate() {
        return awardsRate;
    }

    public void setAwardsRate(float awardsRate) {
        this.awardsRate = awardsRate;
    }

    public int getAwardsCount() {
        return awardsCount;
    }

    public void setAwardsCount(int awardsCount) {
        this.awardsCount = awardsCount;
    }

    public int getAwardsGiveCount() {
        return awardsGiveCount;
    }

    public void setAwardsGiveCount(int awardsGiveCount) {
        this.awardsGiveCount = awardsGiveCount;
    }

    public int getAwardsState() {
        return awardsState;
    }

    public void setAwardsState(int awardsState) {
        this.awardsState = awardsState;
    }

    @Override
    public String toString() {
        return "ActivityAwards{" +
                "awardsId=" + awardsId +
                ", activityId=" + activityId +
                ", awardsInfoId=" + awardsInfoId +
                ", awardsType='" + awardsType + '\'' +
                ", awardsName='" + awardsName + '\'' +
                ", awardsContent='" + awardsContent + '\'' +
                ", awardsRate=" + awardsRate +
                ", awardsCount=" + awardsCount +
                ", awardsGiveCount=" + awardsGiveCount +
                ", awardsState=" + awardsState +
                '}';
    }
}
