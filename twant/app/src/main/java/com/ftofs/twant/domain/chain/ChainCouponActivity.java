package com.ftofs.twant.domain.chain;

import java.io.Serializable;

public class ChainCouponActivity implements Serializable,Cloneable {
    /**
     * 自增编码
     */
    private int id;

    /**
     * 门店ID
     */
    private int chainId = 0;

    /**
     * 门店名称
     */
    private String chainName = "";

    /**
     * 地区内容
     */
    private String areaInfo = "";

    /**
     * 详细地址
     */
    private String address = "";

    /**
     * 平台券活动ID
     */
    private int activityId;

    /**
     * 门店可发放的平台券总数
     */
    private int chainAvailableNum = 0;

    /**
     * 门店已发放平台券数量
     */
    private int chainGiveoutNum = 0;

    /**
     * 可用状态 0已禁用 1可使用
     */
    private int chainAbleState = 0;

    /**
     * 可用状态文本
     */
    private String chainAbleStateText = "";

    /**
     * 门店剩余可发平台券数
     */
    private int chainRemainNum = 0;

    /**
     * 已领完状态 0未领完，1已领完
     */
    private int withoutState = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getChainAvailableNum() {
        return chainAvailableNum;
    }

    public void setChainAvailableNum(int chainAvailableNum) {
        this.chainAvailableNum = chainAvailableNum;
    }

    public int getChainGiveoutNum() {
        return chainGiveoutNum;
    }

    public void setChainGiveoutNum(int chainGiveoutNum) {
        this.chainGiveoutNum = chainGiveoutNum;
    }

    public int getChainAbleState() {
        return chainAbleState;
    }

    public void setChainAbleState(int chainAbleState) {
        this.chainAbleState = chainAbleState;
    }

    public String getChainAbleStateText() {
        return chainAbleStateText;
    }

    public void setChainAbleStateText(String chainAbleStateText) {
        this.chainAbleStateText = chainAbleStateText;
    }

    public int getChainRemainNum() {
        return chainRemainNum;
    }

    public void setChainRemainNum(int chainRemainNum) {
        this.chainRemainNum = chainRemainNum;
    }

    public int getWithoutState() {
        return withoutState;
    }

    public void setWithoutState(int withoutState) {
        this.withoutState = withoutState;
    }

    @Override
    public String toString() {
        return "ChainCouponActivity{" +
                "id=" + id +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", areaInfo='" + areaInfo + '\'' +
                ", address='" + address + '\'' +
                ", activityId=" + activityId +
                ", chainAvailableNum=" + chainAvailableNum +
                ", chainGiveoutNum=" + chainGiveoutNum +
                ", chainAbleState=" + chainAbleState +
                ", chainAbleStateText='" + chainAbleStateText + '\'' +
                ", chainRemainNum=" + chainRemainNum +
                ", withoutState=" + withoutState +
                '}';
    }
}
