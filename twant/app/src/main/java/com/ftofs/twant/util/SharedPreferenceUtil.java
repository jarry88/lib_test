package com.ftofs.twant.util;

import android.util.Log;

import com.ftofs.lib_net.model.LoginInfo;
import com.ftofs.lib_net.model.MemberVo;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.LoginType;
import com.ftofs.twant.constant.SPField;
import com.gzp.lib_common.utils.SLog;
import com.orhanobut.hawk.Hawk;
import com.wzq.mvvmsmart.net.net_utils.MmkvUtils;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * Android SharedPreferenceUtil 相關的工具類
 * @author zwm
 */
public class SharedPreferenceUtil {
    /**
     * 保存注冊/登錄返回的用戶數據
     * @param loginType
     * @param responseObj
     */
    public static void saveUserInfo(LoginType loginType, EasyJSONObject responseObj) {
        try {
            EasyJSONObject memberVo = responseObj.getSafeObject("datas.memberVo");

            Hawk.put(SPField.FIELD_LOGIN_TYPE, loginType.name());
            if (loginType == LoginType.WEIXIN) {
                Hawk.put(SPField.FIELD_WX_BINDING_STATUS, responseObj.getInt("datas.isBind"));
            } else if (loginType == LoginType.FACEBOOK) {
                Hawk.put(SPField.FIELD_FB_BINDING_STATUS, Constant.TRUE_INT);
            }
            Hawk.put(SPField.FIELD_MEMBER_TOKEN, responseObj.getSafeString("datas.memberToken"));
            SLog.info("FIELD_USER_ID[%s]", responseObj.getSafeString("datas.memberToken"));
            Hawk.put(SPField.FIELD_USER_ID, responseObj.getInt("datas.memberId"));
            SLog.info("FIELD_USER_ID[%d]", responseObj.getInt("datas.memberId"));
            String token = responseObj.getSafeString("datas.token");
            Hawk.put(SPField.FIELD_TOKEN,token );
            MmkvUtils.putStringValue("accessToken",token);
            SLog.info("FIELD_TOKEN[%s]", responseObj.getSafeString("datas.token"));
            String nickName = "";
            String avatarUrl = "";
            if (responseObj.exists("datas.nickName")) {
                nickName = responseObj.getSafeString("datas.nickName");
            }
            if (memberVo.exists("avatar")) {
                avatarUrl = memberVo.getSafeString("avatar");
            }

            if (loginType == LoginType.MOBILE) {
                Hawk.put(SPField.FIELD_NICKNAME, nickName);
                Hawk.put(SPField.FIELD_AVATAR,avatarUrl );
            } else if (loginType == LoginType.WEIXIN) {
                Hawk.put(SPField.FIELD_NICKNAME,StringUtil.isEmpty(nickName)? responseObj.getSafeString("datas.weixinNickName"):nickName);
                Hawk.put(SPField.FIELD_AVATAR, StringUtil.isEmpty(avatarUrl)? responseObj.getSafeString("datas.weixinAvatarUrl"):avatarUrl);
            } else if (loginType == LoginType.FACEBOOK) {
                Hawk.put(SPField.FIELD_NICKNAME, StringUtil.isEmpty(nickName)? responseObj.getSafeString("datas.facebookNickName"):nickName);
                String facebookAvatarUrl = responseObj.getSafeString("datas.facebookAvatarUrl");
                // 去除facebookAvatarUrl前後的引號
                if (facebookAvatarUrl.startsWith("\"")) {
                    facebookAvatarUrl = facebookAvatarUrl.substring(1);
                }
                if (facebookAvatarUrl.endsWith("\"")) {
                    facebookAvatarUrl = facebookAvatarUrl.substring(0, facebookAvatarUrl.length() - 1);
                }

                SLog.info("facebookAvatarUrl[%s]", facebookAvatarUrl);
                Hawk.put(SPField.FIELD_AVATAR, StringUtil.isEmpty(avatarUrl)? facebookAvatarUrl:avatarUrl);
            }

            Hawk.put(SPField.FIELD_MEMBER_NAME, responseObj.getSafeString("datas.memberName"));
            SLog.info("FIELD_MEMBER_NAME[%s]", responseObj.getSafeString("datas.memberName"));
            if (responseObj.exists("datas.imToken")) {
                Hawk.put(SPField.FIELD_IM_TOKEN, responseObj.getSafeString("datas.imToken"));
            }
            Hawk.put(SPField.FIELD_LAST_LOGIN_TIME, Time.timestamp());  // 最近一次登錄時間
            SLog.info("FIELD_LAST_LOGIN_TIME[%d]", Time.timestamp());

            String fullMobile = memberVo.getSafeString("mobileAreaCode") + "," + memberVo.getSafeString("mobile");
            Hawk.put(SPField.FIELD_MOBILE, fullMobile);  // 區號+手機號
            Hawk.put(SPField.FIELD_MOBILE_ENCRYPT, memberVo.getSafeString("mobileEncrypt"));
            Hawk.put(SPField.FIELD_GENDER, memberVo.getInt("memberSex"));
            Hawk.put(SPField.FIELD_MEMBER_SIGNATURE, memberVo.getSafeString("memberSignature"));
            Hawk.put(SPField.FIELD_MEMBER_BIO, memberVo.getSafeString("memberBio"));
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
    public static void saveNewUserInfo(LoginType loginType, LoginInfo loginInfo) {
        try {

            Hawk.put(SPField.FIELD_LOGIN_TYPE, loginType.name());
            if (loginType == LoginType.WEIXIN) {
                Hawk.put(SPField.FIELD_WX_BINDING_STATUS, loginInfo.isBind());
            } else if (loginType == LoginType.FACEBOOK) {
                Hawk.put(SPField.FIELD_FB_BINDING_STATUS, Constant.TRUE_INT);
            }
            Hawk.put(SPField.FIELD_MEMBER_TOKEN, loginInfo.getMemberToken());
            SLog.info("FIELD_USER_Token[%s]", loginInfo.getMemberToken());
            Hawk.put(SPField.FIELD_USER_ID, loginInfo.getMemberId());
            SLog.info("FIELD_USER_ID[%d]", loginInfo.getMemberId());
            String token = loginInfo.getToken();
            Hawk.put(SPField.FIELD_TOKEN,token );
            MmkvUtils.putStringValue("accessToken",token);
            SLog.info("FIELD_TOKEN[%s]",loginInfo.getToken());
            String nickName = "";
            String avatarUrl = "";
            nickName = loginInfo.getNickName();
            MemberVo memberVo = loginInfo.getMemberVo();

            if (memberVo!= null) {
                avatarUrl = memberVo.getAvatar();
            }
            if (loginType == LoginType.MOBILE) {
                Hawk.put(SPField.FIELD_NICKNAME, nickName);
                Hawk.put(SPField.FIELD_AVATAR,avatarUrl );
            } else if (loginType == LoginType.WEIXIN) {
                Hawk.put(SPField.FIELD_NICKNAME,StringUtil.isEmpty(nickName)?loginInfo.getWeixinNickName():nickName);
                Hawk.put(SPField.FIELD_AVATAR, StringUtil.isEmpty(avatarUrl)?loginInfo.getWeixinAvatarUrl():avatarUrl);
            } else if (loginType == LoginType.FACEBOOK) {
                Hawk.put(SPField.FIELD_NICKNAME, StringUtil.isEmpty(nickName)? loginInfo.getFacebookNickName():nickName);
                String facebookAvatarUrl = loginInfo.getFacebookAvatarUrl();
                // 去除facebookAvatarUrl前後的引號
                if (facebookAvatarUrl.startsWith("\"")) {
                    facebookAvatarUrl = facebookAvatarUrl.substring(1);
                }
                if (facebookAvatarUrl.endsWith("\"")) {
                    facebookAvatarUrl = facebookAvatarUrl.substring(0, facebookAvatarUrl.length() - 1);
                }

                SLog.info("facebookAvatarUrl[%s]", facebookAvatarUrl);
                Hawk.put(SPField.FIELD_AVATAR, StringUtil.isEmpty(avatarUrl)? facebookAvatarUrl:avatarUrl);
            }

            Hawk.put(SPField.FIELD_MEMBER_NAME, loginInfo.getMemberName());
            SLog.info("FIELD_MEMBER_NAME[%s]", loginInfo.getMemberName());
                Hawk.put(SPField.FIELD_IM_TOKEN, loginInfo.getImToken());
            Hawk.put(SPField.FIELD_LAST_LOGIN_TIME, Time.timestamp());  // 最近一次登錄時間
            SLog.info("FIELD_LAST_LOGIN_TIME[%d]", Time.timestamp());
            if (memberVo != null) {

                String fullMobile = memberVo.getMobileAreaCode() + "," + memberVo.getMobile();
                Hawk.put(SPField.FIELD_MOBILE, fullMobile);  // 區號+手機號
                Hawk.put(SPField.FIELD_MOBILE_ENCRYPT, memberVo.getMobileEncrypt());
                Hawk.put(SPField.FIELD_GENDER, memberVo.getMemberSex());
                Hawk.put(SPField.FIELD_MEMBER_SIGNATURE, memberVo.getMemberSignature());
                Hawk.put(SPField.FIELD_MEMBER_BIO, memberVo.getMemberBio());
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
}
