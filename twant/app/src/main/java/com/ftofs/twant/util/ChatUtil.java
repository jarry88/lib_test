package com.ftofs.twant.util;

import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import cn.snailpad.easyjson.EasyJSONObject;

public class ChatUtil {
    public static final int ROLE_MEMBER = 0;  // 會員
    public static final int ROLE_CS_AVAILABLE = 1; // 客服可用
    public static final int ROLE_CS_UNAVAILABLE = 2; // 客服不可用


    /**
     * 獲取或創建會話
     * @param yourMemberName 對方的memberName
     * @param nickname  對方的nickname
     * @param avatarUrl 對方的頭像Url
     * @param role  對方的role
     */
    public static EMConversation getConversation(String yourMemberName, String nickname, String avatarUrl, int role) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(yourMemberName, EMConversation.EMConversationType.Chat, true);
        if (conversation == null) {
            SLog.info("Error!獲取或創建會話失敗");
            return null;
        }

        // 會話的擴展信息
        String ext = EasyJSONObject.generate(
                "nickName", nickname,
                "avatarUrl", avatarUrl,
                "role", role
        ).toString();

        conversation.setExtField(ext);

        return conversation;
    }


    /**
     * 獲取整數類型的消息類型
     * @param message
     * @return
     */
    public static int getIntMessageType(EMMessage message) {
        EMMessage.Type messageType = message.getType();
        if (messageType == EMMessage.Type.IMAGE) {
            return Constant.CHAT_MESSAGE_TYPE_IMAGE;
        } else if (messageType == EMMessage.Type.TXT) {
            // 拓展的文本消息類型
            String txtMessageType = message.getStringAttribute("messageType", "");
            if ("goods".equals(txtMessageType)) {
                return Constant.CHAT_MESSAGE_TYPE_GOODS;
            } else if ("orders".equals(txtMessageType)) {
                return Constant.CHAT_MESSAGE_TYPE_ORDER;
            } else { // 默認為文本消息
                return Constant.CHAT_MESSAGE_TYPE_TXT;
            }
        }

        return Constant.CHAT_MESSAGE_TYPE_UNKNOWN;
    }


    public static void setMessageCommonAttr(EMMessage message, int role) {
        String nickname = User.getUserInfo(SPField.FIELD_NICKNAME, "");
        String avatarUrl = User.getUserInfo(SPField.FIELD_AVATAR, "");

        message.setAttribute("nickName", nickname);
        message.setAttribute("avatar", avatarUrl);
        message.setAttribute("role", String.valueOf(role));
        message.setAttribute("sendTime", String.valueOf(System.currentTimeMillis()));
    }
}
