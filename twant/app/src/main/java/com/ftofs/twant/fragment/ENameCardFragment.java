package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.InviteAddFriendPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class ENameCardFragment extends BaseFragment implements View.OnClickListener {
    private String memberName;
    private boolean isMyself=false;

    @BindView(R.id.img_avatar)
    ImageView avatar;
    @BindView(R.id.tv_name)
    TextView tvName;

    @BindView(R.id.icon_follow)
    ImageView iconFollow;
    @BindView(R.id.tv_follow)
    TextView tvFollow;
    @BindView(R.id.icon_add_or_chat)
    ImageView iconAddOrChat;
    @BindView(R.id.tv_add_or_chat)
    TextView tvAddOrChat;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumeber;
    @BindView(R.id.tv_wechat)
    TextView tvWechatId;
    @BindView(R.id.tv_facebook_id)
    TextView tvFaceBookId;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_personal_bio)
    TextView tvPersonalBio;
    @BindView(R.id.icon_sex_mini)
    ImageView iconSex;
    @BindView(R.id.img_enc_background)
    ImageView imgBackground;
    @BindView(R.id.btn_follow)
    RelativeLayout btnFollow;
    @BindView(R.id.btn_add_friend)
    RelativeLayout btnAddFriend;
    @BindView(R.id.btn_goto_home)
    RelativeLayout btnGotoHome;
    @BindView(R.id.tv_edit)
    TextView btnEdit;
    private int isFollow;
    private int memberSex;
    private int isFriend;
    private List<ListPopupItem> academicDiplomasList=new ArrayList<>();
    private String nickName;
    private String avatarUrl;
    EasyJSONArray experienceList;
    private EasyJSONArray educationList;
    private EasyJSONArray certificateList;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Unbinder unbinder;

    public static ENameCardFragment newInstance(String memberName) {
        Bundle args = new Bundle();

        ENameCardFragment fragment = new ENameCardFragment();
        fragment.memberName = memberName;
        fragment.setArguments(args);

        return fragment;
    }
    public static ENameCardFragment newInstance() {
        Bundle args = new Bundle();

        ENameCardFragment fragment = new ENameCardFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_e_name_card, container, false);
        unbinder = ButterKnife.bind( this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_edit, this);
        Util.setOnClickListener(view,R.id.btn_follow,this);
        Util.setOnClickListener(view,R.id.btn_add_friend,this);
        Util.setOnClickListener(view,R.id.btn_goto_home,this);

        loadENameInfo(view);
        hideSkillInfo(view);

    }

    private void hideSkillInfo(View view) {
        view.findViewById(R.id.ll_honor_container).setVisibility(View.GONE);
        view.findViewById(R.id.rv_honor_title).setVisibility(View.GONE);
        view.findViewById(R.id.rv_skill_title).setVisibility(View.GONE);
        view.findViewById(R.id.ll_skill_container).setVisibility(View.GONE);
    }

    private void loadENameInfo(View view) {
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        if (StringUtil.isEmpty(memberName)) {
            //為空就是打開用戶自己的名片
            isMyself = true;
            memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, "");
        }
        EasyJSONObject params = EasyJSONObject.generate("memberName", memberName,"token",User.getToken());
        String token = User.getToken();
        String url = Api.MEMBER_CARD_DETAIL + "?token=" + token + "&memberName=" + memberName;
        SLog.info(params.toString());
        Api.getUI(Api.MEMBER_CARD_DETAIL, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                try {
                    EasyJSONObject memberCardVo = responseObj.getSafeObject("datas.memberCardVo");
                    avatarUrl = memberCardVo.getSafeString("avatarUrl");
                    String trueName = memberCardVo.getSafeString("trueName");
                    nickName = memberCardVo.getSafeString("nickName");
                    if (!StringUtil.isEmpty(trueName)) {
                        nickName = trueName;
                    }

                    tvName.setText(nickName);
                    String mobile = memberCardVo.getSafeString("mobile");
                    tvPhoneNumeber.setText(mobile);
                    String wechat = memberCardVo.getSafeString("wechat");
                    tvWechatId.setText(wechat);
                    String memberBio = memberCardVo.getSafeString("memberBio");
                    tvPersonalBio.setText(memberBio);
                    String email = memberCardVo.getSafeString("email");
                    tvEmail.setText(email);
                    String companyName = memberCardVo.getSafeString("companyName");
                    tvCompany.setText(companyName);
                    String facebook = memberCardVo.getSafeString("facebook");
                    tvFaceBookId.setText(facebook);
                    String resumeWish = memberCardVo.getSafeString("resumeWish");
                    String memberAddress = memberCardVo.getSafeString("memberAddress");
                    tvAddress.setText(memberAddress);
                    isFriend = memberCardVo.getInt("isFriend");
                    updateFriendStatus();
                    updateFollowStatus();
                    isFollow = memberCardVo.getInt("isFollow");
                    int isMe = memberCardVo.getInt("isMe");
                    updateIsMe(isMe);
                    educationList = memberCardVo.getSafeArray("educationList");
                    experienceList = memberCardVo.getSafeArray("experienceList");
                    certificateList = memberCardVo.getSafeArray("certificateList");
                    EasyJSONArray academicDiplomas = responseObj.getSafeArray("datas.academicDiplomasList");
                    academicDiplomasList.clear();
                    for (int i = 0; i < academicDiplomas.length(); i++) {
                        academicDiplomasList.add(new ListPopupItem(academicDiplomas.getObject(i).getInt("academicId"), academicDiplomas.getObject(i).getSafeString("academicDiplomas"), null));
                    }
                    MemberResumeFragment.initExperienceListView(_mActivity,view,experienceList,isMyself);
                    MemberResumeFragment.initEducationListView(_mActivity,view,educationList,academicDiplomasList,isMyself);
                    MemberResumeFragment.initCertificateListView(_mActivity,view,certificateList,isMyself);
                    memberSex = memberCardVo.getInt("memberSex");
                    updateSex();
                    Glide.with(view).load(avatarUrl).into(avatar);


                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void updateIsMe(int isMe) {
        isMyself = isMe == 1;
        if (isMyself) {
            getView().findViewById(R.id.btn_follow).setVisibility(View.GONE);
            getView().findViewById(R.id.btn_add_friend).setVisibility(View.GONE);
            getView().findViewById(R.id.btn_goto_home).setVisibility(View.GONE);
            getView().findViewById(R.id.btn_edit).setVisibility(View.VISIBLE);
        }
    }

    private void updateSex() {
        if (memberSex == 1) {//0保密1男2女
            imgBackground.setImageResource(R.drawable.enc_bg_gender_male);
            iconSex.setImageResource(R.drawable.icon_enc_male);
        } else if (memberSex == 2) {
            imgBackground.setImageResource(R.drawable.enc_bg_gender_female);
            iconSex.setImageResource(R.drawable.icon_enc_female);
        } else {
            imgBackground.setImageResource(R.drawable.enc_bg_gender_secret);
            iconSex.setVisibility(View.GONE);
        }
    }

    private void updateFriendStatus() {
        btnAddFriend.setVisibility(isFriend==1?View.GONE:View.VISIBLE);
        btnGotoHome.setVisibility(isFriend==1?View.VISIBLE:View.GONE);
    }

    private void updateFollowStatus() {
        if (isFollow == 1) {
            tvFollow.setText("已關注");
//            Glide.with(getView()).load(R.drawable.icon_follow_post_small);
        } else {
            tvFollow.setText("關注");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_edit) {
            Util.startFragment(MemberResumeFragment.newInstance());
        } else if (id == R.id.btn_follow) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            // 加關注或取消關注
            EasyJSONObject params = EasyJSONObject.generate(
                    "memberName", memberName,
                    "state", 1 - isFollow,
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

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        isFollow = 1 - isFollow;
                        if (isFollow == 1) {
                            ToastUtil.success(_mActivity, "關注成功");
                            tvFollow.setText(getString(R.string.text_followed));
                        } else {
                            ToastUtil.success(_mActivity, "取消關注成功");
                            tvFollow.setText(getString(R.string.text_follow));
                        }
                    } catch (Exception e) {

                    }
                }
            });
        } else if (id == R.id.btn_add_friend
        ) {
            new XPopup.Builder(_mActivity)
                    .autoOpenSoftInput(true)
                    .asCustom(new InviteAddFriendPopup(_mActivity, memberName))
                    .show();

        } else if (id == R.id.btn_goto_home) {
            // 訪問專頁
            start(TestFriendFragment.newInstance(memberName));

        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadENameInfo(getView());
    }
}