package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.FollowMeAvatarAdapter;
import com.ftofs.twant.adapter.MemberPostListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.entity.UniversalMemberItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SharePopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 好友專頁
 * @author gzp
 * @date 2019/11/17
 */
public class TestFriendFragment extends BaseFragment implements View.OnClickListener{
    /**
     *   人緣、想粉、想要帖數據列表
     */
    List<UniversalMemberItem> followHimList = new ArrayList<>();
    List<PostItem> postItemList = new ArrayList<>();

    FollowMeAvatarAdapter followHimAvatarAdapter;

    ImageView imgAvatar;
    TextView tvNickname;
    TextView tvMemberLevel;



    TextView tvPopularity;
    TextView tvFansCount;
    TextView tvArticleCount;

    TextView tvPersonalProfile;

    TextView btnFollow;
    /**
     * 好友的的信息
     */
    String memberName;
    String nickname;
    String avatarUrl;
    int isFollow;
    String memberProfile;
    String memberSignature;
    MemberPostListAdapter memberPostListAdapter;

    public static TestFriendFragment newInstance(String memberName){
        Bundle args = new Bundle();
        args.putString("memberName",memberName);
        TestFriendFragment fragment = new TestFriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend,container,false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        memberSignature = getString(R.string.member_signature);
        memberName = getArguments().getString("memberName");

        imgAvatar = view.findViewById(R.id.img_avatar);
        Util.setOnClickListener(view, R.id.btn_goto_my, this);
        Util.setOnClickListener(view, R.id.btn_goto_home, this);
        Util.setOnClickListener(view, R.id.btn_back,this);

        btnFollow = view.findViewById(R.id.btn_follow);
        btnFollow.setOnClickListener(this);
        Util.setOnClickListener(view,R.id.btn_want_chat,this);

        Util.setOnClickListener(view, R.id.btn_about, this);
        Util.setOnClickListener(view, R.id.btn_article, this);
        Util.setOnClickListener(view, R.id.btn_friends, this);
        Util.setOnClickListener(view, R.id.btn_interactive, this);

        Util.setOnClickListener(view, R.id.btn_show_more, this);
        Util.setOnClickListener(view, R.id.icon_expand, this);

        tvNickname = view.findViewById(R.id.tv_nickname);
        tvMemberLevel = view.findViewById(R.id.tv_member_level);
        tvPopularity = view.findViewById(R.id.tv_popularity);
        tvFansCount = view.findViewById(R.id.tv_fans_count);
        tvArticleCount = view.findViewById(R.id.tv_article_count);
        tvPersonalProfile = view.findViewById(R.id.tv_personal_sign);
        tvPersonalProfile.setOnClickListener(this);

        RecyclerView rvFollowHimList = view.findViewById(R.id.rv_follow_him_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false);
        rvFollowHimList.setLayoutManager(layoutManager);
        followHimAvatarAdapter = new FollowMeAvatarAdapter(R.layout.follow_me_avatar_item, followHimList);
        followHimAvatarAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UniversalMemberItem item = followHimList.get(position);
                String memberName = item.memberName;
                Util.startFragment(MemberInfoFragment.newInstance(memberName));
            }
        });
        rvFollowHimList.setAdapter(followHimAvatarAdapter);

        RecyclerView rvPostList = view.findViewById(R.id.rv_post_list);
        memberPostListAdapter = new MemberPostListAdapter(postItemList);
        rvPostList.setLayoutManager(new LinearLayoutManager(_mActivity));
        memberPostListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostItem postItem = postItemList.get(position);
                if(postItem.itemType==PostItem.POST_TYPE_MESSAGE){
                    Util.startFragment(GoodsDetailFragment.newInstance(postItem.commonId,0));
                }else{
                    Util.startFragment(PostDetailFragment.newInstance(postItem.postId));
                }
            }
        });
        memberPostListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                PostItem postItem = postItemList.get(position);

                if (id == R.id.btn_thumb) {
                    switchThumbState(position);
                } else if (id == R.id.btn_fav) {

                } else {
                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new SharePopup(_mActivity, SharePopup.generatePostShareLink(postItem.postId), postItem.title, "", postItem.coverImage, null))
                            .show();
                }
            }
        });
        rvPostList.setAdapter(memberPostListAdapter);

        loadMemberData();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_goto_home:
                this.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.HOME_FRAGMENT);
                break;
            case R.id.btn_goto_my:
                this.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.MY_FRAGMENT);
                break;
            case R.id.btn_want_chat:
                FriendInfo.upsertFriendInfo(memberName, nickname, avatarUrl, ChatUtil.ROLE_MEMBER);
                FriendInfo friendInfo = new FriendInfo();
                friendInfo.memberName = memberName;
                friendInfo.nickname = nickname;
                friendInfo.avatarUrl = avatarUrl;

                friendInfo.role = ChatUtil.ROLE_MEMBER;
                Util.startFragment(ChatFragment.newInstance(ChatUtil.getConversation(memberName, nickname, avatarUrl, ChatUtil.ROLE_MEMBER), friendInfo));
                break;
            case R.id.btn_follow:
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
                                btnFollow.setText(getString(R.string.text_followed));
                            } else {
                                ToastUtil.success(_mActivity, "取消關注成功");
                                btnFollow.setText(getString(R.string.text_follow));
                            }
                        } catch (Exception e) {
                        }
                    }
                });
                break;
            case R.id.btn_about:
                Util.startFragment(MemberInfoFragment.newInstance(memberName));
                break;
            case R.id.btn_article:
                Util.startFragment(MyArticleFragment.newInstance(memberName));
                break;
            case R.id.btn_friends:
                Util.startFragment(MyFriendFragment.newInstance(memberName));
                break;
            case R.id.btn_interactive:
                Util.startFragment(InteractiveFragment.newInstance(memberName));
                break;
            case R.id.tv_personal_profile:
                SLog.info("點到了好友的個性簽名");
                String profile = memberProfile;
                if (StringUtil.isEmpty(profile)) {
                    profile = "";
                }
                Util.startFragment(PersonalProfileFragment.newInstance(profile,memberName));
                break;
            // 點擊兩個地方都顯示【想粉】Fragment
            case R.id.btn_show_more:
            case R.id.icon_expand:
                Util.startFragment(FollowMeFragment.newInstance(followHimList));
                break;
            default:
                break;
        }
    }
    /**
     * 加載用戶數據
     */
    private void loadMemberData() {
        String token = User.getToken();
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "memberName", memberName);
        SLog.info("params[%s]",params);

        Api.postUI(Api.PATH_MEMBER_PAGE, params, new UICallback() {
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
//注意這裡更改了字段為avatar
                    avatarUrl = responseObj.getSafeString("datas.memberVo.avatar");
                    SLog.info("avatarUrl[%s]",avatarUrl);
                    if (!StringUtil.isEmpty(avatarUrl)) {
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAvatar);
                        imgAvatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!avatarUrl.endsWith(".gif")) { // 顯示大圖
                                    SLog.info("點擊顯示大圖");
                                    start(ImageViewerFragment.newInstance(StringUtil.normalizeImageUrl(avatarUrl)));
                                }

                            }
                        });
                        //Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAuthorAvatar);
                    }

                    nickname = responseObj.getSafeString("datas.memberVo.nickName");
                    SLog.info("nickname[%s]",nickname);

                    tvNickname.setText(nickname);

                    tvMemberLevel.setText(responseObj.getSafeString("datas.memberVo.currGrade.gradeName") + getString(R.string.text_member));

                    isFollow = responseObj.getInt("datas.memberVo.follow");
                    if (isFollow == 1) {
                        btnFollow.setText(getString(R.string.text_followed));
                    } else {
                        btnFollow.setText(getString(R.string.text_follow));
                    }
                    tvPopularity.setText(String.valueOf(responseObj.getInt("datas.memberHomeStat.popularity")));
                    tvFansCount.setText(String.valueOf(responseObj.getInt("datas.memberHomeStat.follow")));
                    tvArticleCount.setText(String.valueOf(responseObj.getInt("datas.memberHomeStat.post")));

                    memberProfile = responseObj.getSafeString("datas.memberVo.memberBio");
                    if (StringUtil.isEmpty(memberSignature)) {
                        tvPersonalProfile.setText(getString(R.string.input_personal_sign_hint));
                    } else {
                        tvPersonalProfile.setText(memberSignature);
                    }

                    // 【想粉】數據
                    followHimList.clear();
                    EasyJSONArray fansList = responseObj.getSafeArray("datas.fansList");
                    for (Object object : fansList) {
                        EasyJSONObject fans = (EasyJSONObject) object;

                        UniversalMemberItem item = new UniversalMemberItem();
                        item.avatarUrl = fans.getSafeString("avatar");
                        item.memberName = fans.getSafeString("memberName");
                        item.nickname = fans.getSafeString("nickName");
                        item.memberSignature = fans.getSafeString("memberSignature");
                        followHimList.add(item);
                    }

                    followHimAvatarAdapter.setNewData(followHimList);


                    EasyJSONArray wantPostVoList = responseObj.getSafeArray("datas.wantPostVoList");
                    SLog.info("responseStr好友專頁數據[%s]", wantPostVoList);
                    for (Object object : wantPostVoList) {
                        EasyJSONObject wantPostVo = (EasyJSONObject) object;

                        PostItem postItem = new PostItem();
                        postItem.postId = wantPostVo.getInt("postId");
                        postItem.itemType = wantPostVo.getInt("postType");
                        if(postItem.itemType==PostItem.POST_TYPE_MESSAGE){
                            postItem.title = wantPostVo.getSafeString("title");
                            EasyJSONObject storeVo = wantPostVo.getSafeObject("storeVo");
                            postItem.shopAvatar = storeVo.getSafeString("storeAvatarUrl");
                            postItem.storeName = storeVo.getSafeString("storeName");
                            EasyJSONObject goods = wantPostVo.getSafeObject("goodsCommon");
                            postItem.goodsName = goods.getSafeString("goodsName");
                            postItem.goodsPrice = Double.toString(goods.getDouble("appPrice0"));
                            postItem.goodsimage = goods.getSafeString("imageName");
                            postItem.commonId = goods.getInt("commonId");
                            postItem.goodsModal = StringUtil.safeModel(goods);
                        }else{
                            postItem.coverImage = wantPostVo.getSafeString("coverImage");
                            postItem.title = wantPostVo.getSafeString("postCategory") + " | " + wantPostVo.getSafeString("title");
                            EasyJSONObject memberVo = wantPostVo.getSafeObject("memberVo");
                            postItem.authorAvatar = memberVo.getSafeString("avatar");
                            postItem.authorNickname = memberVo.getSafeString("nickName");
                            postItem.createTime = wantPostVo.getSafeString("createTime");
                            postItem.postThumb = wantPostVo.getInt("postLike");
                            postItem.postFollow = wantPostVo.getInt("postFavor");
                            postItem.postReply = wantPostVo.getInt("postReply");
                            postItem.content = wantPostVo.getSafeString("content");
                        }


                        postItemList.add(postItem);
                    }

                    SLog.info("postItemList.size[%d]", postItemList.size());
                    memberPostListAdapter.setNewData(postItemList);
                } catch (Exception e) {

                }
            }
        });
    }

    private void switchThumbState(int position) {
        SLog.info("switchInteractiveState[%d]", position);
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            Util.showLoginFragment();
            return;
        }

        PostItem postItem = postItemList.get(position);
        int newState = 1 - postItem.isLike;

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "postId", postItem.postId,
                "state", newState);

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_POST_THUMB, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                postItem.isLike = newState;
                if (newState == 1) {
                    postItem.postThumb++;
                } else {
                    postItem.postThumb--;
                }

                memberPostListAdapter.notifyItemChanged(position);
            }
        });
    }
    @Override
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }
}
