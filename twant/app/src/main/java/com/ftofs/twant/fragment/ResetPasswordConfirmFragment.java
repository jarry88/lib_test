package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.ResponseCode;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 確認重置密碼、確認支付密碼Fragment
 * @author zwm
 */
public class ResetPasswordConfirmFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 用途
     */
    int usage;

    TextView tvFragmentTitle;

    String areaCode;
    String mobile;
    int smsCodeValidTime;
    boolean isModifyPaymentPassword;

    EditText etSmsCode;
    EditText etPassword;
    EditText etConfirmPassword;

    RelativeLayout rlButtonContainer;

    /**
     *
     * @param areaCode 區號
     * @param mobile 手機號
     * @param smsCodeValidTime 短信驗證碼的過期時間
     * @param isModifyPaymentPassword 主要用于區分顯示【修改支付密碼】還是【設置支付密碼】
     * @return
     */
    public static ResetPasswordConfirmFragment newInstance(int usage, String areaCode, String mobile, int smsCodeValidTime, boolean isModifyPaymentPassword) {
        Bundle args = new Bundle();

        args.putInt("usage", usage);
        args.putString("areaCode", areaCode);
        args.putString("mobile", mobile);
        args.putInt("smsCodeValidTime", smsCodeValidTime);
        args.putBoolean("isModifyPaymentPassword", isModifyPaymentPassword);
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
        usage = args.getInt("usage");
        areaCode = args.getString("areaCode");
        mobile = args.getString("mobile");
        smsCodeValidTime = args.getInt("smsCodeValidTime");
        isModifyPaymentPassword = args.getBoolean("isModifyPaymentPassword");

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        if (usage == Constant.USAGE_RESET_PASSWORD) {
            tvFragmentTitle.setText(R.string.reset_password_fragment_title);
        } else if (usage == Constant.USAGE_SET_PAYMENT_PASSWORD) {
            tvFragmentTitle.setText(R.string.payment_password_fragment_title);
        }

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);

        SLog.info("areaCode[%s], mobile[%s], smsCodeValidTime[%d]", areaCode, mobile, smsCodeValidTime);

        TextView tvSmsCodeHint = view.findViewById(R.id.tv_sms_code_hint);
        tvSmsCodeHint.setText(String.format(getString(R.string.text_sms_code_validate_hint), mobile, smsCodeValidTime));

        etSmsCode = view.findViewById(R.id.et_sms_code);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);

        if (usage == Constant.USAGE_RESET_PASSWORD) {
            etPassword.setHint(String.format(getString(R.string.input_password_hint), "-"));
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); // textPassword
        } else if (usage == Constant.USAGE_SET_PAYMENT_PASSWORD) {
            etPassword.setHint(R.string.payment_password_hint);
            etPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD); // numberPassword
        }

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
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {
            if (usage == Constant.USAGE_RESET_PASSWORD) {
                doResetPassword();
            } else if (usage == Constant.USAGE_SET_PAYMENT_PASSWORD) {
                doSetPaymentPassword();
            }
        }
    }

    private void doResetPassword() {
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

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                try {
                    int code = responseObj.getInt("code");
                    if (code != ResponseCode.SUCCESS) {
                        ToastUtil.error(_mActivity, responseObj.getSafeString("datas.error"));
                        return;
                    }

                    hideSoftInput();

                    /*
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
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void doSetPaymentPassword() {
        String smsCode =etSmsCode.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (StringUtil.isEmpty(smsCode)) {
            ToastUtil.error(_mActivity, getString(R.string.input_sms_code_hint));
            return;
        }

        if (!password.equals(confirmPassword)) {
            ToastUtil.error(_mActivity, "密碼不一致");
            return;
        }

        if (password.length() < 1) {
            ToastUtil.error(_mActivity, "密碼不能為空");
            return;
        }

        // 校驗密碼是否為6位數字
        Pattern pattern = Pattern.compile("^[0-9]{6}$");
        Matcher matcher = pattern.matcher(password);
        boolean result = matcher.matches();
        if (!result) {
            ToastUtil.error(_mActivity, getString(R.string.payment_password_hint));
            return;
        }

        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            ToastUtil.error(_mActivity, getString(R.string.text_user_not_login));
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "smsAuthCode", smsCode,
                "payPwd", password,
                "payPwdRepeat", confirmPassword);
        SLog.info("params[%s]", params);

        Api.postUI(Api.PATH_WALLET_PAYMENT_PASSWORD, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                // 出棧2個Fragment(密碼Fragment和確認密碼Fragment)
                Fragment fragment = Util.getFragmentByLayer(_mActivity, 2);
                if (fragment != null) {
                    popTo(fragment.getClass(), false);
                }
                ToastUtil.success(_mActivity, "設置支付密碼成功");
                return;
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
