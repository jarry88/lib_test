package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PostListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.PostCategory;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.entity.SearchPostParams;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.GoodsFilterDrawerPopupView;
import com.ftofs.twant.widget.PostFilterDrawerPopupView;
import com.ftofs.twant.widget.SimpleTabButton;
import com.ftofs.twant.widget.SimpleTabManager;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 想要圈
 * @author zwm
 */
public class CircleFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    List<PostCategory> postCategoryList = new ArrayList<>();
    List<PostItem> postItemList = new ArrayList<>();

    LinearLayout llTabButtonContainer;

    PostListAdapter adapter;
    SearchPostParams searchPostParams = new SearchPostParams();

    boolean isPostDataLoaded;

    /**
     * 選中的過濾索引
     * -1表示未選中，從0開始
     */
    int filterSelectedIndex = -1;

    public static CircleFragment newInstance() {
        Bundle args = new Bundle();

        CircleFragment fragment = new CircleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        llTabButtonContainer = view.findViewById(R.id.ll_tab_button_container);

        Util.setOnClickListener(view, R.id.btn_add_post, this);
        Util.setOnClickListener(view, R.id.btn_post_filter, this);

        RecyclerView rvPostList = view.findViewById(R.id.rv_post_list);
        adapter = new PostListAdapter(R.layout.post_list_item, postItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostItem postItem = postItemList.get(position);
                Util.startFragment(PostDetailFragment.newInstance(postItem.postId));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvPostList.setLayoutManager(layoutManager);
        rvPostList.setAdapter(adapter);

        // 添加前面固定的Item
        PostCategory item = new PostCategory();
        item.categoryId = -1;
        item.categoryName = "全部";
        postCategoryList.add(item);

        loadPostData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        SLog.info("onEBMessage, messageType[%s]", message.messageType);
        if (message.messageType == EBMessageType.MESSAGE_TYPE_ADD_POST) {
            isPostDataLoaded = false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_add_post) {
            Util.startFragment(AddPostFragment.newInstance());
        } else if (id == R.id.btn_post_filter) {
            new XPopup.Builder(_mActivity)
                    .popupPosition(PopupPosition.Right)//右边
                    .hasStatusBarShadow(true) //启用状态栏阴影
                    .asCustom(new PostFilterDrawerPopupView(_mActivity, this, filterSelectedIndex))
                    .show();
        }
    }

    private void loadPostData() {
        EasyJSONObject params = EasyJSONObject.generate();

        try {
            if (searchPostParams.page > 0) {
                params.set("page", searchPostParams.page);
            }
            if (!StringUtil.isEmpty(searchPostParams.category)) {
                params.set("category", searchPostParams.category);
            }
            if (!StringUtil.isEmpty(searchPostParams.sort)) {
                params.set("sort", searchPostParams.sort);
            }

            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }
        } catch (Exception e) {

        }

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_SEARCH_POST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                try {
                    if (StringUtil.isEmpty(responseStr)) {
                        return;
                    }

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    // 如果未初始化，則初始化分類菜單
                    if (postCategoryList.size() <= 1) {
                        int twBlue = getResources().getColor(R.color.tw_blue, null);
                        EasyJSONArray wantPostCategoryList = responseObj.getArray("datas.wantPostCategoryList");

                        for (Object object : wantPostCategoryList) {
                            PostCategory postCategory = (PostCategory) EasyJSONBase.jsonDecode(PostCategory.class, object.toString());
                            postCategoryList.add(postCategory);
                        }

                        SimpleTabManager tabManager = new SimpleTabManager(0) {
                            @Override
                            public void onClick(View v) {
                                boolean isRepeat = onSelect(v);
                                int id = v.getId();
                                SLog.info("id[%d]", id);
                                if (isRepeat) {
                                    return;
                                }

                                if (id == -1) { // 點擊全部
                                    searchPostParams.category = null;
                                } else {
                                    searchPostParams.category = getCategoryName(id);
                                }

                                SLog.info("category[%s]", searchPostParams.category);

                                loadPostData();
                            }
                        };
                        for (PostCategory postCategory : postCategoryList) {
                            SimpleTabButton button = new SimpleTabButton(_mActivity);
                            button.setId(postCategory.categoryId);
                            button.setText(postCategory.categoryName);
                            button.setSelectedColor(twBlue);
                            ViewGroup.MarginLayoutParams layoutParams =
                                    new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.leftMargin = Util.dip2px(_mActivity, 10);
                            layoutParams.rightMargin = Util.dip2px(_mActivity, 10);
                            llTabButtonContainer.addView(button, layoutParams);

                            tabManager.add(button);
                        }
                    }

                    postItemList.clear();
                    EasyJSONArray wantPostList = responseObj.getArray("datas.wantPostList");
                    for (Object object : wantPostList) {
                        EasyJSONObject post = (EasyJSONObject) object;
                        PostItem item = new PostItem();

                        item.postId = post.getInt("postId");
                        item.coverImage = post.getString("coverImage");
                        item.postCategory = post.getString("postCategory");
                        item.title = post.getString("title");
                        item.postReply = post.getInt("postReply");
                        item.postFollow = post.getInt("postLike");
                        item.deadline = post.getString("expiresDate");

                        EasyJSONObject memberVo = post.getObject("memberVo");
                        // SLog.info("memberVo[%s]", memberVo);
                        if (memberVo != null) {
                            item.authorAvatar = memberVo.getString("avatar");
                            item.authorNickname = memberVo.getString("nickName");
                        }
                        item.postFollow = post.getInt("postLike");

                        postItemList.add(item);
                    }

                    isPostDataLoaded = true;
                    adapter.setNewData(postItemList);
                } catch (Exception e) {
                    e.printStackTrace();
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("type[%s], id[%d], extra[%s]", type, id, extra);
        if (type == PopupType.POST_FILTER) {
            filterSelectedIndex = id;
            searchPostParams.sort = (String) extra;
            loadPostData();
        }
    }


    private String getCategoryName(int id) {
        for (PostCategory postCategory : postCategoryList) {
            if (postCategory.categoryId == id) {
                return postCategory.categoryName;
            }
        }

        return null;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (!isPostDataLoaded) {
            llTabButtonContainer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadPostData();
                }
            }, 500);

        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
