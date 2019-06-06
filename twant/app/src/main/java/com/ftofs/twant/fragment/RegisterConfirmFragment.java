package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.SharedPreferenceUtil;
import com.ftofs.twant.util.SqliteUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

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

        etSmsCode = view.findViewById(R.id.et_sms_code);
        etPassword = view.findViewById(R.id.et_password);
        etPassword.setHint(String.format(getString(R.string.input_password_hint), "-"));

        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        etNickname = view.findViewById(R.id.et_nickname);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_register) {
            String fullMobile = areaCode + "," + mobile;
            String smsCode =etSmsCode.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String nickname = etNickname.getText().toString().trim();

            if (StringUtil.isEmpty(smsCode)) {
                ToastUtil.show(_mActivity, getString(R.string.input_sms_code_hint));
                return;
            }

            if (StringUtil.isEmpty(password)) {
                ToastUtil.show(_mActivity, "請輸入密碼");
                return;
            }

            if (!password.equals(confirmPassword)) {
                ToastUtil.show(_mActivity, "密碼不一致");
                return;
            }

            if (StringUtil.isEmpty(nickname)) {
                ToastUtil.show(_mActivity, getString(R.string.input_nickname_hint));
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

                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    try {
                        int code = responseObj.getInt("code");
                        if (code != ResponseCode.SUCCESS) {
                            ToastUtil.show(_mActivity, responseObj.getString("datas.error"));
                            return;
                        }

                        // 保存服務器端返回的數據
                        int userId = responseObj.getInt("datas.memberId");
                        SharedPreferenceUtil.saveUserInfo(responseObj);
                        EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_LOGIN_SUCCESS, null);
                        SqliteUtil.switchUserDB(userId);

                        ToastUtil.show(_mActivity, "注冊成功");

                        // 注冊成功，跳到主頁面
                        popTo(MainFragment.class, false);
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
