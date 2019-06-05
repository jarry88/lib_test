package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 個人信息頁面
 * @author zwm
 */
public class PersonalInfoFragment extends BaseFragment implements View.OnClickListener {
    String[] genderTextMap;


    View contentView;
    String nickname;
    public static PersonalInfoFragment newInstance() {
        Bundle args = new Bundle();

        PersonalInfoFragment fragment = new PersonalInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        genderTextMap = new String[] {
                getString(R.string.text_confidentiality), getString(R.string.text_male), getString(R.string.text_female)};

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_modify_nickname, this);

        contentView = view;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        loadPersonalInfo(contentView);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_modify_nickname:
                MainFragment mainFragment = MainFragment.getInstance();
                mainFragment.start(ModifyNicknameFragment.newInstance(nickname));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    private void loadPersonalInfo(final View view) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_MEMBER_DETAIL, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    EasyJSONObject memberInfo = responseObj.getObject("datas.memberInfo");

                    TextView tvUserName = view.findViewById(R.id.tv_user_name);
                    tvUserName.setText(memberInfo.getString("memberName"));

                    TextView tvNickname = view.findViewById(R.id.tv_nickname);
                    nickname = memberInfo.getString("nickName");
                    tvNickname.setText(nickname);

                    int gender = memberInfo.getInt("memberSex");
                    TextView tvGender = view.findViewById(R.id.tv_gender);
                    tvGender.setText(genderTextMap[gender]);

                    String birthday = memberInfo.getString("birthday");
                    if (!StringUtil.isEmpty(birthday)) {
                        TextView tvBirthday = view.findViewById(R.id.tv_birthday);
                        tvBirthday.setText(birthday);
                    }

                    String location = memberInfo.getString("addressAreaInfo");
                    if (!StringUtil.isEmpty(birthday)) {
                        TextView tvBirthday = view.findViewById(R.id.tv_location);
                        tvBirthday.setText(location);
                    }
                } catch (Exception e) {

                }
            }
        });
    }
}
