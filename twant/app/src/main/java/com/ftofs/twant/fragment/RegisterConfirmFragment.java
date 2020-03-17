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
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.LoginType;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONException;
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
    private TextView btnRegister;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        btnRegister.setBackgroundResource(R.drawable.blue_button);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_register) {
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

            Api.postUI(Api.PATH_MOBILE_REGISTER, EasyJSONObject.generate(
                    "mobile", fullMobile,
                    "smsAuthCode", smsCode,
                    "memberPwd", password,
                    "memberPwdRepeat", confirmPassword,
                    "clientType", Constant.CLIENT_TYPE_ANDROID,
                    "nickName", nickname), new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    btnRegister.setBackgroundResource(R.drawable.grey_button);
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

                        // 保存服務器端返回的數據
                        int userId = responseObj.getInt("datas.memberId");
                        User.onLoginSuccess(userId, LoginType.MOBILE, responseObj);
                        hideSoftInput();
                        Util.getMemberToken(_mActivity);

                        ToastUtil.success(_mActivity, "注冊成功");

                        // 注冊成功，跳到主頁面
                        popTo(MainFragment.class, false);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
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
