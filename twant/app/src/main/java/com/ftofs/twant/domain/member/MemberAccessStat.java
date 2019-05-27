package com.ftofs.twant.domain.member;

import java.io.Serializable;

public class MemberAccessStat implements Serializable{
    /**
     * 記錄id
     */
    private int id;

    /**
     * 會員名稱（唯一）
     */
    private String memberName;

    /**
     * 游客ip
     */
    private String ip;

    /**
     * 店鋪Id
     */
    private int storeId;

    /**
     * 最後活動時間
     */
    private String activeTime;

    /**
     * 進店時間
     */
    private String accessTime;

    public MemberAccessStat(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "MemberAccessStat{" +
                "id=" + id +
                ", memberName='" + memberName + '\'' +
                ", ip='" + ip + '\'' +
                ", storeId=" + storeId +
                ", activeTime=" + activeTime +
                ", accessTime=" + accessTime +
                '}';
    }
}
