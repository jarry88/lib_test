package com.ftofs.twant.util;

import android.content.Context;
import android.util.Log;

import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.fragment.AddPostFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.vo.member.MemberVo;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 調用公共接口方法工具類
 * @author gzp
 */
public class ApiUtil {
    /**
     * 調用刪除評論接口的公用方法
     * @param commentId 評論序號
     * @param updateUI 接口返回成功後需要執行的調用界面更新方法
     */
    public static void deleteComment(Context _mActivity, int commentId, SimpleCallback updateUI)  {
        try {
            String url = Api.PATH_COMMENT_REMOVE;
            EasyJSONObject params = EasyJSONObject.generate();
            params.set("token", User.getToken());
            params.set("commentId",commentId);
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try{
                        SLog.info("responseStr[%s]",responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                            return;
                        }
                        updateUI.onSimpleCall(responseObj);
                    }catch (Exception e){
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
            SLog.info("刪除了這條評論[%s]",commentId);
        }catch (Exception e){
            SLog.info("Error![%s]",e);
        }

    };
    /**
     * 查取會員身份信息的公用方法
     * @param memberName imName 會員id
     * @param updateUI 接口返回成功後需要執行的調用界面更新方法
     */
    public static void getImInfo(Context _mActivity, String memberName, SimpleCallback updateUI) {
        try {
            EasyJSONObject params = EasyJSONObject.generate();
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }
            if (StringUtil.isEmpty(memberName)) {
                //默認從hawk拿去
                memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, "");
            }
            params.set("token", token);
            params.set("imName", memberName);
            SLog.info("params[%s]",params.toString());
            String url = Api.PATH_IM_INFO;
            Api.getUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                            SLog.info("獲取身份信息失敗");
                            return;
                        }
                        EasyJSONObject imMemberInfo = responseObj.getObject("datas.imMemberInfo");
                        MemberVo memberVo = new MemberVo();
                        memberVo.setAvatarUrl(imMemberInfo.getSafeString("memberAvatar"));
                        memberVo.setNickName(imMemberInfo.getSafeString("nickName"));
                        memberVo.role = imMemberInfo.getInt("role");
                        memberVo.staffName = imMemberInfo.getSafeString("staffName");
                        memberVo.storeAvatar = imMemberInfo.getSafeString("storeAvatar");
                        memberVo.storeName = imMemberInfo.getSafeString("storeName");
                        memberVo.setMemberName(imMemberInfo.getSafeString("memberName"));
                        memberVo.setMemberId(imMemberInfo.getInt("memberId"));
                        memberVo.setStoreId(imMemberInfo.getInt("storeId"));
                        SLog.info("獲取[%s]信息,身份%d",memberVo.getNickName(),memberVo.role);
                        updateUI.onSimpleCall(memberVo);

                    } catch (Exception e) {
                        SLog.info("Error!不用处理，message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error![%s]", e);
        }
    }

    /**
     * 查取能否發送貼文
     * @param fromWeb imName 會員id
     */
    public static void addPost(Context context,boolean fromWeb) {
        addPost(context,fromWeb,null);

    }

    public static void addPost(Context context, boolean fromWeb, EasyJSONObject dataObj) {
        try {
            EasyJSONObject params = EasyJSONObject.generate();
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                Util.showLoginFragment();
                return;
            }
            params.set("token", User.getToken());
            String url = Api.PATH_WANT_POST_ISSUE_VALIDATE;
            Api.getUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(context, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if(ToastUtil.checkError(context, responseObj)){
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }
                    try{

                        SLog.info("responseStr[%s]",responseStr);
                        if (dataObj == null) {
                            Util.startFragment(AddPostFragment.newInstance(fromWeb));
                        } else {
                            Util.startFragment(AddPostFragment.newInstance(dataObj,fromWeb));
                        }
                    }catch (Exception e){
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        }catch (Exception e){
            SLog.info("Error![%s]",e);
        }
    }
}
