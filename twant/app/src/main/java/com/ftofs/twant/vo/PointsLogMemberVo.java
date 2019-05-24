package com.ftofs.twant.vo;



/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 积分日志
 * 
 * @author zxy
 * Created 2017/4/13 11:00
 */
public class PointsLogMemberVo {
    /**
     * 自增编码
     */
    private int logId = 0;
    /**
     * 会员编码
     */
    private int memberId = 0;
    /**
     * 积分负数表示扣除，正数表示增加
     */
    private int points = 0;
    /**
     * 添加时间
     */
    private String addTime = "";
    /**
     * 操作描述
     */
    private String description = "";
    /**
     * 操作阶段
     */
    private String operationStage = "";
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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
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
}
