package com.ftofs.twant.task;

import android.content.Context;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.PathUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import org.litepal.LitePal;

import java.io.File;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 更新（或添加）好友信息的任務
 * @author zwm
 */
public class UpdateFriendInfoTask implements Runnable {
    String memberName;
    String token;

    /**
     * 構造方法
     * @param memberName 如果memberName為null，表示更新會話列表中所有好友的信息
     */
    public UpdateFriendInfoTask(String memberName) {
        this.memberName = memberName;
    }

    @Override
    public void run() {
        SLog.info("UpdateFriendInfoTask start...");
        token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        if (!StringUtil.isEmpty(memberName)) {  // 表示更新單個好友的信息
            updateFriendInfo(memberName);
            return;
        }

        // 獲取環信所有會話列表
        Map<String, EMConversation> conversationMap = EMClient.getInstance().chatManager().getAllConversations();
        SLog.info("會話數[%d]", conversationMap.size());
        for (Map.Entry<String, EMConversation> entry : conversationMap.entrySet()) {
            String memberName = entry.getKey();
            FriendInfo friendInfo = LitePal.where("memberName = ?", memberName).findFirst(FriendInfo.class);
            if (friendInfo == null) { // 好友資料為空才更新
                updateFriendInfo(memberName);
            }
        }
    }

    private void updateFriendInfo(String memberName) {
        SLog.info("updateFriendInfo[%s]", memberName);
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "friendMemberName", memberName);
        String responseStr = Api.syncPost(Api.PATH_FRIEND_INFO, params);
        SLog.info("responseStr[%s]", responseStr);

        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
        if (ToastUtil.isError(responseObj)) {
            return;
        }

        FriendInfo friendInfo = new FriendInfo();

        Context applicationContext = TwantApplication.getContext();

        try {
            EasyJSONObject memberInfo = responseObj.getObject("datas.memberInfo");
            friendInfo.userId = memberInfo.getInt("memberId");
            friendInfo.gender = memberInfo.getInt("memberSex");
            friendInfo.memberName = memberInfo.getString("memberName");
            friendInfo.nickname = memberInfo.getString("nickName");
            friendInfo.avatarUrl = memberInfo.getString("avatar");
            friendInfo.memberSignature = memberInfo.getString("memberSignature");
            friendInfo.memberBio = memberInfo.getString("memberBio");
            friendInfo.updateTime = System.currentTimeMillis();


            if (!StringUtil.isEmpty(friendInfo.avatarUrl)) {
                String filename = PathUtil.getFilename(friendInfo.avatarUrl);

                // 下載頭像
                File file = FileUtil.getCacheFile(applicationContext, filename);
                boolean isDownloadSuccess = Api.syncDownloadFile(StringUtil.normalizeImageUrl(friendInfo.avatarUrl), file);
                
                if (isDownloadSuccess) {
                    friendInfo.avatarImg = FileUtil.readFile(file);
                    SLog.info("avatarImg大小[%d]", friendInfo.avatarImg.length);
                }
            }


            friendInfo.save();
        } catch (EasyJSONException e) {
            e.printStackTrace();
            SLog.info("Error!%s", e.getMessage());
        }
    }
}
