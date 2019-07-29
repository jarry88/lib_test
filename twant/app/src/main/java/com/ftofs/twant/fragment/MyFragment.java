package com.ftofs.twant.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.FollowMeAvatarAdapter;
import com.ftofs.twant.adapter.OrderListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.QuickClickButton;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 我的
 * @author zwm
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    // 關注我的頭像列表
    List<String> followMeList = new ArrayList<>();

    BaseQuickAdapter adapter;

    ImageView imgAvatar;
    TextView tvNickname;
    TextView tvMemberLevel;
    TextView tvCartItemCount;
    TextView tvPopularity;
    TextView tvFansCount;
    TextView tvArticleCount;

    TextView tvPersonalProfile;

    boolean userDataLoaded;

    String personalProfile;
    String inputPersonalProfileHint;

    public static MyFragment newInstance() {
        Bundle args = new Bundle();

        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        inputPersonalProfileHint = getString(R.string.input_personal_profile_hint);

        imgAvatar = view.findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);
        Util.setOnClickListener(view, R.id.btn_setting, this);

        Util.setOnClickListener(view, R.id.btn_mall, this);
        Util.setOnClickListener(view, R.id.btn_friends, this);
        Util.setOnClickListener(view, R.id.btn_interactive, this);

        QuickClickButton btnQuickClick = view.findViewById(R.id.btn_quick_click);
        btnQuickClick.setOnQuickClickListener(new QuickClickButton.OnQuickClickListener() {
            @Override
            public void onQuickClick(View view) {
                new XPopup.Builder(getContext())
//                        .maxWidth(600)
                        .asCenterList("請選擇操作", new String[] {"顯示Fragment棧", "顯示調試頁面"},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        SLog.info("position[%d], text[%s]", position, text);
                                        if (position == 0) {

                                        } else {
                                            Util.startFragment(DebugFragment.newInstance());
                                        }
                                    }
                                })
                        .show();
            }
        });

        tvNickname = view.findViewById(R.id.tv_nickname);
        tvMemberLevel = view.findViewById(R.id.tv_member_level);
        tvCartItemCount = view.findViewById(R.id.tv_cart_item_count);
        tvPopularity = view.findViewById(R.id.tv_popularity);
        tvFansCount = view.findViewById(R.id.tv_fans_count);
        tvArticleCount = view.findViewById(R.id.tv_article_count);
        tvPersonalProfile = view.findViewById(R.id.tv_personal_profile);
        tvPersonalProfile.setOnClickListener(this);

        RecyclerView rvFollowMeList = view.findViewById(R.id.rv_follow_me_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false);
        rvFollowMeList.setLayoutManager(layoutManager);
        adapter = new FollowMeAvatarAdapter(R.layout.follow_me_avatar_item, followMeList);
        rvFollowMeList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_REFRESH_DATA) {
            loadUserData();
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_LOGOUT_SUCCESS) { // 如果用戶退出登錄，重新加載數據
            userDataLoaded = false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.img_avatar:
                Util.startFragment(PersonalInfoFragment.newInstance());
                break;
            case R.id.btn_mall:
                Util.startFragment(MallFragment.newInstance());
                break;
            case R.id.btn_friends:
                Util.startFragment(ContactFragment.newInstance());
                break;
            case R.id.btn_interactive:
                Util.startFragment(InteractiveFragment.newInstance());
                break;
            case R.id.btn_setting:
                Util.startFragment(UniversalFragment.newInstance());
                break;
            case R.id.tv_personal_profile:
                String profile = personalProfile;
                if (StringUtil.isEmpty(profile)) {
                    profile = "";
                }
                Util.startFragment(PersonalProfileFragment.newInstance(profile));
                break;
            default:
                break;
        }
    }

    /**
     * 加載用戶數據
     */
    private void loadUserData() {
        String token = User.getToken();
        String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);

        if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "memberName", memberName);

        Api.postUI(Api.PATH_USER_DATA, params, new UICallback() {
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

                    String avatarUrl = responseObj.getString("datas.memberVo.avatarUrl");
                    if (!StringUtil.isEmpty(avatarUrl)) {
                        Glide.with(_mActivity).load(avatarUrl).centerCrop().into(imgAvatar);
                    }

                    String nickname = responseObj.getString("datas.memberVo.nickName");
                    tvNickname.setText(nickname);

                    tvMemberLevel.setText(responseObj.getString("datas.memberVo.currGrade.gradeName") + getString(R.string.text_member));
                    tvCartItemCount.setText(String.valueOf(responseObj.getInt("datas.memberHomeStat.cartList")));
                    tvPopularity.setText(String.valueOf(responseObj.getInt("datas.memberHomeStat.popularity")));
                    tvFansCount.setText(String.valueOf(responseObj.getInt("datas.memberHomeStat.follow")));
                    tvArticleCount.setText(String.valueOf(responseObj.getInt("datas.memberHomeStat.post")));

                    personalProfile = responseObj.getString("datas.memberVo.memberBio");
                    if (StringUtil.isEmpty(personalProfile)) {
                        tvPersonalProfile.setText(inputPersonalProfileHint);
                    } else {
                        tvPersonalProfile.setText(personalProfile);
                    }

                    // 【關注我的】數據
                    followMeList.clear();
                    EasyJSONArray fansList = responseObj.getArray("datas.fansList");
                    for (Object object : fansList) {
                        EasyJSONObject fans = (EasyJSONObject) object;
                        String avatar = fans.getString("avatar");
                        followMeList.add(avatar);
                    }

                    for (int i = 0; i < 20; i++) {
                        // followMeList.add("https://www.snailpad.cn/tmp/timg.jpg");
                    }


                    adapter.setNewData(followMeList);

                    userDataLoaded = true;
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (!userDataLoaded) {
            loadUserData();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
