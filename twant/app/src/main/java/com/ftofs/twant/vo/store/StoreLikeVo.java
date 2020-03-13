package com.ftofs.twant.vo.store;

import java.io.Serializable;

/**
 * @Description: 讚想視圖對象
 * @Auther: yangjian
 * @Date: 2018/12/21 14:18
 */
public class StoreLikeVo implements Serializable {
    /**
     * 讚想id
     */
    private int id;
    /**
     * 城友id
     */
    private int memberId;
    /**
     * 商店id
     */
    private int storeId;
    /**
     * 讚想狀態
     */
    private int state;
    /**
     * 點想時間
     */
    private String createTime;
    /**
     * 城友名
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
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
