package com.ftofs.twant.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.kyleduo.switchbutton.SwitchButton;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 設置Fragment
 * @author zwm
 */
public class SecuritySettingFragment extends BaseFragment implements View.OnClickListener {
    SwitchButton sbBindWeixin;
    SwitchButton sbBindFacebook;
    int wxBindingStatus;
    int fbBindingStatus;

    public static final int SNS_TYPE_WEIXIN = 1;
    public static final int SNS_TYPE_FACEBOOK = 2;

    public static final int ACTION_TYPE_BIND = 1;
    public static final int ACTION_TYPE_UNBIND = 2;

    int fbAction;    // 用於區別Facebook綁定還是解綁

    CallbackManager callbackManager;
    List<String> permissions;  // Facebook 權限

    public static SecuritySettingFragment newInstance() {
        Bundle args = new Bundle();

        SecuritySettingFragment fragment = new SecuritySettingFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_security_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        permissions = new ArrayList<>();
        permissions.add("email");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_login_password, this);
        Util.setOnClickListener(view, R.id.btn_change_mobile, this);
        Util.setOnClickListener(view, R.id.btn_payment_password, this);

        Util.setOnClickListener(view, R.id.btn_logout, this);

        String mobileEncrypt = Hawk.get(SPField.FIELD_MOBILE_ENCRYPT, "");
        TextView tvMobile = view.findViewById(R.id.tv_mobile);
        tvMobile.setText(mobileEncrypt);
        sbBindWeixin = view.findViewById(R.id.sb_bind_weixin);
        sbBindFacebook = view.findViewById(R.id.sb_bind_facebook);
        wxBindingStatus = Hawk.get(SPField.FIELD_WX_BINDING_STATUS, 0);
        fbBindingStatus = Hawk.get(SPField.FIELD_WX_BINDING_STATUS, 0);
        loadBindStatus();//請求接口更新綁定狀態

