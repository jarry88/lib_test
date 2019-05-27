package com.ftofs.twant.domain.im;

public class FriendRequest {
    /**
     * 主鍵
     */
    private int id;

    /**
     * 申請發起方
     */
    private String fromMember;

    /**
     * 申請接收方
     */
    private String toMember;

    /**
     * 備注
     */
    private String notes;

    /**
     * 處理狀態 詳見 FriendRequestType
     */
    private int state;

    /**
     * 發起申請時間
     */
    private String createTime;

    /**
     * 處理時間
     */
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromMember() {
        return fromMember;
    }

    public void setFromMember(String fromMember) {
        this.fromMember = fromMember;
    }

    public String getToMember() {
        return toMember;
    }

    public void setToMember(String toMember) {
        this.toMember = toMember;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
