package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.SharedPreferenceUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 密碼登入
 * @author zwm
 */
public class PasswordLoginFragment extends BaseFragment implements View.OnClickListener {
    // !!!暫時寫死
    String areaCode = "0086";

    ImageView btnRefreshCaptcha;
    String captchaKey;
    EditText etMobile;
    EditText etPassword;
    EditText etCaptcha;

    public static PasswordLoginFragment newInstance() {
        Bundle args = new Bundle();

        PasswordLoginFragment fragment = new PasswordLoginFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_login, this);
        btnRefreshCaptcha = view.findViewById(R.id.btn_refresh_captcha);
        btnRefreshCaptcha.setOnClickListener(this);

        etMobile = view.findViewById(R.id.et_mobile);
        etPassword = view.findViewById(R.id.et_password);
        etCaptcha = view.findViewById(R.id.et_captcha);

        refreshCaptcha();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            String mobile = etMobile.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String captcha = etCaptcha.getText().toString().trim();

            String fullMobile = areaCode + "," + mobile;

            EasyJSONObject params = EasyJSONObject.generate(
                    "memberName", fullMobile,
                    "password", password,
                    "captchaKey", captchaKey,
                    "captchaVal", captcha,
                    "clientType", Constant.CLIENT_TYPE_ANDROID
            );
            SLog.info("params[%s]", params);
            Api.postUI(Api.PATH_LOGIN, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseStr = response.body().string();
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.show(_mActivity, "登錄成功");
                    SharedPreferenceUtil.saveUserInfo(responseObj);
                }
            });
        } else if (id == R.id.btn_refresh_captcha) {
            refreshCaptcha();
        }
    }

    /**
     * 刷新驗證碼
     */
    private void refreshCaptcha() {
        Api.refreshCaptcha(new TaskObserver() {
            @Override
            public void onMessage() {
                Pair<Bitmap, String> result = (Pair<Bitmap, String>) message;
                SLog.info("result[%s]", result);
                if (result == null) {
                    return;
                }
                captchaKey = result.second;
                btnRefreshCaptcha.setImageBitmap(result.first);
            }
        });
    }
}
