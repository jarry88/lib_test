package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.LoginType;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.UmengAnalyticsActionName;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;
import com.umeng.analytics.MobclickAgent;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 確認注冊界面
 * @author zwm
 */
public class RegisterConfirmFragment extends BaseFragment implements View.OnClickListener {
    String areaCode;
    String mobile;
    int smsCodeValidTime;

    EditText etSmsCode;
    EditText etPassword;
    EditText etConfirmPassword;
    EditText etNickname;
    EditText etPromotionCode;
    private TextView btnRegister;

    ImageView icPromotionCodeVisibility;
    View rlPromotionCodeContainer;
    boolean promotionCodeVisible = false;  // 推薦碼是否處於可見狀態

    /**
     *
     * @param areaCode 區號
     * @param mobile 手機號
     * @param smsCodeValidTime 短信驗證碼的過期時間
     * @return
     */
    public static RegisterConfirmFragment newInstance(String areaCode, String mobile, int smsCodeValidTime) {
        Bundle args = new Bundle();

        args.putString("areaCode", areaCode);
        args.putString("mobile", mobile);
        args.putInt("smsCodeValidTime", smsCodeValidTime);
        RegisterConfirmFragment fragment = new RegisterConfirmFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_confirm, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        areaCode = args.getString("areaCode");
        mobile = args.getString("mobile");
        smsCodeValidTime = args.getInt("smsCodeValidTime");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_register, this);

        SLog.info("areaCode[%s], mobile[%s], smsCodeValidTime[%d]", areaCode, mobile, smsCodeValidTime);

        TextView tvSmsCodeHint = view.findViewById(R.id.tv_sms_code_hint);
        tvSmsCodeHint.setText(String.format(getString(R.string.text_sms_code_validate_hint), mobile, smsCodeValidTime));
        btnRegister = view.findViewById(R.id.btn_register);
        etSmsCode = view.findViewById(R.id.et_sms_code);
        etSmsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateBtnRegister();
            }
        });
        etPassword = view.findViewById(R.id.et_password);
        etPassword.setHint(String.format(getString(R.string.input_password_hint), "-"));
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateBtnRegister();
            }
        });
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateBtnRegister();
            }
        });
        etNickname = view.findViewById(R.id.et_nickname);
        etNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateBtnRegister();
            }
        });
        etPromotionCode = view.findViewById(R.id.et_promotion_code);
        icPromotionCodeVisibility = view.findViewById(R.id.ic_promotion_code_visibility);
        rlPromotionCodeContainer = view.findViewById(R.id.rl_promotion_code_container);
        Util.setOnClickListener(view, R.id.btn_switch_promotion_code_visibility, this);
    }

    private void updateBtnRegister() {
        String smsCode =etSmsCode.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String nickname = etNickname.getText().toString().trim();

        if (StringUtil.isEmpty(smsCode)) {
            return;
        }

        if (StringUtil.isEmpty(password)) {
            return;
        }

        if (!password.equals(confirmPassword)) {
            return;
        }

        if (StringUtil.isEmpty(nickname)) {
            return;
        }
        btnRegister.setBackgroundResource(R.drawable.blue_button1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_switch_promotion_code_visibility) {
            promotionCodeVisible = !promotionCodeVisible;
            icPromotionCodeVisibility.setImageResource(promotionCodeVisible ? R.drawable.ic_baseline_arrow_drop_up_24 : R.drawable.ic_baseline_arrow_drop_down_24);
            rlPromotionCodeContainer.setVisibility(promotionCodeVisible ? View.VISIBLE : View.GONE);
        } else if (id == R.id.btn_register) {
            if (Config.PROD) {
                MobclickAgent.onEvent(TwantApplication.Companion.get(), UmengAnalyticsActionName.REGISTER);
            }

            try {
                String fullMobile = areaCode + "," + mobile;
                String smsCode =etSmsCode.getText().toString().trim();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                String nickname = etNickname.getText().toString().trim();

                if (StringUtil.isEmpty(smsCode)) {
                    ToastUtil.error(_mActivity, getString(R.string.input_sms_code_hint));
                    btnRegister.setBackgroundResource(R.drawable.grey_button);

                    return;
                }

                if (StringUtil.isEmpty(password)) {
                    ToastUtil.error(_mActivity, "請輸入密碼");
                    btnRegister.setBackgroundResource(R.drawable.grey_button);

                    return;
                }

                if (!password.equals(confirmPassword)) {
                    ToastUtil.error(_mActivity, "密碼不一致");
                    btnRegister.setBackgroundResource(R.drawable.grey_button);

                    return;
                }

                if (StringUtil.isEmpty(nickname)) {
                    ToastUtil.error(_mActivity, getString(R.string.input_nickname_hint));
                    btnRegister.setBackgroundResource(R.drawable.grey_button);
                    return;
                }

                SLog.info("mobile[%s]", fullMobile);
                EasyJSONObject params = EasyJSONObject.generate(
                        "mobile", fullMobile,
                        "smsAuthCode", smsCode,
                        "memberPwd", password,
                        "memberPwdRepeat", confirmPassword,
                        "clientType", Constant.CLIENT_TYPE_ANDROID,
                        "nickName", nickname);

                // 拼湊推薦碼和uuid
                String promotionCode = etPromotionCode.getText().toString().trim();
                if (!StringUtil.isEmpty(promotionCode)) {
                    String uuid = Util.getUUID();
                    if (!StringUtil.isEmpty(uuid)) {
                        params.set("recommendNumber", promotionCode);
                        params.set("clientUuid", uuid);
                    }
                }

                String url = Api.PATH_MOBILE_REGISTER;
                SLog.info("url[%s], params[%s]", url, params);
                Api.postUI(url, params, new UICallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                        ToastUtil.showNetworkError(_mActivity, e);
                        btnRegister.setBackgroundResource(R.drawable.grey_button);
                    }

                    @Override
                    public void onResponse(Call call, String responseStr) throws IOException {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                            return;
                        }

                        try {
                            int code = responseObj.getInt("code");
                            if (code != ResponseCode.SUCCESS) {
                                ToastUtil.error(_mActivity, responseObj.getSafeString("datas.error"));
                                return;
                            }

                            // 保存服務器端返回的數據
                            int userId = responseObj.getInt("datas.memberId");
                            User.onLoginSuccess(userId, LoginType.MOBILE, responseObj);
                            hideSoftInput();
                            Util.getMemberToken(_mActivity);

                            ToastUtil.success(_mActivity, "注冊成功");

                            if (Config.PROD) {
                                MobclickAgent.onEvent(TwantApplication.Companion.get(), UmengAnalyticsActionName.REGISTER_SUCCESS);
                            }

                            // 注冊成功，跳到主頁面
                            popTo(MainFragment.class, false);
                        } catch (Exception e) {
                            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                        }
                    }
                });
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }
    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("__onSupportVisible");
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        btnRegister.setBackgroundResource(R.drawable.grey_button);
    }
    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        SLog.info("__onSupportInvisible");
        _mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
