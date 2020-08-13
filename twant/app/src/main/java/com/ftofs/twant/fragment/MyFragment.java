package com.ftofs.twant.fragment;


import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.FollowMeAvatarAdapter;
import com.ftofs.twant.adapter.MemberPostListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.entity.UniversalMemberItem;
import com.ftofs.twant.interfaces.CommonCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.CameraUtil;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.HawkUtil;
import com.ftofs.twant.util.PermissionUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.member.MemberVo;
import com.ftofs.twant.widget.BottomConfirmPopup;
import com.ftofs.twant.widget.QuickClickButton;
import com.ftofs.twant.widget.SharePopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.XPopupCallback;
import com.orhanobut.hawk.Hawk;
import com.yanzhenjie.permission.runtime.Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 我的
 * @author zwm
 */
public class MyFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener,BaseQuickAdapter.RequestLoadMoreListener {
    // 【想粉】數據列表
    List<UniversalMemberItem> followMeList = new ArrayList<>();
    List<PostItem> postItemList = new ArrayList<>();

    FollowMeAvatarAdapter followMeAvatarAdapter;
    NestedScrollView containerView;
    int containerViewHeight = 0;

    ImageView imgPersonalBackground;

    ImageView imgAvatar;
    ImageView imgAuthorAvatar;
    TextView tvNickname;
    TextView tvMemberLevel;
    TextView tvPopularity;
    TextView tvFansCount;
    TextView tvArticleCount;

    TextView tvMemberSignature;

    boolean userDataLoaded;

    String memberSignature;
    String inputMemberSignatureHint;
    RecyclerView rvPostList;
    MemberPostListAdapter memberPostListAdapter;
    int currPage=1;
    boolean hasMore=true;

    RelativeLayout rlFollowMeList;

    /**
     * 拍照的圖片文件
     */
    File captureImageFile;

    /**
     * 改變了個人背景圖
     */
    boolean personalBackgroundChanged = false;
    private TextView tvShoppingMessageCount;
    private int PostCountMax=10;
    private boolean showBack;
    private RecyclerView rvFollowMeList;
    private FrameLayout btnSeller;
    private boolean showSeller;

    public static MyFragment newInstance() {
        Bundle args = new Bundle();

        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static MyFragment newInstance(boolean showBack) {
        Bundle args = new Bundle();

        MyFragment fragment = new MyFragment();
        fragment.showBack = showBack;
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

        inputMemberSignatureHint = getString(R.string.member_signature);

        imgAvatar = view.findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);
        imgAuthorAvatar = view.findViewById(R.id.img_author_avatar);

        containerView = view.findViewById(R.id.container_view);


        Util.setOnClickListener(view, R.id.btn_setting, this);
        btnSeller = view.findViewById(R.id.btn_goto_seller);
        btnSeller.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_back_round, this);
        view.findViewById(R.id.btn_back_round).setVisibility(showBack ? View.VISIBLE : View.GONE);

        Util.setOnClickListener(view, R.id.btn_mall, this);
        Util.setOnClickListener(view, R.id.btn_article, this);
        Util.setOnClickListener(view, R.id.btn_friends, this);
        Util.setOnClickListener(view, R.id.btn_interactive, this);
        Util.setOnClickListener(view,R.id.btn_want_job,this);

        Util.setOnClickListener(view, R.id.btn_publish_post, this);
        Util.setOnClickListener(view, R.id.btn_show_more, this);
        Util.setOnClickListener(view, R.id.icon_expand, this);

        imgPersonalBackground = view.findViewById(R.id.img_personal_background);
        imgPersonalBackground.setOnClickListener(this);

        rlFollowMeList = view.findViewById(R.id.rl_follow_me_list);
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
        tvShoppingMessageCount = view.findViewById(R.id.tv_shopping_message_item_count);
        tvPopularity = view.findViewById(R.id.tv_popularity);
        tvFansCount = view.findViewById(R.id.tv_fans_count);
        tvArticleCount = view.findViewById(R.id.tv_article_count);
        tvMemberSignature = view.findViewById(R.id.tv_member_signature);
        tvMemberSignature.setOnClickListener(this);


