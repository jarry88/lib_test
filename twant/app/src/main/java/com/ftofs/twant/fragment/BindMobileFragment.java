package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.LoginType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 綁定手機號Fragment
 * @author zwm
 */
public class BindMobileFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    public static final int BIND_TYPE_WEIXIN = 1;
    public static final int BIND_TYPE_FACEBOOK = 2;

    int bindType;
    String accessToken;
    String openId;
    String userId;

    /**
     * 當前選中的區號索引
     */
    private int selectedMobileZoneIndex = 0;
    List<MobileZone> mobileZoneList = new ArrayList<>();
    String captchaKey;

    TextView tvAreaName;
    TextView btnGetSMSCode;
    ImageView btnRefreshCaptcha;


    EditText etMobile;
    EditText etCaptcha;
    EditText etSmsCode;

    boolean canSendSMS = true;

    /**
     * 構造方法
     * @param accessToken 微信返回的
     * @param openId 微信返回的
     * @return
     */
    public static BindMobileFragment newInstance(String accessToken, String openId) {
        Bundle args = new Bundle();

        args.putInt("bindType", BIND_TYPE_WEIXIN);
        args.putString("accessToken", accessToken);
        args.putString("openId", openId);
        BindMobileFragment fragment = new BindMobileFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public static BindMobileFragment newInstance(int bindType, String accessToken, String userId) {
        Bundle args = new Bundle();

        args.putInt("bindType", bindType);
        args.putString("accessToken", accessToken);
        args.putString("userId", userId);
        BindMobileFragment fragment = new BindMobileFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_mobile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        bindType = args.getInt("bindType");
        if (bindType == BIND_TYPE_WEIXIN) {
            accessToken = args.getString("accessToken");
            openId = args.getString("openId");
        } else if (bindType == BIND_TYPE_FACEBOOK) {
            accessToken = args.getString("accessToken");
            userId = args.getString("userId");
        }


        tvAreaName = view.findViewById(R.id.tv_area_name);

        btnGetSMSCode = view.findViewById(R.id.btn_get_sms_code);
        btnGetSMSCode.setOnClickListener(this);


        btnRefreshCaptcha = view.findViewById(R.id.btn_refresh_captcha);
        btnRefreshCaptcha.setOnClickListener(this);

        etMobile = view.findViewById(R.id.et_mobile);
        etCaptcha = view.findViewById(R.id.et_captcha);
        etSmsCode = view.findViewById(R.id.et_sms_code);
        etSmsCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                SLog.info("here");
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    doBind();
                }
                return false;
            }
        });

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_mobile_zone, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);

        refreshCaptcha();
        getMobileZoneList();
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
                    tvAreaName.setText(mobileZoneList.get(0).areaName);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } if (id == R.id.btn_refresh_captcha) {
            refreshCaptcha();
        } else if (id == R.id.btn_get_sms_code) {
            if (!canSendSMS) {
                return;
            }

            if (mobileZoneList.size() <= selectedMobileZoneIndex) {
                return;
            }
            MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);

            String mobile = etMobile.getText().toString().trim();
            if (StringUtil.isEmpty(mobile)) {
                ToastUtil.error(_mActivity, getString(R.string.input_mobile_hint));
                return;
            }

            if (!StringUtil.isMobileValid(mobile, mobileZone.areaId)) {
                String[] areaArray = new String[] {
                        "",
                        getString(R.string.text_hongkong),
                        getString(R.string.text_mainland),
                        getString(R.string.text_macao)
                };

                String msg = String.format(getString(R.string.text_invalid_mobile), areaArray[mobileZone.areaId]);
                ToastUtil.error(_mActivity, msg);
                return;
            }


            String fullMobile = mobileZone.areaCode + "," + mobile;
            /*
            String captchaText = etCaptcha.getText().toString().trim();
            SLog.info("captchaText[%s]", captchaText);
            if (StringUtil.isEmpty(captchaText)) {
                ToastUtil.error(_mActivity, getString(R.string.input_captcha_hint));
                return;
            }

             */

            EasyJSONObject params = EasyJSONObject.generate(
                    "mobile", fullMobile);

            SLog.info("params[%s]", params.toString());
            Api.getUI(Api.PATH_WX_BIND_SEND_SMS_CODE, params, new UICallback() {
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
                        String textSecond = getString(R.string.text_second);
                        // 60秒倒計時
                        CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
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
                        countDownTimer.start();
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } else if (id == R.id.btn_ok) {
            doBind();
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
        }
    }

    private void doBind() {
        if (mobileZoneList.size() <= selectedMobileZoneIndex) {
            return;
        }
        MobileZone mobileZone = mobileZoneList.get(selectedMobileZoneIndex);

        String mobile = etMobile.getText().toString().trim();
        if (StringUtil.isEmpty(mobile)) {
            ToastUtil.error(_mActivity, getString(R.string.input_mobile_hint));
            return;
        }

        if (!StringUtil.isMobileValid(mobile, mobileZone.areaId)) {
            String[] areaArray = new String[] {
                    "",
                    getString(R.string.text_hongkong),
                    getString(R.string.text_mainland),
                    getString(R.string.text_macao)
            };

            String msg = String.format(getString(R.string.text_invalid_mobile), areaArray[mobileZone.areaId]);
            ToastUtil.error(_mActivity, msg);
            return;
        }

        String fullMobile = mobileZone.areaCode + "," + mobile;
        String smsCode = etSmsCode.getText().toString().trim();
        if (StringUtil.isEmpty(smsCode)) {
            ToastUtil.error(_mActivity, getString(R.string.input_sms_code_hint));
            return;
        }
        EasyJSONObject params;
        if (bindType == BIND_TYPE_WEIXIN) {
            params = EasyJSONObject.generate(
                    "accessToken", accessToken,
                    "openId", openId,
                    "mobile", fullMobile,
                    "smsAuthCode", smsCode,
                    "clientType", Constant.CLIENT_TYPE_ANDROID
            );
        } else {
            params = EasyJSONObject.generate(
                    "accessToken", accessToken,
                    "userId", userId,
                    "mobile", fullMobile,
                    "smsAuthCode", smsCode,
                    "clientType", Constant.CLIENT_TYPE_ANDROID
            );
        }

        String url = null;
        if (bindType == BIND_TYPE_WEIXIN) {
            url = Api.PATH_WX_LOGIN_STEP2;
        } else if (bindType == BIND_TYPE_FACEBOOK) {
            url = Api.PATH_FACEBOOK_LOGIN_BIND;
        }
        SLog.info("params[%s]", params);
        Api.postUI(url, params, new UICallback() {
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
                        // 如果出錯，刷新驗證碼
                        refreshCaptcha();
                        return;
                    }

                    if (bindType == BIND_TYPE_WEIXIN) {
                        Hawk.put(SPField.FIELD_WX_BINDING_STATUS, Constant.TRUE_INT);
                    } else if (bindType == BIND_TYPE_FACEBOOK) {
                        Hawk.put(SPField.FIELD_FB_BINDING_STATUS, Constant.TRUE_INT);
                    }


                    ToastUtil.success(_mActivity, "綁定成功");
                    int userId = responseObj.getInt("datas.memberId");
                    LoginType loginType = null;
                    if (bindType == BIND_TYPE_WEIXIN) {
                        loginType = LoginType.MOBILE;
                    } else if (bindType == BIND_TYPE_FACEBOOK) {
                        loginType = LoginType.FACEBOOK;
                    }
                    User.onLoginSuccess(userId, loginType, responseObj);
                    hideSoftInput();

                    // 出棧2個Fragment(當前Fragment和LoginFragment)
                    Fragment fragment = Util.getFragmentByLayer(_mActivity, 2);
                    if (fragment != null) {
                        popTo(fragment.getClass(), false);
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("selectedMobileZoneIndex[%d], id[%d]", selectedMobileZoneIndex, id);
        if (this.selectedMobileZoneIndex == id) {
            return;
        }

        this.selectedMobileZoneIndex = id;
        String areaName = mobileZoneList.get(selectedMobileZoneIndex).areaName;
        tvAreaName.setText(areaName);
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
        SLog.info("__onSupportVisible");
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SLog.info("__onSupportInvisible");
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}