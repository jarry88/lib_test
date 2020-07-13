package com.ftofs.twant.orm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ftofs.twant.entity.ChatConversation;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.vo.member.MemberVo;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.umeng.commonsdk.debug.E;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

import static com.ftofs.twant.orm.ImNameMap.getByImName;

public class Conversation extends LitePalSupport {
    public String conversationId;
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

    public EMMessage lastMessage;
    public String lastMessageText;
    public long lastMessageTime;
    public long timestamp;
    public String extField;
    public List<EMMessage> allEMMessage = new ArrayList<>();
    public int unreadMsgCount;
    public int lastMessageType;

    /**
     * 更新会话信息
     *
     * @param newEMMessageList       新得到的数据列表
     * @param lastMessage     最新信息
     */
    public static void upsertConversationInfo(String memberName, List<EMMessage> newEMMessageList,EMMessage lastMessage,String extField,int unreadMsgCount) {
        Conversation conversation = getByMemberName(memberName);

//        for (int i = 0; i < conversation.allEMMessage.size(); i++) {
//                SLog.info("第%d条的时间为%s",i,conversation.allEMMessage.get(i).getMsgTime());
//            }
        conversation.allEMMessage.addAll(newEMMessageList);
        conversation.unreadMsgCount = unreadMsgCount;
        conversation.memberName = memberName;
        if (!StringUtil.isEmpty(extField)) {
            conversation.extField = extField;
            conversation.explainExtField();
        }
        if (lastMessage != null) {
            conversation.lastMessage = lastMessage;
            conversation.explainLastMessage();
        }
        if (conversation.save()) {
            SLog.info("保存会话消息成功:%s,last [%s],lastTime[%s]", conversation.nickname,conversation.getLastMessageText(),conversation.lastMessageTime);
        } else {
            SLog.info("保存会话消息失败:%s", conversation.nickname);
        }
    }

    public static void upsertConversationByMemberVo(MemberVo member) {
        if (member == null) {
            return;
        }
        Conversation conversation = Conversation.getByMemberName(member.getMemberName());
        conversation.role = member.role;

        conversation.storeName = member.storeName;
        conversation.storeId = member.getStoreId();
        conversation.avatarUrl = member.getAvatar();
        conversation.nickname = member.getNickName();
        conversation.avatarUrl = member.getAvatar();
        conversation.storeAvatarUrl = member.storeAvatar;
        conversation.save();
    }

    public static void saveNewChat(ChatConversation newChat) {
        if (newChat == null) {
            return;
        }
        Conversation conversation1 = Conversation.getByMemberName(newChat.friendInfo.memberName);
        conversation1.nickname = newChat.friendInfo.nickname;
        conversation1.storeName = newChat.friendInfo.storeName;
        conversation1.avatarUrl = newChat.friendInfo.avatarUrl;
        conversation1.storeAvatarUrl = newChat.friendInfo.storeAvatar;
        conversation1.lastMessageText = newChat.lastMessage;
        conversation1.lastMessageType = newChat.lastMessageType;
        conversation1.storeId = newChat.friendInfo.storeId;
        conversation1.role = newChat.friendInfo.role;
        conversation1.timestamp = newChat.timestamp;
        conversation1.save();
    }

    public void explainLastMessage() {
        if (lastMessage == null) {
            return;
        }
        lastMessageText = lastMessage.getBody().toString();
        lastMessageTime = lastMessage.getMsgTime();
        lastMessageType = ChatUtil.getIntMessageType(lastMessage);
    }

    public static void clearUnreadMsgCount(String conversationId) {
        Conversation conversation = getByConversationId(conversationId);
        if (conversation != null) {
            conversation.unreadMsgCount = 0;
            conversation.save();
//            conversation.updateAll("conversationId=?",conversationId);
            SLog.info("unreadCount[%d]个, ", getByConversationId(conversationId).unreadMsgCount);
        }

    }
    private void explainExtField() {
        if (StringUtil.isEmpty(extField)) {
            return;
        }
        EasyJSONObject extFieldObj = EasyJSONObject.parse(extField);
        try {
            nickname = extFieldObj.getSafeString("nickName");
//            storeName = extFieldObj.getSafeString("storeName");
            avatarUrl = extFieldObj.getSafeString("avatarUrl");
            role = extFieldObj.getInt("role");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public String getLastMessageText() {
        if (lastMessage == null) {
            SLog.info("空指针异常");
            return "";

        } else {
            return lastMessage.getBody().toString();
        }
    }


    public static Conversation getByConversationId(String conversationId) {
        return LitePal.where("conversationId = ?", conversationId).findFirst(Conversation.class);
    }
    public static Conversation getByMemberName(String memberName) {
        Conversation conversation = LitePal.where("memberName = ?", memberName).findFirst(Conversation.class);
        if (conversation == null) {
            conversation = new Conversation();
            conversation.memberName = memberName;
        }
        return conversation;
    }

    public static List<Conversation> getAllConversations() {
        //怎么分库
        return LitePal.findAll(Conversation.class);
    }



    public static FriendInfo newInstance(String memberName, String nickName, String avatar, int roleCsPlatform) {
        FriendInfo friendInfo = new FriendInfo();
        friendInfo.memberName = memberName;
        friendInfo.nickname = nickName;
        friendInfo.avatarUrl = avatar;
        friendInfo.role = roleCsPlatform;
        return friendInfo;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("memberName[%s], nickname[%s],storeName[%s] avatarUrl[%s], role[%s],lastMessage[%s],lastMessageTime[%s]",
                memberName, nickname,storeName, avatarUrl, role,lastMessageText,lastMessageTime);
    }

    public boolean needUpdate() {
        if (StringUtil.isEmpty(nickname)) {
            return true;
        }
        return false;
    }

    public FriendInfo toFriendInfo() {
        FriendInfo friendInfo = new FriendInfo();
        friendInfo.nickname = nickname;
        friendInfo.memberName = memberName;
        friendInfo.avatarUrl = avatarUrl;
        friendInfo.storeAvatar = storeAvatarUrl;
        friendInfo.storeName = storeName;
        friendInfo.storeAvatarUrl = storeAvatarUrl;
        friendInfo.role = role;
        friendInfo.storeName = storeName;
        return friendInfo;
    }
}