        rvFollowMeList = view.findViewById(R.id.rv_follow_me_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false);
        rvFollowMeList.setLayoutManager(layoutManager);
        followMeAvatarAdapter = new FollowMeAvatarAdapter(R.layout.follow_me_avatar_item, followMeList);
        followMeAvatarAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UniversalMemberItem item = followMeList.get(position);
                String memberName = item.memberName;
                Util.startFragment(MemberInfoFragment.newInstance(memberName));
            }
        });
        rvFollowMeList.setAdapter(followMeAvatarAdapter);

        rvPostList = view.findViewById(R.id.rv_post_list);
        SLog.info("isNestedScrollingEnabled[%s]", rvPostList.isNestedScrollingEnabled());
        rvPostList.setNestedScrollingEnabled(false);
        memberPostListAdapter = new MemberPostListAdapter(postItemList);
        rvPostList.setLayoutManager(new LinearLayoutManager(_mActivity));
        memberPostListAdapter.setEnableLoadMore(true);
        memberPostListAdapter.setOnLoadMoreListener(this,rvPostList);
        memberPostListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostItem postItem = postItemList.get(position);
                if (postItem.itemType == PostItem.POST_TYPE_MESSAGE){
                    Util.startFragment(GoodsDetailFragment.newInstance(postItem.commonId,0));
                }
                else{
                    Util.startFragment(PostDetailFragment.newInstance(postItem.postId));
                }

            }
        });
        memberPostListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                PostItem postItem = postItemList.get(position);
                if(postItem.itemType == PostItem.POST_TYPE_MESSAGE){
                    Util.startFragment(GoodsDetailFragment.newInstance(postItem.commonId,0));
                }
                else{
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

            }
        });
        rvPostList.setAdapter(memberPostListAdapter);

        containerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int rvPostListY = Util.getYOnScreen(rvPostList);
                int containerViewY = Util.getYOnScreen(containerView);

