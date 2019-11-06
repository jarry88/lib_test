package com.ftofs.twant.entity;

import android.util.SizeF;

import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
import com.orhanobut.hawk.Hawk;

import java.lang.reflect.InvocationTargetException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 未讀消息數
 * @author zwm
 */
public class UnreadCount {
    public int transact;
    public int asset;
    public int social;
    public int bargain;
    public int notice;

    /**
     * 保存未讀消息數到Hawk中
     * @param unreadCount
     */
    public static void save(UnreadCount unreadCount) {
        try {
            String jsonStr = EasyJSONBase.jsonEncode(unreadCount);
            SLog.info("jsonStr[%s]", jsonStr);
            Hawk.put(SPField.FIELD_UNREAD_MESSAGE_COUNT, jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 從Hawk中獲取未讀消息數
     * @return
     */
    public static UnreadCount get() {
        String jsonStr = Hawk.get(SPField.FIELD_UNREAD_MESSAGE_COUNT, "");
        SLog.info("jsonStr[%s]", jsonStr);
        try {
            UnreadCount unreadCount = (UnreadCount) EasyJSONBase.jsonDecode(UnreadCount.class, jsonStr);
            return unreadCount;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 從服務端返回的列表處理未讀數據
     * @param unreadList
     * @return
     */
    public static UnreadCount processUnreadList(EasyJSONArray unreadList) {
        try {
            UnreadCount unreadCount = new UnreadCount();

            for (Object object : unreadList) {
                EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                int id = easyJSONObject.getInt("id");
                int messageUnreadCount = easyJSONObject.getInt("messageUnreadCount");

                if (id == Constant.TPL_CLASS_TRANSACT) {
                    unreadCount.transact = messageUnreadCount;
                } else if (id == Constant.TPL_CLASS_ASSET) {
                    unreadCount.asset = messageUnreadCount;
                } else if (id == Constant.TPL_CLASS_SOCIAL) {
                    unreadCount.social = messageUnreadCount;
                } else if (id == Constant.TPL_CLASS_BARGAIN) {
                    unreadCount.bargain = messageUnreadCount;
                } else if (id == Constant.TPL_CLASS_NOTICE) {
                    unreadCount.notice = messageUnreadCount;
                }
            }
            return unreadCount;
        } catch (Exception e) {
            return null;
        }
    }
}
