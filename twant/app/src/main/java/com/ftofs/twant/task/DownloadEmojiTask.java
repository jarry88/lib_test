package com.ftofs.twant.task;

import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Emoji;
import com.ftofs.twant.orm.UserStatus;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.PathUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;

import org.litepal.LitePal;

import java.io.File;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

public class DownloadEmojiTask implements Runnable {
    @Override
    public void run() {
        SLog.info("DownloadEmojiTask begin...");

        try {
            UserStatus userStatus = LitePal.where("key = ?", String.valueOf(UserStatus.Key.KEY_EMOJI_VERSION.ordinal()))
                    .findFirst(UserStatus.class);

            String emojiVersion = "";
            if (userStatus != null) {
                emojiVersion = userStatus.value;
            }
            SLog.info("emojiVersion[%s]", emojiVersion);

            EasyJSONObject params = EasyJSONObject.generate("emojiVersions", emojiVersion);
            String responseStr = Api.syncGet(Api.PATH_GET_EMOJI_LIST, params);
            SLog.info("responseStr[%s]", responseStr);

            EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
            if (ToastUtil.isError(responseObj)) {
                return;
            }

            boolean isNeedUpdate = responseObj.getBoolean("datas.isNeedUpdate");
            if (!isNeedUpdate) {
                return;
            }

            String versions = responseObj.getString("datas.versions");

            // 先刪除所有舊的表情信息
            LitePal.deleteAll(Emoji.class);

            EasyJSONArray emojiList = responseObj.getArray("datas.emojiList");
            boolean allSuceess = true; // 所有表情是否保存成功
            for (Object object : emojiList) {
                EasyJSONObject emojiItem = (EasyJSONObject) object;


                // 下載表情圖片
                int emojiId = emojiItem.getInt("emojiId");
                String url = StringUtil.normalizeImageUrl(emojiItem.getString("emojiImage"));
                String ext = PathUtil.getExtension(url, true);

                String absolutePath = String.format("emojiList/%s/%d.%s", versions, emojiId, ext);
                File emojiFile = FileUtil.getCacheFile(TwantApplication.getContext(), absolutePath);
                boolean success = Api.syncDownloadFile(url, emojiFile);
                if (success) {
                    SLog.info("emoji absolutePath[%s]", emojiFile.getAbsolutePath());

                    Emoji emoji = new Emoji();
                    emoji.emojiId = emojiId;
                    emoji.emojiCode = emojiItem.getString("emojiCode");
                    emoji.emojiDesc = emojiItem.getString("emojiDesc");
                    emoji.emojiImage = emojiItem.getString("emojiImage");
                    emoji.sort = emojiItem.getInt("sort");
                    emoji.absolutePath = emojiFile.getAbsolutePath();
                    SLog.info("emoji.absolutePath[%s]", emoji.absolutePath);

                    emoji.save();
                } else {
                    allSuceess = false;
                }

                // 更新表情版本號
                if (allSuceess) {
                    if (userStatus == null) {
                        userStatus = new UserStatus();
                        userStatus.key = UserStatus.Key.KEY_EMOJI_VERSION.ordinal();
                    }
                    userStatus.value = versions;
                    userStatus.save();
                }
            }
        } catch (Exception e) {

        }
    }
}
