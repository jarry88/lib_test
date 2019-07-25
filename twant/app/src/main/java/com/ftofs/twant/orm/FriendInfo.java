package com.ftofs.twant.orm;

import org.litepal.crud.LitePalSupport;

/**
 * 好友資料
 * @author zwm
 */
public class FriendInfo extends LitePalSupport {
    public int userId;
    public int gender;
    public String memberName;
    public String nickname;
    public String avatarUrl;
    public byte[] avatarImg;
    public String memberSignature;
    public String memberBio;
    public long updateTime;  // 好友資料更新時間
}
