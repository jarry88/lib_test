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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.MyFollowArticleAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.PostItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.InviteAddFriendPopup;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 作者介紹頁
 * @author  gzp
 * @date 2019/12/17
 */
public class AuthorInfoFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    String authorMemberName;
    boolean hasMore=true;
    RecyclerView rvArticleList;
    private List<PostItem> articleItemList;
    private MyFollowArticleAdapter adapter;
    private boolean isArticleDataLoaded;
    private int currPage;
    private int isFollow;
    private ImageView imageAvatar;
    TextView tvNickname;
    TextView tvMemberSignature;
    TextView tvPopularity;
    TextView tvLocation;
    TextView tvFollowCount;
    TextView tvArticleCount;
    TextView btnFollow;
    TextView btnAddFriend;
    private int isFriend;
    private String avatarUrl;
    private String nickname;

    public static AuthorInfoFragment newInstance(String authorMemberName) {
        AuthorInfoFragment fragment = new AuthorInfoFragment();
        fragment.authorMemberName = authorMemberName;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_author_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.setOnClickListener(view,R.id.btn_back,this);
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
        articleItemList = new ArrayList<>();
        adapter = new MyFollowArticleAdapter(R.layout.my_follow_article_item, articleItemList);
        adapter.setOnItemClickListener((adapter, view1, position) -> {
            int postId = articleItemList.get(position).postId;
            Util.startFragment(PostDetailFragment.newInstance(postId));
        });
        rvArticleList = view.findViewById(R.id.rv_article_list);
        rvArticleList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if(lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
                    SLog.info("滑动到底了^________________^");
                    if (!hasMore && articleItemList.size() > 0) {
                        int lastItemPos = articleItemList.size() - 1;
                        PostItem lastItem = articleItemList.get(lastItemPos);
                        if (lastItem.animShowStatus == Constant.ANIM_NOT_SHOWN) {
                            lastItem.animShowStatus = Constant.ANIM_SHOWING;
                            adapter.notifyItemChanged(lastItemPos);
                        }
                    }
                }
            }
        });


        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvArticleList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvArticleList.setLayoutManager(layoutManager);
        rvArticleList.setAdapter(adapter);

        loadMemberInfo();

    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("進入到加載數據");
        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadData(currPage +1);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_follow:
                String token = User.getToken();
                if (StringUtil.isEmpty(token)) {
                    return;
                }

                // 加關注或取消關注
                EasyJSONObject params = EasyJSONObject.generate(
                        "memberName", authorMemberName,
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
            case  R.id.btn_add_friend:
                if (isFriend == 0) {
                    new XPopup.Builder(_mActivity)
                            .autoOpenSoftInput(true)
                            .asCustom(new InviteAddFriendPopup(_mActivity, authorMemberName))
                            .show();
                } else { // 訪問專頁
                    start(TestFriendFragment.newInstance(authorMemberName));
                }
            break;
            default:
                break;
        }
    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (!isArticleDataLoaded) {
            loadData(currPage);
        }
    }
    private void loadMemberInfo() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "memberName", authorMemberName
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

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
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
                    if (isArticleDataLoaded) {
                        for (PostItem postItem : articleItemList) {
                            postItem.authorNickname=nickname;
                        }
                        adapter.notifyDataSetChanged();
                    }
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
    private void loadData(int page) {
        if (StringUtil.isEmpty(authorMemberName)) {
            return;
        }
        int size=20;
        EasyJSONObject params = EasyJSONObject.generate(
                "page", page,
                "memberName", authorMemberName);

        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_WANT_POST_MEMBER_INFO_LIST, params, new UICallback() {
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

                try {
                    EasyJSONArray wantPostVos = responseObj.getSafeArray("datas.wantPostVos");
                    SLog.info("responseArray%s",wantPostVos.toString());
                    if (wantPostVos.length() < size) {
                        hasMore = false;
                    }
                    for (Object object : wantPostVos) {
                        EasyJSONObject post = (EasyJSONObject) object;
                        PostItem item = new PostItem();

                        item.itemType=PostItem.POST_TYPE_AUTHOR;
                        item.postId = post.getInt("postId");
                        item.coverImage = post.getSafeString("coverImage");
                        item.postCategory = post.getSafeString("postCategory");
                        item.title = post.getSafeString("title");
                        item.createTime = post.getSafeString("createTime");


                        item.authorAvatar = avatarUrl;
                        item.authorNickname = nickname;
                        item.postLike = post.getInt("postLike");
                        item.postView = post.getInt("postView");
                        item.postFollow = post.getInt("postFavor");
                        item.isLike = post.getInt("isLike");
                        item.isFav = post.getInt("isFavor");
                        item.comeTrueState = post.getInt("comeTrueState");
                        item.isDelete = post.getInt("isDelete");

                        articleItemList.add(item);
                    }
                    isArticleDataLoaded = true;
                    adapter.loadMoreComplete();
                    adapter.setNewData(articleItemList);
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }
}
