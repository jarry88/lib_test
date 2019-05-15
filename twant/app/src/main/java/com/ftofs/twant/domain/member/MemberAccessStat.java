package com.ftofs.twant.domain.member;

import java.io.Serializable;
import java.sql.Timestamp;

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
    private Timestamp activeTime;

    /**
     * 進店時間
     */
    private Timestamp accessTime;

    public MemberAccessStat(){}

    public MemberAccessStat(int id, String memberName, String ip,int storeId, Timestamp activeTime, Timestamp accessTime) {
        this.id = id;
        this.memberName = memberName;
        this.ip = ip;
        this.storeId = storeId;
        this.activeTime = activeTime;
        this.accessTime = accessTime;
    }

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

    public Timestamp getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Timestamp activeTime) {
        this.activeTime = activeTime;
    }

    public Timestamp getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Timestamp accessTime) {
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
