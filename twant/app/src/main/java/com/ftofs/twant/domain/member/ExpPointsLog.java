package com.ftofs.twant.domain.member;

import java.io.Serializable;

public class ExpPointsLog implements Serializable {
    /**
     * 自增编码
     */
    private int logId;

    /**
     * 会员编码
     */
    private int memberId = 0;

    /**
     * 真實值
     */
    private int points = 0;

    /**
     * 添加时间
     */
    private String addTime;

    /**
     * 操作描述
     */
    private String description = "";

    /**
     * 操作阶段
     */
    private String operationStage = "";

    /**
     * 操作阶段文本
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public String getOperationStage() {
        return operationStage;
    }

    public void setOperationStage(String operationStage) {
        this.operationStage = operationStage;
    }

    public String getOperationStageText() {
        return operationStageText;
    }

    @Override
    public String toString() {
        return "ExpPointsLog{" +
                "logId=" + logId +
                ", memberId=" + memberId +
                ", points=" + points +
                ", addTime=" + addTime +
                ", description='" + description + '\'' +
                ", operationStage='" + operationStage + '\'' +
                ", operationStageText='" + operationStageText + '\'' +
                '}';
    }
}
