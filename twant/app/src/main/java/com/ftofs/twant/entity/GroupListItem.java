package com.ftofs.twant.entity;

public class GroupListItem {
    public int goId;  // 開團ID
    public long endTime;
    public String memberAvatar;
    public int requireNum; // 成團需要的人數
    public int joinedNum;  // 已入團的人數

    public GroupListItem(int goId, long endTime, String memberAvatar, int requireNum, int joinedNum) {
        this.goId = goId;
        this.endTime = endTime;
        this.memberAvatar = memberAvatar;
        this.requireNum = requireNum;
        this.joinedNum = joinedNum;
    }
}
