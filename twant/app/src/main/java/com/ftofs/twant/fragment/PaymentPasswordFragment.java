package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 設置支付密碼
 * @author zwm
 */
public class PaymentPasswordFragment extends BaseFragment implements View.OnClickListener {
    EditText etPassword;
    EditText etConfirmPassword;

    public static PaymentPasswordFragment newInstance() {
        Bundle args = new Bundle();

        PaymentPasswordFragment fragment = new PaymentPasswordFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_password, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_clear_password, this);
        Util.setOnClickListener(view, R.id.btn_view_password, this);
        Util.setOnClickListener(view, R.id.btn_clear_confirm_password, this);
        Util.setOnClickListener(view, R.id.btn_view_confirm_password, this);

        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (!password.equals(confirmPassword)) {
                ToastUtil.show(_mActivity, "密碼不一致");
                return;
            }

            if (password.length() < 1) {
                ToastUtil.show(_mActivity, "密碼不能為空");
                return;
            }

            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                ToastUtil.show(_mActivity, "未登錄");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "smsAuthCode", "",
                    "payPwd", password,
                    "payPwdRepeat", confirmPassword
            );

            Api.postUI(Api.PATH_EDIT_PAYMENT_PASSWORD, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.show(_mActivity, "設置支付密碼成功");
                    return;
                }
            });
        }
    }
}
