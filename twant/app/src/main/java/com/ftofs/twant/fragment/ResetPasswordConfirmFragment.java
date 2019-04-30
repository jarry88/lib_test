package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.SharedPreferenceUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 確認重置密碼
 * @author zwm
 */
public class ResetPasswordConfirmFragment extends BaseFragment implements View.OnClickListener {
    String areaCode;
    String mobile;
    int smsCodeValidTime;

    EditText etSmsCode;
    EditText etPassword;
    EditText etConfirmPassword;

    /**
     *
     * @param areaCode 區號
     * @param mobile 手機號
     * @param smsCodeValidTime 短信驗證碼的過期時間
     * @return
     */
    public static ResetPasswordConfirmFragment newInstance(String areaCode, String mobile, int smsCodeValidTime) {
        Bundle args = new Bundle();

        args.putString("areaCode", areaCode);
        args.putString("mobile", mobile);
        args.putInt("smsCodeValidTime", smsCodeValidTime);
        ResetPasswordConfirmFragment fragment = new ResetPasswordConfirmFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password_confirm, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        areaCode = args.getString("areaCode");
        mobile = args.getString("mobile");
        smsCodeValidTime = args.getInt("smsCodeValidTime");

        Util.setOnClickListener(view, R.id.btn_ok, this);

        SLog.info("areaCode[%s], mobile[%s], smsCodeValidTime[%d]", areaCode, mobile, smsCodeValidTime);

        etSmsCode = view.findViewById(R.id.et_sms_code);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            String fullMobile = areaCode + "," + mobile;
            String smsCode =etSmsCode.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (!password.equals(confirmPassword)) {
                ToastUtil.show(_mActivity, "密碼不一致");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "mobile", fullMobile,
                    "smsAuthCode", smsCode,
                    "memberPwd", password,
                    "memberPwdRepeat", confirmPassword);


            Api.postUI(Api.PATH_RESET_PASSWORD, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    try {
                        int code = responseObj.getInt("code");
                        if (code != ResponseCode.SUCCESS) {
                            ToastUtil.show(_mActivity, responseObj.getString("datas.error"));
                            return;
                        }

                        // 保存服務器端返回的數據
                        SharedPreferenceUtil.saveUserInfo(responseObj);

                        // 重置密碼成功，退回到【設置】界面
                        popTo(SettingFragment.class, false);
                        ToastUtil.show(_mActivity, "重置密碼成功");
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
