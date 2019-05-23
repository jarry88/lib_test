package com.ftofs.twant.domain.distribution;

import java.io.Serializable;
import java.math.BigDecimal;


public class DistributionCommissionLog implements Serializable {
    /**
     * 自增编码
     */
    private int logId;

    /**
     * 推广商ID
     */
    private int distributorId = 0;

    /**
     * 会员编号
     */
    private int memberId = 0;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 管理员编号
     */
    private int adminId = 0;

    /**
     * 管理员名称
     */
    private String adminName = "";

    /**
     * 操作阶段（PredepositLogOperationStage状态）
     */
    private String operationStage = "";

    /**
     * 可用金额变更
     */
    private BigDecimal availableAmount = BigDecimal.ZERO;

    /**
     * 冻结金额变更
     */
    private BigDecimal freezeAmount = BigDecimal.ZERO;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 描述
     */
    private String description = "";

    /**
     * 状态文本
     */
    private String operationStageText = "";

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
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

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getOperationStage() {
        return operationStage;
    }

    public void setOperationStage(String operationStage) {
        this.operationStage = operationStage;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(int distributorId) {
        this.distributorId = distributorId;
    }

    public String getOperationStageText() {
        return operationStageText;
    }

    @Override
    public String toString() {
        return "DistributionCommissionLog{" +
                "logId=" + logId +
                ", distributorId=" + distributorId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", operationStage='" + operationStage + '\'' +
                ", availableAmount=" + availableAmount +
                ", freezeAmount=" + freezeAmount +
                ", addTime=" + addTime +
                ", description='" + description + '\'' +
                ", operationStageText='" + operationStageText + '\'' +
                '}';
    }
}
