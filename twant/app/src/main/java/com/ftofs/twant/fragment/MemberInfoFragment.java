package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.InviteAddFriendPopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 會員信息Fragment
 * @author zwm
 */
public class MemberInfoFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 對方的信息
     */
    String memberName;
    String avatarUrl;
    String nickname;

    int isFollow;
    int isFriend;

    ImageView imageAvatar;
    TextView tvNickname;
    TextView tvLocation;
    TextView tvMemberSignature;

    TextView tvPopularity;
    TextView tvFollowCount;
    TextView tvArticleCount;

    TextView btnFollow;
    TextView btnAddFriend;
    TextView btnDeleteFriend;

    public static MemberInfoFragment newInstance(String memberName) {
        Bundle args = new Bundle();

        args.putString("memberName", memberName);
        MemberInfoFragment fragment = new MemberInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        memberName = args.getString("memberName");

        imageAvatar = view.findViewById(R.id.img_avatar);
        imageAvatar.setOnClickListener(this);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvLocation = view.findViewById(R.id.tv_location);
        tvMemberSignature = view.findViewById(R.id.tv_member_signature);

        tvPopularity = view.findViewById(R.id.tv_popularity);
        tvFollowCount = view.findViewById(R.id.tv_follow_count);
        tvArticleCount = view.findViewById(R.id.tv_article_count);

        btnFollow = view.findViewById(R.id.btn_follow);
        btnFollow.setOnClickListener(this);
        btnAddFriend = view.findViewById(R.id.btn_add_friend);
        btnAddFriend.setOnClickListener(this);
        btnDeleteFriend = view.findViewById(R.id.btn_delete_friend);
        btnDeleteFriend.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_chat_with_him, this);

        loadMemberInfo();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_chat_with_him) {
            Util.startFragment(ChatFragment.newInstance(ChatUtil.getConversation(memberName, nickname, avatarUrl, ChatUtil.ROLE_MEMBER)));
        } else if (id == R.id.btn_follow) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }
            if (isFollow == 0) {
                // 加關注
                EasyJSONObject params = EasyJSONObject.generate(
                        "memberName", memberName,
                        "state", 1,
                        "token", token
                );
                Api.postUI(Api.PATH_MEMBER_FOLLOW, params, new UICallback() {
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

                            ToastUtil.success(_mActivity, "關注成功");
                            isFollow = 1;
                            btnFollow.setText(getString(R.string.text_followed));
                        } catch (Exception e) {

                        }
                    }
                });
            }
        } else if (id == R.id.btn_add_friend) {
            if (isFriend == 0) { // 添加好友
                new XPopup.Builder(_mActivity)
                        .autoOpenSoftInput(true)
                        .asCustom(new InviteAddFriendPopup(_mActivity, memberName))
                        .show();
            } else { // 訪問專頁

            }
        } else if (id == R.id.btn_delete_friend) {
            new XPopup.Builder(_mActivity)
//                     .dismissOnTouchOutside(false)
                    // 设置弹窗显示和隐藏的回调监听
//                     .autoDismiss(false)
                    .setPopupCallback(new XPopupCallback() {
                        @Override
                        public void onShow() {
                        }
                        @Override
                        public void onDismiss() {
                        }
                    }).asCustom(new TwConfirmPopup(_mActivity, "確認", "確定要刪除這個好友嗎？", new OnConfirmCallback() {
                @Override
                public void onYes() {
                    SLog.info("onYes");

                    String token = User.getToken();
                    if (StringUtil.isEmpty(token)) {
                        return;
                    }

                    EasyJSONObject params = EasyJSONObject.generate(
                            "token", token,
                            "friendMemberName", memberName);

                    SLog.info("params[%s]", params.toString());
                    Api.postUI(Api.PATH_DELETE_FRIEND, params, new UICallback() {
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

                                ToastUtil.success(_mActivity, "刪除成功");
                            } catch (Exception e) {

                            }
                        }
                    });
                }

                @Override
                public void onNo() {
                    SLog.info("onNo");
                }
            })).show();

        }
    }

    private void loadMemberInfo() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "memberName", memberName
        );

        SLog.info("params[%s]", params.toString());

        Api.postUI(Api.PATH_MEMBER_INFO, params, new UICallback() {
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

                    isFollow = responseObj.getInt("datas.isFollow");
                    if (isFollow == 1) {
                        btnFollow.setText(getString(R.string.text_followed));
                    }

                    isFriend = responseObj.getInt("datas.isFriend");
                    if (isFriend == 1) {
                        btnAddFriend.setText(getString(R.string.text_visit_my_home));
                        btnDeleteFriend.setVisibility(View.VISIBLE);
                    } else {  // 如果不是好友，隱藏【刪除好友】按鈕
                        btnDeleteFriend.setVisibility(View.GONE);
                    }

                    EasyJSONObject member = responseObj.getObject("datas.member");


                    avatarUrl = StringUtil.normalizeImageUrl(member.getString("avatarUrl"));

                    Glide.with(_mActivity).load(avatarUrl).centerCrop().into(imageAvatar);
                    nickname = member.getString("nickName");
                    tvNickname.setText(nickname);
                    tvLocation.setText(member.getString("addressAreaInfo"));
                    tvMemberSignature.setText(member.getString("memberSignature"));


                    EasyJSONObject memberHomeState = responseObj.getObject("datas.memberHomeState");
                    tvPopularity.setText(String.valueOf(memberHomeState.getInt("popularity")));
                    tvFollowCount.setText(String.valueOf(memberHomeState.getInt("follow")));
                    tvArticleCount.setText(String.valueOf(memberHomeState.getInt("post")));


                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
