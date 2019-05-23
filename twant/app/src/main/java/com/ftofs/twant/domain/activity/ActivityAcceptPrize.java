package com.ftofs.twant.domain.activity;

public class ActivityAcceptPrize {
    private int acceptPrizeId;
    /**
     * 活动id
     */
    private int activityId;

    /**
     * 活动类型
     * 0: 砸蛋
     */
    private int activityType;

    /**
     * 消费的积分数量
     */
    private int takePoints;

    /**
     * 联系电话
     */
    private String acceptPrizePhone;

    /**
     * 姓名
     */
    private String acceptPrizeName;

    /**
     * 地址
     */
    private String acceptPrizeAddress;

    /**
     * 会员id
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 会员头像
     */
    private String memberAvatar;

    /**
     * 会员头像地址
     */
    private String memberAvatarUrl;

    /**
     * 中奖状态
     * 0. 未中奖 1.已中奖
     */
    private int state = 0;

    /**
     * 认领状态
     * 0.未领取  1.已领取
     */
    private int acceptState = 0;

    /**
     * 发放状态
     */
    private int giveOutState = 0;

    /**
     * 获奖时间
     */
    private String giveOutTime;

    /**
     * 奖品类型
     */
    private String awardsType;

    /**
     * 奖项 id
     */
    private int awardsId;

    /**
     * 奖项名称
     */
    private String awardsName;

    /**
     * 奖项内容
     */
    private String awardsContent;

    /**
     * 获奖时间
     */
    private String addTime;

    /**
     *  快递公司名称
     */
    private String shipName;

    /**
     * 快递单号
     */
    private String shipSn;

    /**
     * 奖品的json信息
     * @return
     */
    private String prizeJson;

    /**
     * 活动类型
     */
    private String activityTypeText = "";

    /**
     * 奖品类型
     */
    private String awardsTypeText = "";

    /**
     * 认领状态文字
     */
    private String acceptStateText = "";

    /**
     * 发放状态文字
     */
    private String giveOutStateText = "";

    public int getTakePoints() {
        return takePoints;
    }

    public void setTakePoints(int takePoints) {
        this.takePoints = takePoints;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getAcceptPrizeId() {
        return acceptPrizeId;
    }

    public void setAcceptPrizeId(int acceptPrizeId) {
        this.acceptPrizeId = acceptPrizeId;
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

    public String getAwardsType() {
        return awardsType;
    }

    public void setAwardsType(String awardsType) {
        this.awardsType = awardsType;
    }

    public int getAwardsId() {
        return awardsId;
    }

    public void setAwardsId(int awardsId) {
        this.awardsId = awardsId;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAcceptState() {
        return acceptState;
    }

    public void setAcceptState(int acceptState) {
        this.acceptState = acceptState;
    }


    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public String getAcceptPrizePhone() {
        return acceptPrizePhone;
    }

    public void setAcceptPrizePhone(String acceptPrizePhone) {
        this.acceptPrizePhone = acceptPrizePhone;
    }

    public String getAcceptPrizeName() {
        return acceptPrizeName;
    }

    public void setAcceptPrizeName(String acceptPrizeName) {
        this.acceptPrizeName = acceptPrizeName;
    }

    public String getAcceptPrizeAddress() {
        return acceptPrizeAddress;
    }

    public void setAcceptPrizeAddress(String acceptPrizeAddress) {
        this.acceptPrizeAddress = acceptPrizeAddress;
    }

    public String getMemberAvatarUrl() {
        return memberAvatarUrl;
    }

    public void setMemberAvatarUrl(String memberAvatarUrl) {
        this.memberAvatarUrl = memberAvatarUrl;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public String getPrizeJson() {
        return prizeJson;
    }

    public void setPrizeJson(String prizeJson) {
        this.prizeJson = prizeJson;
    }

    public int getGiveOutState() {
        return giveOutState;
    }

    public void setGiveOutState(int giveOutState) {
        this.giveOutState = giveOutState;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipSn() {
        return shipSn;
    }

    public void setShipSn(String shipSn) {
        this.shipSn = shipSn;
    }

    public String getGiveOutTime() {
        return giveOutTime;
    }

    public void setGiveOutTime(String giveOutTime) {
        this.giveOutTime = giveOutTime;
    }

    public String getActivityTypeText() {
        return activityTypeText ;
    }

    public void setActivityTypeText(String activityTypeText) {
        this.activityTypeText = activityTypeText;
    }

    public void setAwardsTypeText(String awardsTypeText) {
        this.awardsTypeText = awardsTypeText;
    }

    public void setAcceptStateText(String acceptStateText) {
        this.acceptStateText = acceptStateText;
    }

    public void setGiveOutStateText(String giveOutStateText) {
        this.giveOutStateText = giveOutStateText;
    }

    public String getAwardsTypeText() {
        return  awardsTypeText;
    }

    public String getAcceptStateText() {
        return acceptStateText;
    }

    public String getGiveOutStateText() {
        return giveOutStateText;
    }

    @Override
    public String toString() {
        return "ActivityAcceptPrize{" +
                "acceptPrizeId=" + acceptPrizeId +
                ", activityId=" + activityId +
                ", activityType=" + activityType +
                ", takePoints=" + takePoints +
                ", acceptPrizePhone='" + acceptPrizePhone + '\'' +
                ", acceptPrizeName='" + acceptPrizeName + '\'' +
                ", acceptPrizeAddress='" + acceptPrizeAddress + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", memberAvatar='" + memberAvatar + '\'' +
                ", memberAvatarUrl='" + memberAvatarUrl + '\'' +
                ", state=" + state +
                ", acceptState=" + acceptState +
                ", giveOutState=" + giveOutState +
                ", giveOutTime=" + giveOutTime +
                ", awardsType='" + awardsType + '\'' +
                ", awardsId=" + awardsId +
                ", awardsName='" + awardsName + '\'' +
                ", awardsContent='" + awardsContent + '\'' +
                ", addTime=" + addTime +
                ", shipName='" + shipName + '\'' +
                ", shipSn='" + shipSn + '\'' +
                ", prizeJson='" + prizeJson + '\'' +
                ", activityTypeText='" + activityTypeText + '\'' +
                ", awardsTypeText='" + awardsTypeText + '\'' +
                ", acceptStateText='" + acceptStateText + '\'' +
                ", giveOutStateText='" + giveOutStateText + '\'' +
                '}';
    }
}