//                SLog.info("rvPostListY[%s], containerViewY[%s]", rvPostListY, containerViewY);
                if (rvPostListY <= containerViewY) {  // 如果列表滑动到顶部，则启用嵌套滚动
                    rvPostList.setNestedScrollingEnabled(true);
                } else {
                    rvPostList.setNestedScrollingEnabled(false);
                }
            }
        });
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
            //主動清空占位圖
            Glide.with(_mActivity).load(R.drawable.my_fragment_header_bg).centerCrop().into(imgPersonalBackground);
            Glide.with(_mActivity).load(R.drawable.icon_default_avatar).centerCrop().into(imgAvatar);
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_CHANGE_PERSONAL_BACKGROUND) {
            personalBackgroundChanged = true;
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_ADD_POST) {

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
            case R.id.btn_article:
                Util.startFragment(MyArticleFragment.newInstance());
                break;
            case R.id.btn_friends:
                Util.startFragment(MyFriendFragment.newInstance());
                //Util.startFragment(TestMyFriendFragment.newInstance());
                break;
            case R.id.btn_interactive:
                Util.startFragment(InteractiveFragment.newInstance());
                break;
            case R.id.btn_want_job:
                Util.startFragment(JobInfoFragment.newInstance());
                break;
            case R.id.btn_setting:
                Util.startFragment(UniversalFragment.newInstance());
                break;
            case R.id.btn_back_round:
                hideSoftInputPop();
                break;
            case R.id.btn_goto_seller:
                Util.startFragment(SellerHomeFragment.newInstance());
                break;
            case R.id.tv_member_signature:
                String signature = memberSignature;
                if (StringUtil.isEmpty(signature)) {
                    signature = "";
                }
                Util.startFragment(EditMemberSignatureFragment.newInstance(signature));
                break;
            case R.id.btn_publish_post:
                ApiUtil.addPost(_mActivity,false);
                break;
            // 點擊兩個地方都顯示【想粉】Fragment
            case R.id.btn_show_more:
            case R.id.icon_expand:
                Util.startFragment(FollowMeFragment.newInstance(followMeList));
                break;
            case R.id.img_personal_background:
                new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                    // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                    .setPopupCallback(new XPopupCallback() {
                        @Override
                        public void onShow() {
                        }
                        @Override
                        public void onDismiss() {
                        }
                    }).asCustom(new BottomConfirmPopup(_mActivity, this))
                    .show();
                break;
            default:
                break;
        }
    }

    private void selectPersonalBackground() {
        new XPopup.Builder(getContext())
//                        .maxWidth(600)
                .asCenterList("请选择", new String[]{"從手機相冊選擇", "從相機拍攝"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                changePersonalBackground(position);
                            }
                        })
                .show();
    }

    /**
     * 加載用戶數據
     */
    private void loadUserData() {
        String token = User.getToken();
        String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
        updateMemberVo();

        if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "memberName", memberName,
                "page",currPage);
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

                    if (ToastUtil.checkError(_mActivity, responseObj,"没有更多数据了")) {
                        hasMore = false;
                        if (currPage > 1) {
                            currPage--;
                        }
                        memberPostListAdapter.loadMoreEnd();
                        return;
                    }
                    if(!userDataLoaded) {
                        SLog.info("重新加載新用戶數據");
                        String avatarUrl = User.getUserInfo(SPField.FIELD_AVATAR, null);
                        if (!StringUtil.isEmpty(avatarUrl)) {
                            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAvatar);
                            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAuthorAvatar);
                        } else {
                            Glide.with(_mActivity).load(R.drawable.grey_default_avatar).centerCrop().into(imgAvatar);
                            Glide.with(_mActivity).load(R.drawable.grey_default_avatar).centerCrop().into(imgAuthorAvatar);
                        } 

                        String nickname = Hawk.get(SPField.FIELD_NICKNAME, "");
                        tvNickname.setText(nickname);

                        if (responseObj.exists("datas.memberVo.memberBackgroundApp")) {
                            String memberBackground = responseObj.getSafeString("datas.memberVo.memberBackgroundApp");
                            //即視為空也要存放，否則在切換用戶時新用戶會使用之前用戶存的背景圖
                            Hawk.put(SPField.FIELD_PERSONAL_BACKGROUND, memberBackground);
                        }
                        setPersonalBackground();

                        tvMemberLevel.setText(responseObj.getSafeString("datas.memberVo.currGrade.gradeName") + getString(R.string.text_member));
                        tvPopularity.setText(String.valueOf(responseObj.getInt("datas.memberHomeStat.popularity")));
                        tvFansCount.setText(String.valueOf(responseObj.getInt("datas.memberHomeStat.follow")));
                        tvArticleCount.setText(String.valueOf(responseObj.getInt("datas.memberHomeStat.post")));

                        memberSignature = responseObj.getSafeString("datas.memberVo.memberSignature");
                        if (StringUtil.isEmpty(memberSignature)) {
                            tvMemberSignature.setText(inputMemberSignatureHint);
                        } else {
                            tvMemberSignature.setText(memberSignature);
                            HawkUtil.setUserData(SPField.USER_DATA_KEY_SIGNATURE, memberSignature);
                        }

                        // 【想粉】數據
                        followMeList.clear();

                    EasyJSONArray fansList = responseObj.getSafeArray("datas.fansList");
                    for (Object object : fansList) {
                        EasyJSONObject fans = (EasyJSONObject) object;

                        UniversalMemberItem item = new UniversalMemberItem();
                        item.avatarUrl = fans.getSafeString("avatar");
                        item.memberName = fans.getSafeString("memberName");
                        item.nickname = fans.getSafeString("nickName");
                        item.memberSignature = fans.getSafeString("memberSignature");
                        followMeList.add(item);
                    }
                        if (followMeList.isEmpty()) {
                            rlFollowMeList.setVisibility(View.GONE);
                        } else {
                            rvFollowMeList.setVisibility(View.VISIBLE);
                            followMeAvatarAdapter.setNewData(followMeList);
                        }

                    userDataLoaded = true;
                    }

                    if (currPage == 1) {
                        postItemList.clear();
                    }
                    EasyJSONArray wantPostVoList = responseObj.getSafeArray("datas.wantPostVoList");
                    for (Object object : wantPostVoList) {
                        EasyJSONObject wantPostVo = (EasyJSONObject) object;
                        SLog.info("wantPostVo[%s]", wantPostVo.toString());
                        PostItem postItem = new PostItem();
                        postItem.postId = wantPostVo.getInt("postId");
                        postItem.itemType = wantPostVo.getInt("postType");
                        if(postItem.itemType==PostItem.POST_TYPE_MESSAGE){
                            postItem.title = wantPostVo.getSafeString("title");
                            EasyJSONObject storeVo = wantPostVo.getSafeObject("storeVo");
                            postItem.shopAvatar = storeVo.getSafeString("storeAvatarUrl");
                            postItem.storeName = storeVo.getSafeString("storeName");
                            EasyJSONObject goods = wantPostVo.getSafeObject("goodsCommon");
                            if (!Util.isJsonObjectEmpty(goods)) {
                                postItem.goodsName = goods.getSafeString("goodsName");
                                postItem.goodsPrice = StringUtil.formatPrice(_mActivity, goods.getDouble("appPrice0"), 0);
                                postItem.goodsimage = goods.getSafeString("imageName");
                                postItem.commonId = goods.getInt("commonId");
                            }
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
                            postItem.comeTrueState = wantPostVo.getInt("comeTrueState");
                        }

                        postItemList.add(postItem);
                    }
                    SLog.info("postItemList.size[%d]", postItemList.size());
                    memberPostListAdapter.setNewData(postItemList);
                    if (wantPostVoList.length() == 0) {
                        hasMore = false;
                    } else {
                        hasMore = true;
                        currPage++;
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void updateMemberVo() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        Api.getUI(Api.PATH_SELLER_ISSELLER, EasyJSONObject.generate("token", token), new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                try {
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }
                    int isSeller = responseObj.getInt("datas.isSeller");
                    showSeller = isSeller == Constant.TRUE_INT;
                    if (!showSeller) {
                        String message = responseObj.getSafeString("datas.message");
                        SLog.info("message[%s]", message);
                    }
                    btnSeller.setVisibility(showSeller ? View.VISIBLE : View.GONE);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));

                }


            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            memberPostListAdapter.setEnableLoadMore(false);
            return;
        }
        loadUserData();
    }
    private void changePersonalBackground(int select) {
        SLog.info("select[%d]", select);
        if (select == 0) {
            openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
        } else if (select == 1) {
            PermissionUtil.actionWithPermission(_mActivity, new String[] {
                    Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE
            }, "拍攝照片/視頻需要授予", new CommonCallback() {
                @Override
                public String onSuccess(@Nullable String data) {
                    captureImageFile = CameraUtil.openCamera(_mActivity, MyFragment.this, Constant.CAMERA_ACTION_IMAGE);
                    return null;
                }

                @Override
                public String onFailure(@Nullable String data) {
                    ToastUtil.error(_mActivity, "您拒絕了授權");
                    return null;
                }
            });
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        updateMainSelectedFragment(MainFragment.MY_FRAGMENT);

        int userId = User.getUserId();
        if (userId < 1) { // 用戶未登錄，顯示登錄頁面
            Util.showLoginFragment();
            return;
        }

        currPage=1;
        loadUserData();
        loadMyShoppongCountData();
        if (personalBackgroundChanged) {
            setPersonalBackground();
            personalBackgroundChanged = false;
        }

        String nickname = Hawk.get(SPField.FIELD_NICKNAME, "");
        tvNickname.setText(nickname);

        String signature = HawkUtil.getUserData(SPField.USER_DATA_KEY_SIGNATURE, "");
        if (StringUtil.isEmpty(signature)) {
            signature = inputMemberSignatureHint;
        }
        tvMemberSignature.setText(signature);

        if (containerViewHeight == 0) {
            containerViewHeight = containerView.getHeight();
            SLog.info("containerViewHeight[%d]", containerViewHeight);

            ViewGroup.LayoutParams layoutParams = rvPostList.getLayoutParams();
            layoutParams.height = containerViewHeight;
            rvPostList.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SLog.info("onActivityResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }
        // 上傳圖片到OSS
        if (requestCode == RequestCode.OPEN_ALBUM.ordinal() || requestCode == RequestCode.CAMERA_IMAGE.ordinal()) {
            String absolutePath;
            if (requestCode == RequestCode.OPEN_ALBUM.ordinal()) {
                Uri uri = data.getData();
                absolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
            } else {
                absolutePath = captureImageFile.getAbsolutePath();  // 拍照得到的文件路徑
            }
            SLog.info("absolutePath[%s]", absolutePath);

            TaskObserver taskObserver = new TaskObserver() {
                @Override
                public void onMessage() {
                    String imageUrl = (String) message;

                    if (imageUrl == null) {
                        ToastUtil.error(_mActivity, "上傳圖片失敗");
                        return;
                    }

                    // 上傳成功
                    Hawk.put(SPField.FIELD_PERSONAL_BACKGROUND, imageUrl);
                    setPersonalBackground();
                    ToastUtil.success(_mActivity, "更改成功");
                }
            };

            TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
                @Override
                public Object doWork() {
                    File file = new File(absolutePath);
                    String imageUrl = Api.syncUploadFile(file);
                    String token = User.getToken();

                    if (StringUtil.isEmpty(token)) {
                        return null;
                    }

                    EasyJSONObject params = EasyJSONObject.generate(
                            "token", token,
                            "imageUrl", imageUrl);
                    String responseStr = Api.syncPost(Api.PATH_CHANGE_PERSONAL_BACKGROUND, params);
                    if (StringUtil.isEmpty(responseStr)) {
                        return null;
                    }
                    return imageUrl;
                }
            });
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.SELECT_PERSONAL_BACKGROUND) {
            selectPersonalBackground();
        }
    }

    /**
     * 加載訂單信息數
     */
    private void loadMyShoppongCountData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token);

        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_ORDER_COUNT, params, new UICallback() {
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

                /*
                new: 待付款
                pay: 待發貨
                finish: 已完成
                send: 待收貨
                noeval: 待評論
                 */
                try {
                    int newCount = responseObj.getInt("datas.new");
                    int payCount = responseObj.getInt("datas.pay");
                    int sendCount = responseObj.getInt("datas.send");
                    int noevalCount = responseObj.getInt("datas.noeval");
                    int sumCount =newCount+payCount+sendCount+noevalCount;
                    if (sumCount > 0) {
                        tvShoppingMessageCount.setVisibility(View.VISIBLE);
                    } else {
                        tvShoppingMessageCount.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
    private void setPersonalBackground() {
        // 是否使用默認背景圖
        String personalBackgroundUrl = Hawk.get(SPField.FIELD_PERSONAL_BACKGROUND, null);

        if (StringUtil.isEmpty(personalBackgroundUrl)) {
            Glide.with(_mActivity).load(R.drawable.my_fragment_header_bg).centerCrop().into(imgPersonalBackground);
        } else {
            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(personalBackgroundUrl)).centerCrop().into(imgPersonalBackground);
        }
    }
}
