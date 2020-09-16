package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.RvMemberPostListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.PostItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 我的想要帖Fragment
 * @author zwm
 */
public class MyArticleFragment extends BaseFragment implements View.OnClickListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    List<PostItem> postItemList = new ArrayList<>();
    RvMemberPostListAdapter adapter;
    String memberName;

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;


    public static MyArticleFragment newInstance() {
        Bundle args = new Bundle();
        MyArticleFragment fragment = new MyArticleFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static MyArticleFragment newInstance(String memberName) {
        Bundle args = new Bundle();
        args.putString("memberName",memberName);
        MyArticleFragment fragment = new MyArticleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_article, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv_Artical = view.findViewById(R.id.tv_my_article);
        if (getArguments().containsKey("memberName")) {
            tv_Artical.setText(getString(R.string.text_him_article));
            memberName = getArguments().getString("memberName");
        } else {
            memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
        }
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_menu, this);

        RecyclerView rvPostList = view.findViewById(R.id.rv_post_list);
        adapter = new RvMemberPostListAdapter(postItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostItem postItem = postItemList.get(position);
                Util.startFragment(PostDetailFragment.newInstance(postItem.postId));
            }
        });
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvPostList);
        rvPostList.setLayoutManager(new LinearLayoutManager(_mActivity));
        rvPostList.setAdapter(adapter);

        loadData(currPage + 1);
    }

    private void loadData(int page) {
        String token = User.getToken();

        if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
            return;
        }

        String url = Api.PATH_POST_LIST;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "page", page,
                "memberName", memberName);

        SLog.info("params[%s]", params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
                adapter.loadMoreFail();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        adapter.loadMoreFail();
                        return;
                    }

                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    SLog.info("hasMore[%s]", hasMore);
                    if (!hasMore) {
                        adapter.loadMoreEnd();
                        adapter.setEnableLoadMore(false);
                    }

                    EasyJSONArray wantPostList = responseObj.getSafeArray("datas.wantPostList");
                    for (Object object : wantPostList) {
                        EasyJSONObject post = (EasyJSONObject) object;
                        PostItem item = new PostItem();

                        item.postId = post.getInt("postId");
                        item.coverImage = post.getSafeString("coverImage");
                        item.postCategory = post.getSafeString("postCategory");
                        item.title = post.getSafeString("title");
                        item.content = post.getSafeString("content");
                        item.comeTrueState =post.getInt("comeTrueState");
                        EasyJSONObject memberVo = post.getSafeObject("memberVo");
                        if (!Util.isJsonObjectEmpty(memberVo)) {
                            item.authorAvatar = memberVo.getSafeString("avatar");
                            item.authorNickname = memberVo.getSafeString("nickName");
                        }
                        item.postThumb = post.getInt("postLike");
                        item.postReply = post.getInt("postReply");
                        item.postFollow = post.getInt("postFavor");
                        item.itemType = Constant.ITEM_TYPE_NORMAL;

                        postItemList.add(item);
                    }

                    if (!hasMore) {
                        // 如果全部加載完畢，添加加載完畢的提示
                        PostItem item = new PostItem();
                        item.itemType = Constant.ITEM_TYPE_LOAD_END_HINT;
                        postItemList.add(item);
                    }

                    adapter.loadMoreComplete();
                    adapter.setNewData(postItemList);
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_menu) {
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 11))
                    .offsetY(-Util.dip2px(_mActivity, 8))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(_mActivity, this, BlackDropdownMenu.TYPE_HOME_AND_MY))
                    .show();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadData(currPage + 1);
    }
}
