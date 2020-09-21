package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SPField;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.HawkUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 個人簡介
 * @author zwm
 */
public class PersonalProfileFragment extends BaseFragment implements View.OnClickListener {
    EditText etPersonalProfile;
    TextView tvWordCount;
    boolean isUser;
    private int MaxLenth=500;


    public static PersonalProfileFragment newInstance() {
        String profile = HawkUtil.getUserData(SPField.USER_DATA_KEY_BIO, "");
        return newInstance(profile);
    }

    public static PersonalProfileFragment newInstance(String profile) {
        Bundle args = new Bundle();

        args.putString("profile", profile);
        PersonalProfileFragment fragment = new PersonalProfileFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static PersonalProfileFragment newInstance(String profile,String memberName) {
        Bundle args = new Bundle();

        args.putString("profile", profile);
        args.putString("memberName",memberName);
        PersonalProfileFragment fragment = new PersonalProfileFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        isUser = !args.containsKey("memberName");
        String profile = args.getString("profile");

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);

        tvWordCount = view.findViewById(R.id.tv_word_count);
        updateWordCount(profile.length());
        etPersonalProfile = view.findViewById(R.id.et_personal_profile);
        etPersonalProfile.setText(profile);
        InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0]=new InputFilter.LengthFilter(MaxLenth);
        etPersonalProfile.setFilters(inputFilters);
        // 字數計數
        if(isUser){
            etPersonalProfile.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int len = s.length();

                    updateWordCount(len);
                }
            });
            EditTextUtil.cursorSeekToEnd(etPersonalProfile);
            showSoftInput(etPersonalProfile);
        }else {
            view.findViewById(R.id.btn_ok).setVisibility(View.GONE);
            etPersonalProfile.setFocusableInTouchMode(false);
        }


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {
            if(isUser)
                savePersonalProfile();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void savePersonalProfile() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        String profile = etPersonalProfile.getText().toString().trim();
        if (StringUtil.isEmpty(profile)) {
            ToastUtil.error(_mActivity, getString(R.string.text_personal_profile) + "不能為空");
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
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.success(_mActivity, getString(R.string.text_personal_profile) + "保存成功");
                    HawkUtil.setUserData(SPField.USER_DATA_KEY_BIO, profile);

                    hideSoftInputPop();
                } catch (Exception e) {

                }
            }
        });
    }

    private void updateWordCount(int wordCount) {
        if (wordCount < MaxLenth) {
            tvWordCount.setText(String.format("%d/500", MaxLenth - wordCount));
        } else {
            tvWordCount.setText(String.format("0/500"));
        }
    }
}
