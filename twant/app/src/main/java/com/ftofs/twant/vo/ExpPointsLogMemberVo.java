package com.ftofs.twant.vo;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 真實值日志
 * 
 * @author hbj
 * Created 2017/4/13 16:15
 */
public class ExpPointsLogMemberVo {
    /**
     * 自增编码
     */
    private int logId;
    /**
     * 会员编码
     */
    private int memberId;
    /**
     * 真實值负数表示扣除，正数表示增加
     */
    private int points;
    /**
     * 添加时间
     */

    private String addTime;
    /**
     * 操作描述
     */
    private String description;
    /**
     * 操作阶段
     */
    private String operationStage;
    /**
     * 会员名称
     */
    private String memberName;

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

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
