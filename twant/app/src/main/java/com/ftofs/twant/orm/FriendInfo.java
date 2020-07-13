package com.ftofs.twant.orm;

import androidx.annotation.NonNull;

import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.fragment.MessageFragment;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.StringUtil;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 好友資料(包括普通城友和商店客服)
 * @author zwm
 */
public class FriendInfo extends LitePalSupport {
    public String memberName;
    public String nickname;
    public String avatarUrl;
    public String storeAvatarUrl;
    public int role;//這裏的role 是好友非好友的角色，注意不要和會員客服角色混淆
    public int memberRole;//這裏為身份角色 0會員 1 客服 2店主
    public long updateTime;  // 好友資料更新時間
    public String groupName;
    public GoodsInfo goodsInfo;
    public String storeName;
    public int storeId;
    public String storeAvatar;

    public static FriendInfo getFriendInfoByMemberName(String memberName) {
        FriendInfo friendInfo =LitePal.where("memberName = ?", memberName).findFirst(FriendInfo.class);
        if (friendInfo == null) {
            friendInfo = new FriendInfo();
            friendInfo.memberName = memberName;
        }
        return friendInfo;
    }

    /**
     * 更新聊天的用戶信息
     * @param memberName
     * @param nickname
     * @param avatarUrl
     * @param role -1 表示不更新role字段
     */
    public static void upsertFriendInfo(String memberName, String nickname, String avatarUrl, int role) {
        upsertFriendInfo(memberName,nickname,avatarUrl,role,0,"",0);

    }


    public static void upsertFriendInfo(String memberName, String nickname, String avatarUrl) {
        upsertFriendInfo(memberName, nickname, avatarUrl, -1);
    }
    //4156即時通訊會話列表增加店鋪標識與客服暱稱 任務后，全面添加客服屬性，另外補充一個獨立于好友狀態的角色屬性

    /**
     *
     * @param memberName
     * @param nickname
     * @param avatarUrl
     * @param role       這裏是指好友狀態身份
     * @param memberRole  這裏是指商城身份
     * @param storeName
     * @param storeId
     */
    public static void upsertFriendInfo(String memberName, String nickname, String avatarUrl,int role,int memberRole,String storeName,int storeId) {
        FriendInfo friendInfo = getFriendInfoByMemberName(memberName);

        if (friendInfo == null) {
            friendInfo = new FriendInfo();
            friendInfo.memberName = memberName;
            friendInfo.role = -1;
            friendInfo.memberRole = 0;
        }

        friendInfo.nickname = nickname;
        friendInfo.avatarUrl = avatarUrl;
        friendInfo.memberRole = memberRole;
        friendInfo.storeId = storeId;
        friendInfo.storeName = storeName;
        if (role != -1) {
            friendInfo.role = role;
        }
        friendInfo.updateTime = System.currentTimeMillis();
        friendInfo.save();
    }

    public static FriendInfo newInstance(String memberName, String nickName, String avatar, int roleCsPlatform) {
        FriendInfo friendInfo = new FriendInfo();
        friendInfo.memberName = memberName;
        friendInfo.nickname = nickName;
        friendInfo.avatarUrl = avatar;
        friendInfo.role = roleCsPlatform;
        return friendInfo;
    }

    public static FriendInfo parse(EasyJSONObject conversation) throws Exception{
        FriendInfo friendInfo = new FriendInfo();
        friendInfo.memberName = conversation.getSafeString("memberName");
        friendInfo.nickname= conversation.getSafeString("nickName");
        if (StringUtil.isEmpty(friendInfo.nickname)) {
            return null;
        }
        friendInfo.avatarUrl = conversation.getSafeString("avatar");
        friendInfo.storeAvatarUrl = conversation.getSafeString("storeAvatar");
        friendInfo.storeName = conversation.getSafeString("storeName");

        friendInfo.role = conversation.getInt("role");
        friendInfo.storeId = conversation.getInt("storeId");
        if (friendInfo.storeId == MessageFragment.PLATFORM_CUSTOM_STORE_ID) {
            friendInfo.role = ChatUtil.ROLE_CS_PLATFORM;
        }
        return friendInfo;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("memberName[%s], nickname[%s],storeName[%s] avatarUrl[%s],storeAvatarUrl[%s], role[%s]",
                memberName, nickname, storeName,avatarUrl,storeAvatarUrl, role);
    }

    public String getRoleAvatar() {
        return role==0||role==3?avatarUrl: storeAvatarUrl;
    }

    public String getName() {
        switch (role) {
            case 0:
                return nickname;
            case 2: case 1:
                if (storeName == null) {
                    return nickname;
                }
                return storeName + " " + nickname;
            case 3:
                return nickname;
            default:
                return nickname;
        }
    }
}
