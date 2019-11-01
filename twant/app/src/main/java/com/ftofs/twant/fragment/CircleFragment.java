package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PostListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SearchType;
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
public class CircleFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    List<PostCategory> postCategoryList = new ArrayList<>();
    List<PostItem> postItemList = new ArrayList<>();

    LinearLayout llTabButtonContainer;

    PostListAdapter adapter;
    SearchPostParams searchPostParams = new SearchPostParams();
    RecyclerView rvPostList;
    SwipeRefreshLayout swipeRefreshLayout;

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    /**
     * 是否獨立的Fragment，還是依附于MainFragment
     */
    boolean isStandalone;
    boolean isPostDataLoaded;

    /**
     * 選中的過濾索引
     * -1表示未選中，從0開始
     */
    int filterSelectedIndex = -1;

    public static CircleFragment newInstance(boolean isStandalone, SearchPostParams searchPostParams) {
        Bundle args = new Bundle();

        CircleFragment fragment = new CircleFragment();
        fragment.setArguments(args);

        fragment.setStandalone(isStandalone);
        fragment.setSearchPostParams(searchPostParams);

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

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        llTabButtonContainer = view.findViewById(R.id.ll_tab_button_container);

        if (isStandalone) { // 獨立的頁面
            view.findViewById(R.id.tool_bar).setVisibility(View.GONE);
            view.findViewById(R.id.tool_bar_standalone).setVisibility(View.VISIBLE);

            Util.setOnClickListener(view, R.id.btn_back, this);
        } else { // 附加在MainFragment上的頁面
            view.findViewById(R.id.tool_bar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tool_bar_standalone).setVisibility(View.GONE);

            Util.setOnClickListener(view, R.id.btn_search_post, this);
            Util.setOnClickListener(view, R.id.btn_add_post, this);
        }


        Util.setOnClickListener(view, R.id.btn_post_filter, this);

        rvPostList = view.findViewById(R.id.rv_post_list);
        adapter = new PostListAdapter(postItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostItem postItem = postItemList.get(position);
                if (postItem.getItemType() == Constant.ITEM_TYPE_NORMAL) {
                    Util.startFragment(PostDetailFragment.newInstance(postItem.postId));
                } else {
                    Util.startFragment(AddPostFragment.newInstance());
                }
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_thumb) {
                    // switchThumbState(position);  現在做成不可點擊
                }
            }
        });
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvPostList);

        rvPostList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

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
                    // Toast.makeText(_mActivity, "滑动到底了", Toast.LENGTH_SHORT).show();
                    SLog.info("滑动到底了^________________^");
                    if (!hasMore) {
                        int lastItemPos = postItemList.size() - 1;
                        PostItem lastItem = postItemList.get(lastItemPos);
                        if (lastItem.animShowStatus == PostItem.ANIM_NOT_SHOWN) {
                            lastItem.animShowStatus = PostItem.ANIM_SHOWING;
                            adapter.notifyItemChanged(lastItemPos);
                        }
                    }
                }
            }
        });

        // 設置空頁面
        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.no_result_empty_view, null, false);
        // 設置空頁面的提示語
        TextView tvEmptyHint = emptyView.findViewById(R.id.tv_empty_hint);
        tvEmptyHint.setText(R.string.no_post_hint);
        adapter.setEmptyView(emptyView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvPostList.setLayoutManager(layoutManager);
        rvPostList.setAdapter(adapter);

        // 添加前面固定的Item
        PostCategory item = new PostCategory();
        item.categoryId = -1;
        item.categoryName = "全部";
        postCategoryList.add(item);

        if (searchPostParams.page > 0) {
            loadPostData(searchPostParams.page);
        } else {
            loadPostData(currPage + 1);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        // SLog.info("onEBMessage, messageType[%s]", message.messageType);
        if (message.messageType == EBMessageType.MESSAGE_TYPE_ADD_POST) {
            SLog.info("收到添加貼文消息");
            isPostDataLoaded = false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_add_post) {
            Util.startFragment(AddPostFragment.newInstance());
        } else if (id == R.id.btn_post_filter) {
            new XPopup.Builder(_mActivity)
                    .popupPosition(PopupPosition.Right)//右边
                    .hasStatusBarShadow(true) //启用状态栏阴影
                    .asCustom(new PostFilterDrawerPopupView(_mActivity, this, filterSelectedIndex))
                    .show();
        } else if (id == R.id.btn_search_post) {
            Util.startFragment(SearchFragment.newInstance(SearchType.ARTICLE));
        }
    }

    private void loadPostData(int page) {
        // 組裝篩選參數
        EasyJSONObject params = EasyJSONObject.generate();

        try {
            params.set("page", page);

            if (!StringUtil.isEmpty(searchPostParams.category)) {
                params.set("category", searchPostParams.category);
            }
            if (!StringUtil.isEmpty(searchPostParams.sort)) {
                params.set("sort", searchPostParams.sort);
            }
            if (!StringUtil.isEmpty(searchPostParams.keyword)) {
                params.set("keyword", searchPostParams.keyword);
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
                adapter.loadMoreFail();
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
                        adapter.loadMoreFail();
                        return;
                    }

                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    SLog.info("hasMore[%s]", hasMore);
                    if (!hasMore) {
                        adapter.loadMoreEnd();
                        adapter.setEnableLoadMore(false);
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

                                currPage = 0;
                                loadPostData(currPage + 1);
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

                    // 如果是加載第一頁的數據，先清除舊數據
                    if (page == 1) {
                        postItemList.clear();
                    }
                    EasyJSONArray wantPostList = responseObj.getArray("datas.wantPostList");
                    for (Object object : wantPostList) {
                        EasyJSONObject post = (EasyJSONObject) object;
                        PostItem item = new PostItem();

                        item.itemType = Constant.ITEM_TYPE_NORMAL;
                        item.postId = post.getInt("postId");
                        item.coverImage = post.getString("coverImage");
                        item.postCategory = post.getString("postCategory");
                        item.title = post.getString("title");
                        item.createTime = post.getString("createTime");
                        item.postReply = post.getInt("postReply");
                        item.postFollow = post.getInt("postLike");
                        item.postView = post.getInt("postView");
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

                    if (!hasMore && postItemList.size() > 1) {
                        // 如果全部加載完畢，添加加載完畢的提示
                        PostItem item = new PostItem();
                        item.itemType = Constant.ITEM_TYPE_LOAD_END_HINT;
                        postItemList.add(item);
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    adapter.loadMoreComplete();
                    isPostDataLoaded = true;
                    adapter.setNewData(postItemList);
                    currPage++;
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
            currPage = 0;
            loadPostData(currPage + 1);
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
                    currPage = 0;
                    loadPostData(currPage + 1);
                }
            }, 1500);
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
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                postItem.isLike = newState;
                if (newState == 1) {
                    postItem.postFollow++;
                } else {
                    postItem.postFollow--;
                }

                adapter.notifyItemChanged(position);
            }
        });
    }


    public void setSearchPostParams(SearchPostParams searchPostParams) {
        this.searchPostParams = searchPostParams;

        // 保證不為null
        if (this.searchPostParams == null) {
            this.searchPostParams = new SearchPostParams();
        }
    }

    public void setStandalone(boolean standalone) {
        isStandalone = standalone;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (isStandalone) {
            pop();
            return true;
        }
        return false;
    }

    /**
     * 上滑加載更多
     */
    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadPostData(currPage + 1);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        SLog.info("onRefresh");
        currPage = 0;
        isPostDataLoaded = false;
        loadPostData(currPage + 1);
    }
}
