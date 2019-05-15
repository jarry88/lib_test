package com.ftofs.twant.domain.member;

import java.io.Serializable;
import java.sql.Timestamp;

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
     * 信賴值
     */
    private int points = 0;

    /**
     * 添加时间
     */
    private Timestamp addTime;

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

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
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
        if (operationStage.equals("login")) {
            operationStageText = "會員登錄";
        }else if(operationStage.equals("comments")){
            operationStageText = "商品評論";
        }else if(operationStage.equals("orders")){
            operationStageText = "訂單消費";
        }else if(operationStage.equals("register")){
            operationStageText = "會員注冊";
        }
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
