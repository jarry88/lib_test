package com.ftofs.twant.vo.store;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Description: 點讚視圖對象
 * @Auther: yangjian
 * @Date: 2018/12/21 14:18
 */
public class StoreLikeVo implements Serializable {
    /**
     * 點讚id
     */
    private int id;
    /**
     * 會員id
     */
    private int memberId;
    /**
     * 店鋪id
     */
    private int storeId;
    /**
     * 點讚狀態
     */
    private int state;
    /**
     * 點想時間
     */
    private Timestamp createTime;
    /**
     * 會員名
     */
    private String memberName;
    /**
     * 暱稱
     */
    private String nickName;
    /**
     * 頭像
     */
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "StoreLikeVo{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", storeId=" + storeId +
                ", state=" + state +
                ", createTime=" + createTime +
                ", memberName='" + memberName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
