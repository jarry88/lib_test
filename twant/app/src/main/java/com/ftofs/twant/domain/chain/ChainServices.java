package com.ftofs.twant.domain.chain;

import java.io.Serializable;
import java.sql.Timestamp;

public class ChainServices implements Serializable,Cloneable {
    /**
     * 自增编码
     */
    private int servicesId;

    /**
     * 服务名称
     */
    private String servicesName = "";

    /**
     * 服务描述
     */
    private String servicesDescription = "";

    /**
     * 门店ID
     */
    private int chainId = 0;

    /**
     * 门店名称
     */
    private String chainName = "";

    /**
     * 添加时间
     */
    private Timestamp addTime;

    /**
     * 服务用状态 0已禁用 1可使用
     */
    private int ableState = 0;

    /**
     * 可用状态文本
     */
    private String ableStateText = "";

    public int getServicesId() {
        return servicesId;
    }

    public void setServicesId(int servicesId) {
        this.servicesId = servicesId;
    }

    public String getServicesName() {
        return servicesName;
    }

    public void setServicesName(String servicesName) {
        this.servicesName = servicesName;
    }

    public String getServicesDescription() {
        return servicesDescription;
    }

    public void setServicesDescription(String servicesDescription) {
        this.servicesDescription = servicesDescription;
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

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getAbleState() {
        return ableState;
    }

    public void setAbleState(int ableState) {
        this.ableState = ableState;
    }

    public String getAbleStateText() {
        if (ableState == 1) {
            ableStateText = "可使用";
        } else {
            ableStateText = "已禁用";
        }
        return ableStateText;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ChainServices{" +
                "servicesId=" + servicesId +
                ", servicesName='" + servicesName + '\'' +
                ", servicesDescription='" + servicesDescription + '\'' +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", addTime=" + addTime +
                ", ableState=" + ableState +
                '}';
    }
}
