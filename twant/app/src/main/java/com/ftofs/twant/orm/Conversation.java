package com.ftofs.twant.orm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GoodsInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.User;
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
     * @param conversationId
     * @param newEMMessageList       新得到的数据列表
     * @param lastMessage     最新信息
     */
    public static void upsertConversationInfo(String conversationId,String memberName, List<EMMessage> newEMMessageList,EMMessage lastMessage,String extField,int unreadMsgCount) {
        Conversation conversation = getByConversationId(conversationId);
        if (conversation == null) {
            conversation = new Conversation();
            conversation.conversationId = conversationId;
            conversation.allEMMessage = newEMMessageList;
        } else {
            for (int i = 0; i < conversation.allEMMessage.size(); i++) {
                SLog.info("第%d条的时间为%s",i,conversation.allEMMessage.get(i).getMsgTime());
            }
            conversation.allEMMessage.addAll(newEMMessageList);
        }
        conversation.unreadMsgCount += unreadMsgCount;
        conversation.memberName = memberName;
        conversation.lastMessage = lastMessage;
        conversation.extField = extField;
        conversation.explainExtField();
        conversation.explainLastMessage();
        if (conversation.save()) {
            SLog.info("保存会话消息成功:%s,last [%s],lastTime[%s]", conversation.nickname,conversation.getLastMessageText(),conversation.lastMessageTime);
        } else {
            SLog.info("保存会话消息失败:%s", conversation.nickname);
        }
    }

    private void explainLastMessage() {
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
        EasyJSONObject extFieldObj = EasyJSONObject.parse(extField);

        try {
            nickname = extFieldObj.getSafeString("nickName");
            avatarUrl = extFieldObj.getSafeString("avatarUrl");
            role = extFieldObj.getInt("role");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public String getLastMessageText() {
        return lastMessage.toString();
    }


    public static Conversation getByConversationId(String conversationId) {
        return LitePal.where("conversationId = ?", conversationId).findFirst(Conversation.class);
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
        return String.format("memberName[%s], nickname[%s], avatarUrl[%s], role[%s],lastMessage[%s],lastMessageTime[%s]",
                memberName, nickname, avatarUrl, role,lastMessageText,lastMessageTime);
    }
}