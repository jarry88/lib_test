package com.ftofs.twant.util;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import cn.snailpad.easyjson.EasyJSONObject;

public class ChatUtil {
    public static final int ROLE_MEMBER = 0;  // 會員
    public static final int ROLE_CS_AVAILABLE = 1; // 客服可用
    public static final int ROLE_CS_UNAVAILABLE = 2; // 客服不可用


    /**
     * 獲取或創建會話
     * @param yourMemberName
     * @param nickname
     * @param avatarUrl
     * @param role
     */
    public static EMConversation getConversation(String yourMemberName, String nickname, String avatarUrl, int role) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(yourMemberName, EMConversation.EMConversationType.Chat, true);

        // 會話的擴展信息
        String ext = EasyJSONObject.generate(
                "nickName", nickname,
                "avatarUrl", avatarUrl,
                "role", role
        ).toString();

        conversation.setExtField(ext);

        return conversation;
    }
}
