package com.ftofs.twant.vo.member;

import java.util.List;

/**
 * @Description: 進店商店記錄視圖對象
 * @Auther: yangjian
 * @Date: 2018/10/22 16:11
 */
public class MemberAccessStatVo {
    /**
     * 在店城友集合
     */
    private List<MemberVo> members;
    /**
     * 在店好友列表
     */
    private List<MemberVo> friends;
    /**
     * 商店Id
     */
    private int storeId;
    /**
     * 在店城友數量
     */
    private int onlineCount;
    /**
     * 已購買城友數量
     */
    private int purchasedCount;
    /**
     * 總訪問城友人數
     */
    private int allVisitCount;
    /**
     * 进店游客总数
     */
    private int visitorCount;

    public List<MemberVo> getMembers() {
        return members;
    }

    public void setMembers(List<MemberVo> members) {
        this.members = members;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public int getPurchasedCount() {
        return purchasedCount;
    }

    public void setPurchasedCount(int purchasedCount) {
        this.purchasedCount = purchasedCount;
    }

    public int getAllVisitCount() {
        return allVisitCount;
    }

    public void setAllVisitCount(int allVisitCount) {
        this.allVisitCount = allVisitCount;
    }

    public List<MemberVo> getFriends() {
        return friends;
    }

    public void setFriends(List<MemberVo> friends) {
        this.friends = friends;
    }

    public int getVisitorCount() {
        return visitorCount;
    }

    public void setVisitorCount(int visitorCount) {
        this.visitorCount = visitorCount;
    }

    @Override
    public String toString() {
        return "MemberAccessStatVo{" +
                "members=" + members +
                ", friends=" + friends +
                ", storeId=" + storeId +
                ", onlineCount=" + onlineCount +
                ", purchasedCount=" + purchasedCount +
                ", allVisitCount=" + allVisitCount +
                ", visitorCount=" + visitorCount +
                '}';
    }
}
