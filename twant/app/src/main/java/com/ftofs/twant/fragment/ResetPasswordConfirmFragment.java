package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    RelativeLayout rlButtonContainer;

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

        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        areaCode = args.getString("areaCode");
        mobile = args.getString("mobile");
        smsCodeValidTime = args.getInt("smsCodeValidTime");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);

        SLog.info("areaCode[%s], mobile[%s], smsCodeValidTime[%d]", areaCode, mobile, smsCodeValidTime);

        TextView tvSmsCodeHint = view.findViewById(R.id.tv_sms_code_hint);
        tvSmsCodeHint.setText(String.format(getString(R.string.text_sms_code_validate_hint), mobile, smsCodeValidTime));

        etSmsCode = view.findViewById(R.id.et_sms_code);
        etPassword = view.findViewById(R.id.et_password);
        etPassword.setHint(String.format(getString(R.string.input_password_hint), "-"));
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);

        rlButtonContainer = view.findViewById(R.id.rl_button_container);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_KEYBOARD_STATE_CHANGED) {
            int keyboardState = (int) message.data;
            SLog.info("keyboardState[%d]", keyboardState);
            if (keyboardState == Constant.KEYBOARD_SHOWN) {
                rlButtonContainer.setVisibility(View.GONE);
            } else {
                rlButtonContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlButtonContainer.setVisibility(View.VISIBLE);
                    }
                }, 150);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_ok) {
            String fullMobile = areaCode + "," + mobile;
            String smsCode =etSmsCode.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (!password.equals(confirmPassword)) {
                ToastUtil.error(_mActivity, "密碼不一致");
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
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    try {
                        int code = responseObj.getInt("code");
                        if (code != ResponseCode.SUCCESS) {
                            ToastUtil.error(_mActivity, responseObj.getString("datas.error"));
                            return;
                        }

                        hideSoftInput();

                        /**
                         * 重置密碼后的操作
                         * 如果用戶未登錄，跳轉到登錄頁面
                         * 如果用戶已登錄，跳轉到個人專頁
                         */

                        int userId = User.getUserId();
                        if (userId > 0) {
                            popTo(MainFragment.class, false);
                            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.MY_FRAGMENT);
                        } else {
                            popTo(LoginFragment.class, false);
                        }


                        ToastUtil.success(_mActivity, "重置密碼成功");

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