        if (TwantApplication.wxApi.isWXAppInstalled()) {  // 如果微信已經安裝，則顯示綁定設置
            view.findViewById(R.id.btn_wx_login_setting).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.btn_wx_login_setting).setVisibility(View.GONE);
        }

        sbBindWeixin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SLog.info("onCheckedChanged, isChecked[%s]", isChecked);
                if (isChecked) {
                    SLog.info("綁定微信");
                    // 綁定微信
                    String token = User.getToken();
                    if (StringUtil.isEmpty(token)) {
                        return;
                    }

                    ((MainActivity) _mActivity).doWeixinLogin(Constant.WEIXIN_AUTH_USAGE_BIND);
                } else {
                    SLog.info("解綁微信");
                    showUnbindConfirm(SNS_TYPE_WEIXIN);
                }
            }
        });

        sbBindFacebook.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SLog.info("onCheckedChanged, isChecked[%s]", isChecked);
                if (isChecked) {
                    SLog.info("綁定Facebook");
                    // 綁定Facebook
                    fbAction = ACTION_TYPE_BIND;
                    LoginManager.getInstance().logInWithReadPermissions(SecuritySettingFragment.this, permissions);
                } else {
                    SLog.info("解綁Facebook");
                    // 解綁Facebook
                    fbAction = ACTION_TYPE_UNBIND;
                    showUnbindConfirm(SNS_TYPE_FACEBOOK);
                }
            }
        });

        callbackManager = ((MainActivity) _mActivity).getCallbackManager();
        // 回调
        LoginManager.getInstance().registerCallback(((MainActivity) _mActivity).getCallbackManager(), new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                SLog.info("onSuccess");
                AccessToken accessToken = loginResult.getAccessToken();
                String token = accessToken.getToken();
                String userId = accessToken.getUserId();
                SLog.info("token[%s], userId[%s]", token, userId);

                if (fbAction == ACTION_TYPE_BIND) {
                    SLog.info("綁定Facebook");
                    bindFacebook(token, userId);
                } else {
                    SLog.info("解綁Facebook");
                    unbindFacebook(token, userId);
                }
            }

            @Override
            public void onCancel() {
                // App code
                SLog.info("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                ToastUtil.error(_mActivity, "登錄失敗");
                SLog.info("onError, exception[%s]", exception.toString());
            }
        });
    }

    /**
     * 顯示解綁的確認彈窗
     * @param snsType
     */
    private void showUnbindConfirm(int snsType) {
        String title = null;
        String content = null;

        if (snsType == SNS_TYPE_WEIXIN) {
            title = "是否解除微信綁定";
            content = "解除綁定後將無法使用微信登入";
        } else if (snsType == SNS_TYPE_FACEBOOK) {
            title = "是否解除Facebook綁定";
            content = "解除綁定後將無法使用Facebook登入";
        }

        new XPopup.Builder(_mActivity)
                .dismissOnBackPressed(false) // 按返回键是否关闭弹窗，默认为true
                .dismissOnTouchOutside(false) // 点击外部是否关闭弹窗，默认为true
                // 设置弹窗显示和隐藏的回调监听
                // .autoDismiss(false)
                .setPopupCallback(new XPopupCallback() {
                    @Override
                    public void onShow() {
                    }
                    @Override
                    public void onDismiss() {
                    }
                }).asCustom(new TwConfirmPopup(_mActivity, title, content, "確認", "取消", new OnConfirmCallback() {
            @Override
            public void onYes() {
                SLog.info("onYes");
                if (snsType == SNS_TYPE_WEIXIN) {
                    // 解綁微信
                    ((MainActivity) _mActivity).doWeixinLogin(Constant.WEIXIN_AUTH_USAGE_UNBIND);
                } else if (snsType == SNS_TYPE_FACEBOOK) {
                    // 解綁Facebook
                    LoginManager.getInstance().logInWithReadPermissions(SecuritySettingFragment.this, permissions);
                }
            }

            @Override
            public void onNo() {
                SLog.info("onNo");
                // 如果選No，恢復原先狀態
                if (snsType == SNS_TYPE_WEIXIN) {
                    sbBindWeixin.setCheckedNoEvent(true);
                } else {
                    sbBindFacebook.setCheckedNoEvent(true);
                }
            }
        })).show();
    }

    private void bindFacebook(String accessToken, String userId) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "accessToken", accessToken,
                "userId", userId,
                "token", token,
                "clientType", Constant.CLIENT_TYPE_ANDROID
        );
        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_FACEBOOK_BIND, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
                sbBindFacebook.setCheckedNoEvent(false);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                sbBindFacebook.setCheckedNoEvent(false);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                sbBindFacebook.setCheckedNoEvent(true);
                Hawk.put(SPField.FIELD_FB_BINDING_STATUS, Constant.TRUE_INT);
                ToastUtil.success(_mActivity, "綁定Facebook成功");
            }
        });
    }

    /**
     * 解綁Facebook
     * @param accessToken facebook的token
     * @param userId facebook的userId
     */
    private void unbindFacebook(String accessToken, String userId) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "accessToken", accessToken,
                "userId", userId,
                "token", token,
                "clientType", Constant.CLIENT_TYPE_ANDROID
        );
        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_FACEBOOK_UNBIND, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
                sbBindFacebook.setCheckedNoEvent(true);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                sbBindFacebook.setCheckedNoEvent(true);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                sbBindFacebook.setCheckedNoEvent(false);
                Hawk.put(SPField.FIELD_FB_BINDING_STATUS, Constant.FALSE_INT);
                ToastUtil.success(_mActivity, "解綁Facebook成功");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadBindStatus() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_MEMBER_DETAIL, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    String weixinUserInfo = responseObj.getSafeString("datas.memberInfo.weixinUserInfo");
                    if(weixinUserInfo.length() > 0){
                        wxBindingStatus = Constant.TRUE_INT;
                        sbBindWeixin.setCheckedNoEvent(true);
                    }else {
                        wxBindingStatus = Constant.FALSE_INT;
                        sbBindWeixin.setCheckedNoEvent(false);
                    }
                    SLog.info("wxBindingStatus[%d]", wxBindingStatus);

                    fbBindingStatus = Hawk.get(SPField.FIELD_FB_BINDING_STATUS, Constant.FALSE_INT);
                    SLog.info("fbBindingStatus[%d]", fbBindingStatus);
                    if(fbBindingStatus == Constant.TRUE_INT){
                        sbBindFacebook.setCheckedNoEvent(true);
                    }else {
                        sbBindFacebook.setCheckedNoEvent(false);
                    }
                    SLog.info("fbBindingStatus[%d]", fbBindingStatus);

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_WEIXIN_UNBIND || message.messageType == EBMessageType.MESSAGE_TYPE_WEIXIN_BIND) {
            int actionType;
            if (message.messageType == EBMessageType.MESSAGE_TYPE_WEIXIN_UNBIND) {
                actionType = ACTION_TYPE_UNBIND;
            } else {
                actionType = ACTION_TYPE_BIND;
            }
            String code = (String) message.data;
            changeWeixinBinding(actionType, code);
        }
    }

    private void changeWeixinBinding(int actionType, String code) {
        // 根據返回的數據，請求服務器端解綁微信
        String token = User.getToken();

        String url;
        if (actionType == ACTION_TYPE_UNBIND) {
            url = Api.PATH_UNBIND_WEIXIN;
        } else {
            url = Api.PATH_BIND_WEIXIN;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "code", code,
                "clientType", Constant.CLIENT_TYPE_ANDROID
        );

        SLog.info("url[%s], params[%s]", url, params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
                // 失敗，則恢復回原狀態
                if (actionType == ACTION_TYPE_UNBIND) {
                    sbBindWeixin.setCheckedNoEvent(true);
                } else {
                    sbBindWeixin.setCheckedNoEvent(false);
                }
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        // 失敗，則恢復回原狀態
                        if (actionType == ACTION_TYPE_UNBIND) {
                            sbBindWeixin.setCheckedNoEvent(true);
                        } else {
                            sbBindWeixin.setCheckedNoEvent(false);
                        }
                        return;
                    }

                    if (responseObj.exists("datas.bind")) {
                        boolean bind = responseObj.getBoolean("datas.bind");
                        if (!bind) {
                            sbBindWeixin.setCheckedNoEvent(false);
                            ToastUtil.success(_mActivity,responseObj.getSafeString("datas.message"));
                            return;
                        }
                    }


                    // 成功
                    if (actionType == ACTION_TYPE_UNBIND) {
                        //ToastUtil.success(_mActivity,responseObj.getString());
                        sbBindWeixin.setCheckedNoEvent(false);
                        ToastUtil.success(_mActivity,"解綁成功");
                        wxBindingStatus = Constant.FALSE_INT;
                    } else {
                        sbBindWeixin.setCheckedNoEvent(true);
                        ToastUtil.success(_mActivity,"綁定成功");
                        wxBindingStatus = Constant.TRUE_INT;
                    }

                    Hawk.put(SPField.FIELD_WX_BINDING_STATUS, wxBindingStatus);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_logout) {
            User.logout();
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_LOGOUT_SUCCESS, null);
            popTo(MainFragment.class, false);
        } else if (id == R.id.btn_login_password) {
            Util.startFragment(ResetPasswordFragment.newInstance(Constant.USAGE_RESET_PASSWORD, false));
        } else if (id == R.id.btn_payment_password) {
            Util.startFragment(ResetPasswordFragment.newInstance(Constant.USAGE_SET_PAYMENT_PASSWORD, true));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        /*
        bindingStatus = Hawk.get(SPField.FIELD_WX_BINDING_STATUS, 0);
        SLog.info("bindingStatus[%d]", bindingStatus);
        sbBindWeixin.setCheckedNoEvent(bindingStatus == 1);

         */
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
