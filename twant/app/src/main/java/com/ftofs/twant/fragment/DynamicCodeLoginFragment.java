package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.LoginType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.constant.Sms;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.CheckPhoneView;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 基因碼登入
 * @author zwm
 */
public class DynamicCodeLoginFragment extends BaseFragment implements
        View.OnClickListener, OnSelectedListener {
    /**
     * 當前選中的區號索引
     */
    private int selectedMobileZoneIndex = 0;
    List<MobileZone> mobileZoneList = new ArrayList<>();
    ImageView btnRefreshCaptcha;
    String captchaKey;
    CheckPhoneView etMobileView;

    EditText etCaptcha;
    EditText etSmsCode;
    TextView tvAreaName;
    TextView btnGetSMSCode;

    String textSecond;
    CountDownTimer countDownTimer;
    boolean canSendSMS = true;

    ImageView btnWechatLogin;

    boolean loginButtonEnable = true; // 防止重覆點擊

    CommonCallback commonCallback;
    private ImageView imgClick;
    private boolean checkButtonState=true;
    private TextView tvMobileError;
    private LinearLayout llMobileErrorContainer;

    public void setCommonCallback(CommonCallback commonCallback) {
        this.commonCallback = commonCallback;
    }

    public static DynamicCodeLoginFragment newInstance(CommonCallback commonCallback) {
        Bundle args = new Bundle();

        DynamicCodeLoginFragment fragment = new DynamicCodeLoginFragment();
        fragment.setArguments(args);

        fragment.setCommonCallback(commonCallback);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic_code_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textSecond = getString(R.string.text_second);
        // 60秒倒計時
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetSMSCode.setText(Math.round(millisUntilFinished / 1000) + textSecond);
            }

            @Override
            public void onFinish() {
                canSendSMS = true;
                btnGetSMSCode.setText(R.string.get_sms_code);
            }
        };
        tvMobileError = view.findViewById(R.id.tv_mobile_error);
        llMobileErrorContainer = view.findViewById(R.id.ll_container_mobile_error);

        Util.setOnClickListener(view, R.id.btn_login, this);
        Util.setOnClickListener(view, R.id.btn_facebook_login, this);
        Util.setOnClickListener(view, R.id.btn_mobile_zone, this);
        Util.setOnClickListener(view, R.id.btn_view_tos, this);
        Util.setOnClickListener(view, R.id.btn_view_private_terms, this);
        Util.setOnClickListener(view, R.id.btn_forget_password, this);
        Util.setOnClickListener(view, R.id.img_check, this);
        imgClick = view.findViewById(R.id.img_check);
        btnGetSMSCode = view.findViewById(R.id.btn_get_sms_code);
        btnGetSMSCode.setOnClickListener(this);

        btnRefreshCaptcha = view.findViewById(R.id.btn_refresh_captcha);
        btnRefreshCaptcha.setOnClickListener(this);

        etMobileView = view.findViewById(R.id.et_mobile_view);
        etCaptcha = view.findViewById(R.id.et_captcha);
        etSmsCode = view.findViewById(R.id.et_sms_code);
        etSmsCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                SLog.info("here");
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doLogin();
                }
                return false;
            }
        });
        tvAreaName = view.findViewById(R.id.tv_area_name);

        btnWechatLogin = view.findViewById(R.id.btn_wechat_login);
        btnWechatLogin.setOnClickListener(this);


        refreshCaptcha();
        getMobileZoneList();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_refresh_captcha) {
            refreshCaptcha();
        } else if (id == R.id.btn_wechat_login) {
            if (!TwantApplication.wxApi.isWXAppInstalled()) { // 未安裝微信
                ToastUtil.error(_mActivity, getString(R.string.weixin_not_installed_hint));
                return;
            }

            if (Config.PROD) {
                MobclickAgent.onEvent(TwantApplication.getInstance(), UmengAnalyticsActionName.WECHAT_LOGIN);
            }

            ((MainActivity) _mActivity).doWeixinLogin(Constant.WEIXIN_AUTH_USAGE_LOGIN);
        } else if (id == R.id.img_check) {
            checkButtonState =!checkButtonState;
            Glide.with(_mActivity).load(checkButtonState?R.drawable.icon_checked:R.drawable.icon_unchecked).centerCrop().into(imgClick);

        } else if (id == R.id.btn_facebook_login) {
            if (Config.PROD) {
                MobclickAgent.onEvent(TwantApplication.getInstance(), UmengAnalyticsActionName.FACEBOOK_LOGIN);
            }

            ((LoginFragment) commonCallback).facebookLogin();
        } else if (id == R.id.btn_get_sms_code) {
            if (!canSendSMS) {
                return;
            }

            if (mobileZoneList.size() <= selectedMobileZoneIndex) {
                return;
            }
            MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);

            kotlin.Pair<Boolean, String> pair = etMobileView.checkError();
            SLog.info("Pair[%s]", pair.toString());

            if (!pair.component1()) {
                ToastUtil.error(_mActivity, pair.component2());
                return;
            }


            String mobile=pair.component2();
            String fullMobile = mobileZone.areaCode + "," + mobile;
            String captchaText = etCaptcha.getText().toString().trim();
            SLog.info("captchaText[%s]", captchaText);
            if (StringUtil.isEmpty(captchaText)) {
                ToastUtil.error(_mActivity, getString(R.string.input_captcha_hint));
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "mobile", fullMobile,
                    "captchaKey", captchaKey,
                    "captchaVal", captchaText,
                    "sendType", Sms.SEND_TYPE_LOGIN
            );

            SLog.info("params[%s]", params.toString());
            Api.getUI(Api.PATH_SEND_SMS_CODE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    try {
                        int code = responseObj.getInt("code");
                        if (code != ResponseCode.SUCCESS) {
                            ToastUtil.error(_mActivity, responseObj.getSafeString("datas.error"));
                            return;
                        }

                        ToastUtil.success(_mActivity, "基因碼已發送");
                        canSendSMS = false;
                        countDownTimer.start();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } else if (id == R.id.btn_login) {
            if (!loginButtonEnable) {
                return;
            }
            if (Config.PROD) {
                MobclickAgent.onEvent(TwantApplication.getInstance(), UmengAnalyticsActionName.LOGIN);
            }
            doLogin();
        } else if (id == R.id.btn_mobile_zone) {
            List<ListPopupItem> itemList = new ArrayList<>();
            for (MobileZone mobileZone : mobileZoneList) {
                ListPopupItem item = new ListPopupItem(mobileZone.areaId, mobileZone.areaName, null);
                itemList.add(item);
            }

            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.mobile_zone_text),
                            PopupType.MOBILE_ZONE, itemList, selectedMobileZoneIndex, this))
                    .show();
        } else if (id == R.id.btn_view_tos || id == R.id.btn_view_private_terms) {
            int articleId;
            String title;
            if (id == R.id.btn_view_tos) {
                articleId = H5GameFragment.ARTICLE_ID_TERMS_OF_SERVICE;
                title = getString(R.string.text_service_contract);
            } else {
                articleId = H5GameFragment.ARTICLE_ID_TERMS_OF_PRIVATE;
                title = "私隱條款";
            }
            Util.startFragment(H5GameFragment.newInstance(articleId, title));
        } else if (id == R.id.btn_forget_password) {
            Util.startFragment(ResetPasswordFragment.newInstance(Constant.USAGE_RESET_PASSWORD, false));
        }
    }

    private boolean checkMobileZoneList() {
        if (mobileZoneList == null || mobileZoneList.size() <= selectedMobileZoneIndex) {
            return false;
        }
        return true;
    }
    private void doLogin() {
        if (mobileZoneList.size() <= selectedMobileZoneIndex) {
            return;
        }
        MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);
        kotlin.Pair<Boolean, String> pair = etMobileView.checkError();

        if (!pair.component1()) {
            ToastUtil.error(_mActivity, pair.component2());
            return;
        }

        String mobile=etMobileView.getPhone();
        String fullMobile = mobileZone.areaCode + "," + mobile;
        String smsCode = etSmsCode.getText().toString().trim();
        if (StringUtil.isEmpty(smsCode)) {
            ToastUtil.error(_mActivity, getString(R.string.input_sms_code_hint));
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "mobile", fullMobile,
                "smsAuthCode", smsCode,
                "clientType", Constant.CLIENT_TYPE_ANDROID
        );

        loginButtonEnable = false;
        Api.postUI(Api.PATH_MOBILE_LOGIN, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loginButtonEnable = true;
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                // loginButtonEnable = true; 不需要，因为已经登录成功
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                try {
                    int code = responseObj.getInt("code");
                    if (code != ResponseCode.SUCCESS) {
                        ToastUtil.error(_mActivity, responseObj.getSafeString("datas.error"));
                        // 如果出錯，刷新驗證碼
                        refreshCaptcha();
                        loginButtonEnable = true;
                        return;
                    }

                    ToastUtil.success(_mActivity, "登入成功");
                    int userId = responseObj.getInt("datas.memberId");
                    User.onLoginSuccess(userId, LoginType.MOBILE, responseObj);
                    hideSoftInput();
                    Util.getMemberToken(_mActivity);

                    if (commonCallback != null) {
                        SLog.info("Fragment出棧");
                        commonCallback.onSuccess(null);
                    }
                } catch (Exception e) {
                    loginButtonEnable = true;
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 刷新驗證碼
     */
    private void refreshCaptcha() {
        Api.refreshCaptcha(new TaskObserver() {
            @Override
            public void onMessage() {
                Pair<Bitmap, String> result = (Pair<Bitmap, String>) message;
                if (result == null) {
                    return;
                }
                captchaKey = result.second;
                btnRefreshCaptcha.setImageBitmap(result.first);
            }
        });
    }

    /**
     * 获取区号列表
     */
    private void getMobileZoneList() {
        Api.getMobileZoneList(new TaskObserver() {
            @Override
            public void onMessage() {
                List<MobileZone> result = (List<MobileZone>) message;
                if (result == null) {
                    return;
                }
                mobileZoneList = result;

                SLog.info("mobileZoneList.size[%d]", mobileZoneList.size());
                if (mobileZoneList.size() > 0) {
                    etMobileView.setMobileList(mobileZoneList);
                    onSelected(null,0,null);
                }
            }
        });
    }


    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("selectedMobileZoneIndex[%d], id[%d]", selectedMobileZoneIndex, id);
        this.selectedMobileZoneIndex = id;
        String areaName = mobileZoneList.get(selectedMobileZoneIndex).areaName;
        tvAreaName.setText(areaName);
        etMobileView.setZoneIndex(id);
    }
}
