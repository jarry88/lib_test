package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 個人簡介
 * @author zwm
 */
public class PersonalProfileFragment extends BaseFragment implements View.OnClickListener {
    EditText etPersonalProfile;


    public static PersonalProfileFragment newInstance(String profile) {
        Bundle args = new Bundle();

        args.putString("profile", profile);
        PersonalProfileFragment fragment = new PersonalProfileFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String profile = args.getString("profile");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);

        etPersonalProfile = view.findViewById(R.id.et_personal_profile);
        etPersonalProfile.setText(profile);
        EditTextUtil.cursorSeekToEnd(etPersonalProfile);
        showSoftInput(etPersonalProfile);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_ok) {
            savePersonalProfile();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    private void savePersonalProfile() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        String profile = etPersonalProfile.getText().toString().trim();
        if (StringUtil.isEmpty(profile)) {
            ToastUtil.show(_mActivity, getString(R.string.text_personal_profile) + "不能為空");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "memberBio", profile);
        
        SLog.info("params[%s]", params);

        Api.postUI(Api.PATH_SAVE_PERSONAL_PROFILE, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.show(_mActivity, getString(R.string.text_personal_profile) + "保存成功");
                    pop();
                } catch (Exception e) {

                }
            }
        });
    }
}
