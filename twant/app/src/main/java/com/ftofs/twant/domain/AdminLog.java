package com.ftofs.twant.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class AdminLog implements Serializable {
    /**
     * 日志编号
     */
    private int logId;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 记录时间
     */
    private Timestamp createTime;

    /**
     * 管理员用户名
     */
    private String adminName;

    /**
     * 管理员编号
     */
    private int adminId;

    /**
     * 操作人IP
     */
    private String ip;

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "AdminLog{" +
                "logId=" + logId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", adminName='" + adminName + '\'' +
                ", adminId=" + adminId +
                ", ip='" + ip + '\'' +
                '}';
    }
}
