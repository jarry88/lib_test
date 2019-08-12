package com.ftofs.twant.orm;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * 好友資料(包括普通會員和店鋪客服)
 * @author zwm
 */
public class FriendInfo extends LitePalSupport {
    public String memberName;
    public String nickname;
    public String avatarUrl;
    public int role;
    public long updateTime;  // 好友資料更新時間

    public static FriendInfo getFriendInfoByMemberName(String memberName) {
        return LitePal.where("memberName = ?", memberName).findFirst(FriendInfo.class);
    }

    /**
     * 更新聊天的用戶信息
     * @param memberName
     * @param nickname
     * @param avatarUrl
     * @param role -1 表示不更新role字段
     */
    public static void upsertFriendInfo(String memberName, String nickname, String avatarUrl, int role) {
        FriendInfo friendInfo = getFriendInfoByMemberName(memberName);

        if (friendInfo == null) {
            friendInfo = new FriendInfo();
            friendInfo.memberName = memberName;
            friendInfo.role = -1;
        }

        friendInfo.nickname = nickname;
        friendInfo.avatarUrl = avatarUrl;
        if (role != -1) {
            friendInfo.role = role;
        }
        friendInfo.updateTime = System.currentTimeMillis();

        friendInfo.save();
    }

    public static void upsertFriendInfo(String memberName, String nickname, String avatarUrl) {
        upsertFriendInfo(memberName, nickname, avatarUrl, -1);
    }
}
