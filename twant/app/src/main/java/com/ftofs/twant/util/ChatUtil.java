package com.ftofs.twant.util;

import android.util.Log;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.ChatFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.vo.member.MemberVo;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.json.JSONObject;

import cn.snailpad.easyjson.EasyJSONObject;

public class ChatUtil {
    public static final int ROLE_MEMBER = 0;  // 角色 0會員 1客服 2店主 2020/3/7
    public static final int ROLE_CS_AVAILABLE = 1; // 客服可用
    public static final int ROLE_CS_UNAVAILABLE = 2; // 客服不可用
    public static final int ROLE_CS_PLATFORM = 3; // 平台客服


    /**
     * 獲取或創建會話
     * @param yourMemberName 對方的memberName
     * @param nickname  對方的nickname
     * @param avatarUrl 對方的頭像Url
     * @param role  對方的role,這裏的role應該傳0會員，1客服，2店主
     */
    public static EMConversation getConversation(String yourMemberName, String nickname, String avatarUrl, int role) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(yourMemberName, EMConversation.EMConversationType.Chat, true);
        if (conversation == null) {
            SLog.info("Error!獲取或創建會話失敗, yourMemberName[%s], nickname[%s], avatarUrl[%s], role[%d]",
                    yourMemberName, nickname, avatarUrl, role);
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
            if (ChatFragment.CUSTOM_MESSAGE_TYPE_GOODS.equals(txtMessageType)) {
                return Constant.CHAT_MESSAGE_TYPE_GOODS;
            } else if (ChatFragment.CUSTOM_MESSAGE_TYPE_ORDERS.equals(txtMessageType)) {
                return Constant.CHAT_MESSAGE_TYPE_ORDER;
            } else if (ChatFragment.CUSTOM_MESSAGE_TYPE_ENC.equals(txtMessageType)) {
                return Constant.CHAT_MESSAGE_TYPE_ENC;
            } else { // 默認為文本消息
                return Constant.CHAT_MESSAGE_TYPE_TXT;
            }
        }

        return Constant.CHAT_MESSAGE_TYPE_UNKNOWN;
    }


    public static void setMessageCommonAttr(EMMessage message, String messageType,String em_push_content) {//原來的方法參數是role注意兼容
//        String nickname = User.getUserInfo(SPField.FIELD_NICKNAME, "");
//        String avatarUrl = User.getUserInfo(SPField.FIELD_AVATAR, "");
        MemberVo currMemberInfo = TwantApplication.getInstance().getMemberVo();
        SLog.info(currMemberInfo.toString());
        String avatar = currMemberInfo.getAvatarUrl();
        String nickname = currMemberInfo.getNickName();
        String head =nickname;
        if (currMemberInfo.role > 0) {
            avatar = currMemberInfo.storeAvatar;
            nickname = currMemberInfo.staffName;
            head = currMemberInfo.storeName + " " + nickname;
        }
        em_push_content = head + ": " + em_push_content;
        message.setAttribute("nickName", nickname);
        message.setAttribute("avatar", avatar);
        message.setAttribute("role", String.valueOf(currMemberInfo.role));
        message.setAttribute("sendTime", String.valueOf(System.currentTimeMillis()));
//        message.setAttribute("staffName", currMemberInfo.staffName);
//        message.setAttribute("storeAvatar", currMemberInfo.storeAvatar);
        message.setAttribute("storeName", currMemberInfo.storeName);
        message.setAttribute("storeId", currMemberInfo.getStoreId());
        //移動端同ios一致依據這個屬性顯示推送信息
        JSONObject em_apans_ext = new JSONObject();
        JSONObject extern = new JSONObject();


        try {
            extern.put("nickName",nickname);
            extern.put("messageType",messageType);
            extern.put("avatar", avatar);
            extern.put("role", String.valueOf(currMemberInfo.role));
//        extern.addProperty("staffName", currMemberInfo.staffName);
//        extern.addProperty("storeAvatar", currMemberInfo.storeAvatar);
            extern.put("storeName", currMemberInfo.storeName);
            extern.put("storeId", String.valueOf(currMemberInfo.getStoreId()));
            em_apans_ext.put("em_push_content",em_push_content);
            em_apans_ext.put("extern",extern);
            message.setAttribute("em_apns_ext", em_apans_ext);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public static boolean isFromCurrMemberName(String from) {
        MainActivity mainActivity =MainActivity.getInstance();
        if (mainActivity == null) {
            return false;
        }
        String currMemberName = mainActivity.getCurrChatMemberName();
        if(StringUtil.isEmpty(currMemberName)||StringUtil.isEmpty(from)){
            return false;
        } else  if (currMemberName.equals(from)){
            return true;
        }else {
            return false;
        }

    }

}
