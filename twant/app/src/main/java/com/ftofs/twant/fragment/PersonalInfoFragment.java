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
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.DateSelectPopup;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 個人信息頁面
 * @author zwm
 */
public class PersonalInfoFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    String[] genderTextMap;


    View contentView;
    String nickname;
    int genderIndex;  // 當前選中的性別索引
    TextView tvGender;
    String birthday;
    TextView tvBirthday;

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
        Util.setOnClickListener(view, R.id.btn_set_gender, this);
        Util.setOnClickListener(view, R.id.btn_select_birthday, this);

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
            case R.id.btn_set_gender:
                List<ListPopupItem> itemList = new ArrayList<>();
                for (int i = 0; i < genderTextMap.length; i++) {
                    ListPopupItem item = new ListPopupItem(i, genderTextMap[i], null);
                    itemList.add(item);
                }

                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new ListPopup(_mActivity, getResources().getString(R.string.text_select_gender),
                                Constant.POPUP_TYPE_DEFAULT, itemList, genderIndex, this))
                        .show();
                break;
            case R.id.btn_select_birthday:
                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new DateSelectPopup(_mActivity, birthday, this))
                        .show();
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

                    EasyJSONObject memberInfo = responseObj.getObject("datas.memberInfo");

                    TextView tvUserName = view.findViewById(R.id.tv_user_name);
                    tvUserName.setText(memberInfo.getString("memberName"));

                    TextView tvNickname = view.findViewById(R.id.tv_nickname);
                    nickname = memberInfo.getString("nickName");
                    tvNickname.setText(nickname);

                    int gender = memberInfo.getInt("memberSex");
                    genderIndex = gender;
                    tvGender = view.findViewById(R.id.tv_gender);
                    tvGender.setText(genderTextMap[gender]);

                    birthday = memberInfo.getString("birthday");
                    if (!StringUtil.isEmpty(birthday)) {
                        tvBirthday = view.findViewById(R.id.tv_birthday);
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

    @Override
    public void onSelected(int type, final int id, Object extra) {
        SLog.info("type[%d]", type);

        if (type == Constant.POPUP_TYPE_DEFAULT) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "memberSex", id);
            Api.postUI(Api.PATH_SET_GENDER, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    genderIndex = id;
                    tvGender.setText(genderTextMap[genderIndex]);
                }
            });
        } else if (type == Constant.POPUP_TYPE_DATE) {
            SLog.info("extra[%s]", extra);
            String birthday = (String) extra;
            tvBirthday.setText(birthday);
        }
    }
}
