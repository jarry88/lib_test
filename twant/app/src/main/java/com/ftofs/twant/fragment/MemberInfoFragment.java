package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.InviteAddFriendPopup;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 城友信息Fragment
 * @author zwm
 */
/**
 *更改處loadMemberInfo().onResponse(),267行，
 * 增加isFriend判斷
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

    public static MemberInfoFragment newInstance(String memberName) {
        Bundle args = new Bundle();

        args.putString("memberName", memberName);
        MemberInfoFragment fragment = new MemberInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_chat_with_him, this);

        loadMemberInfo();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_chat_with_him) {
            FriendInfo.upsertFriendInfo(memberName, nickname, avatarUrl, ChatUtil.ROLE_MEMBER);
            FriendInfo friendInfo = new FriendInfo();
            friendInfo.memberName = memberName;
            friendInfo.nickname = nickname;
            friendInfo.avatarUrl = avatarUrl;
            friendInfo.role = ChatUtil.ROLE_MEMBER;
            Util.startFragment(ChatFragment.newInstance(ChatUtil.getConversation(memberName, nickname, avatarUrl, ChatUtil.ROLE_MEMBER), friendInfo));
        } else if (id == R.id.btn_follow) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            // 加關注或取消關注
            String url = Api.PATH_MEMBER_FOLLOW;
            EasyJSONObject params = EasyJSONObject.generate(
                    "memberName", memberName,
                    "state", 1 - isFollow,
                    "token", token
            );
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                            return;
                        }

                        isFollow = 1 - isFollow;
                        if (isFollow == 1) {
                            ToastUtil.success(_mActivity, "關注成功");
                            btnFollow.setText(getString(R.string.text_followed));
                        } else {
                            ToastUtil.success(_mActivity, "取消關注成功");
                            btnFollow.setText(getString(R.string.text_follow));
                        }
                    } catch (Exception e) {

                    }
                }
            });

        } else if (id == R.id.btn_add_friend) {
            if (isFriend == 0) { // 添加好友
                new XPopup.Builder(_mActivity)
                        .autoOpenSoftInput(true)
                        .asCustom(new InviteAddFriendPopup(_mActivity, memberName))
                        .show();
            } else { // 訪問專頁
                start(TestFriendFragment.newInstance(memberName));
            }
        }
    }

    private void loadMemberInfo() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_MEMBER_INFO;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "memberName", memberName
        );

        SLog.info("params[%s]", params.toString());

        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    isFollow = responseObj.getInt("datas.isFollow");
                    if (isFollow == 1) {
                        btnFollow.setText(getString(R.string.text_followed));
                    }

                    isFriend = responseObj.getInt("datas.isFriend");
                    if(isFriend == 1){
                        btnAddFriend.setText("訪問專頁");
                    }
                    EasyJSONObject member = responseObj.getSafeObject("datas.member");

                    avatarUrl = StringUtil.normalizeImageUrl(member.getSafeString("avatarUrl"));

                    if (StringUtil.useDefaultAvatar(avatarUrl)) {
                        Glide.with(_mActivity).load(R.drawable.grey_default_avatar).centerCrop().into(imageAvatar);
                    } else {
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imageAvatar);
                    }

                    nickname = member.getSafeString("nickName");
                    tvNickname.setText(nickname);
                    tvLocation.setText(member.getSafeString("addressAreaInfo"));
                    tvMemberSignature.setText(member.getSafeString("memberSignature"));


                    EasyJSONObject memberHomeState = responseObj.getSafeObject("datas.memberHomeState");
                    tvPopularity.setText(String.valueOf(memberHomeState.getInt("popularity")));
                    tvFollowCount.setText(String.valueOf(memberHomeState.getInt("follow")));
                    tvArticleCount.setText(String.valueOf(memberHomeState.getInt("post")));
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
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
