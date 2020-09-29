package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.adapter.PostListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.PostCategory;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.entity.SearchHistoryItem;
import com.ftofs.twant.entity.SearchPostParams;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.SearchHistoryUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.PostFilterDrawerPopupView;
import com.ftofs.twant.widget.SimpleTabButton;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.TouchEditText;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupPosition;
import com.nex3z.flowlayout.FlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

import static com.ftofs.twant.widget.SearchHistoryPopup.SEARCH_HISTORY_ITEM;

/**
 * 想要圈
 * @author zwm
 */
public class CircleFragment extends MainBaseFragment implements View.OnClickListener, OnSelectedListener,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, SimpleCallback {
    List<PostCategory> postCategoryList = new ArrayList<>();
    List<PostItem> postItemList = new ArrayList<>();

    LinearLayout llTabButtonContainer;
    BasePopupView loadingPopup;
    BasePopupView postFilterDrawerPopupView;


    PostListAdapter adapter;
    SearchPostParams searchPostParams = new SearchPostParams();
    RecyclerView rvPostList;
    SwipeRefreshLayout swipeRefreshLayout;

    TouchEditText etKeyword;
    View llSearchHistoryContainer;
    View flMaskEmptyArea;
    View historyPopupLayout;
    FlowLayout flHistoryContainer;

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    boolean floatButtonShown = true;  // 浮動按鈕是否有顯示
    LinearLayout llFloatButtonContainer;
    ImageView btnPublishWantPost;
    ImageView btnGotoTop;

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
    //來自於bus信息需要主動更新的標記
    private boolean fromBus=false;

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
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        Util.setOnClickListener(view, R.id.tv_title, this);


        flHistoryContainer = view.findViewById(R.id.fl_search_history_container);

        llSearchHistoryContainer = view.findViewById(R.id.ll_search_history_container);
        flMaskEmptyArea = view.findViewById(R.id.fl_mask_empty_area);
        flMaskEmptyArea.setOnClickListener(this);
        historyPopupLayout = view.findViewById(R.id.history_popup_layout);
        historyPopupLayout.setOnClickListener(this);

        etKeyword = view.findViewById(R.id.et_keyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchPostParams.keyword = textView.getText().toString().trim();
                    if (!StringUtil.isEmpty(searchPostParams.keyword)) {
                        SearchHistoryUtil.saveSearchHistory(SearchType.POST.ordinal(), searchPostParams.keyword);
                    }
                    hideSearchHistoryContainer();

                    currPage = 0;
                    loadPostData(currPage + 1);

                    return true;
                }
                return false;
            }
        });
        etKeyword.setSimpleCallback(this);

        llFloatButtonContainer = view.findViewById(R.id.ll_float_button_container);

        if (isStandalone) { // 獨立的頁面
            view.findViewById(R.id.tool_bar).setVisibility(View.GONE);
            view.findViewById(R.id.tool_bar_standalone).setVisibility(View.VISIBLE);

            Util.setOnClickListener(view, R.id.btn_clear_all, this);
            Util.setOnClickListener(view, R.id.btn_back, this);
        } else { // 附加在MainFragment上的頁面
            view.findViewById(R.id.tool_bar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tool_bar_standalone).setVisibility(View.GONE);

            Util.setOnClickListener(view, R.id.btn_search_post, this);
            Util.setOnClickListener(view, R.id.btn_add_post, this);
        }

        btnPublishWantPost = view.findViewById(R.id.btn_publish_want_post);
//        Glide.with(_mActivity).load("file:///android_asset/christmas/publish_want_post_dynamic.gif").centerCrop().into(btnPublishWantPost);
        btnPublishWantPost.setOnClickListener(this);
        btnGotoTop = view.findViewById(R.id.btn_goto_top);
        btnGotoTop.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_post_filter, this);

        rvPostList = view.findViewById(R.id.rv_post_list);
        adapter = new PostListAdapter(_mActivity, postItemList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PostItem postItem = postItemList.get(position);
                if (postItem.getItemType() == Constant.ITEM_TYPE_NORMAL) {
                    Util.startFragment(PostDetailFragment.newInstance(postItem.postId));
                } else {
//                    Util.startFragment(AddPostFragment.newInstance(false));
                    ApiUtil.addPost(_mActivity,false);
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
                SLog.info("__newState[%d]", newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideFloatButton();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showFloatButton();
                }
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
                    if (!hasMore && postItemList.size() > 0) {
                        int lastItemPos = postItemList.size() - 1;
                        PostItem lastItem = postItemList.get(lastItemPos);
                        if (lastItem.animShowStatus == Constant.ANIM_NOT_SHOWN) {
                            lastItem.animShowStatus = Constant.ANIM_SHOWING;
                            adapter.notifyItemChanged(lastItemPos);
                        }
                    }
                }
            }
        });

        // 設置空頁面
        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.layout_placeholder_no_data, null, false);
        // 設置空頁面的提示語
        TextView tvEmptyHint = emptyView.findViewById(R.id.tv_empty_hint);
        tvEmptyHint.setText(R.string.no_post_hint);
        adapter.setEmptyView(emptyView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvPostList.setLayoutManager(layoutManager);
        rvPostList.setAdapter(adapter);
        // 添加前面固定的Item
        PostCategory followItem = new PostCategory();
        followItem.categoryId = -2;
        followItem.categoryName = "關注";
        postCategoryList.add(followItem);

        // 添加前面固定的Item
        PostCategory item = new PostCategory();
        item.categoryId = -1;
        item.categoryName = "全部";
        postCategoryList.add(item);

        if (!StringUtil.isEmpty(searchPostParams.keyword)) {
            etKeyword.setText(searchPostParams.keyword);
        }

        if (searchPostParams.page > 0) {
            loadPostData(searchPostParams.page);
        } else {
            loadPostData(currPage + 1);
        }

        initSearchHistory();
        view.findViewById(R.id.btn_clear_all_history).setOnClickListener(this);
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
            SLog.info("收到添加想要帖消息");
            fromBus = true;
            isPostDataLoaded = false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 處理歷史搜索的點擊事件
        if (v instanceof TextView) {
            Object tag = v.getTag();
            if (tag != null && tag instanceof String && SEARCH_HISTORY_ITEM.equals(tag)) {
                TextView btnSearchHistoryItem = (TextView) v;
                String keyword = btnSearchHistoryItem.getText().toString();
                etKeyword.setText(keyword);

                hideSoftInput();
                hideSearchHistoryContainer();

                currPage = 0;
                searchPostParams.keyword = keyword;
                loadPostData(currPage + 1);

                return;
            }
        }

        if (id == R.id.btn_clear_all_history) {
            flHistoryContainer.removeAllViews();
            SearchHistoryUtil.clearSearchHistory(SearchType.POST.ordinal());
        } else if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_add_post) {
            ApiUtil.addPost(_mActivity,false);
        } else if (id == R.id.btn_post_filter) {
            if (postFilterDrawerPopupView == null) {
                postFilterDrawerPopupView = new XPopup.Builder(_mActivity)
                        .popupPosition(PopupPosition.Right)//右边
                        .hasStatusBarShadow(true) //启用状态栏阴影
                        .asCustom(new PostFilterDrawerPopupView(_mActivity, this, filterSelectedIndex));
            }
            postFilterDrawerPopupView.show();
        } else if (id == R.id.btn_search_post) {
            Util.startFragment(CategoryFragment.newInstance(SearchType.POST, "想要"));
        } else if (id == R.id.btn_publish_want_post) {
            ApiUtil.addPost(_mActivity,false);
        } else if (id == R.id.btn_goto_top) {
            rvPostList.scrollToPosition(0);
        } else if (id == R.id.btn_clear_all) {
            etKeyword.setText("");
        } else if (id == R.id.fl_mask_empty_area) { // 點擊彈窗蒙板的空白區域，則隱藏歷史彈窗
            hideSearchHistoryContainer();
        } else if (id == R.id.history_popup_layout) {
            // 屏蔽點擊事件，不要刪除
        } else if (id == R.id.tv_title) {
            ((MainActivity) _mActivity).showDebugIcon();
        }
    }

    private void initSearchHistory() {
        List<SearchHistoryItem> historyItemList = SearchHistoryUtil.loadSearchHistory(SearchType.POST.ordinal());
        flHistoryContainer.removeAllViews();
        for (SearchHistoryItem item : historyItemList) {
            TextView textView = (TextView) LayoutInflater.from(_mActivity)
                    .inflate(R.layout.search_history_popup_item, flHistoryContainer, false);
            textView.setText(item.keyword);

            textView.setTag(SEARCH_HISTORY_ITEM);
            textView.setOnClickListener(this);

            flHistoryContainer.addView(textView);
        }
    }

    private void loadPostData(int page) {
        // SLog.bt();
        // 組裝篩選參數
        EasyJSONObject params = EasyJSONObject.generate();

        try {
            params.set("page", page);
            params.set("follow", searchPostParams.follow);


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
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        SLog.info("url[%s],params[%s]",Api.PATH_SEARCH_POST, params);
        if (page == 1) {
            loadingPopup = Util.createLoadingPopup(_mActivity).show();
        }
        Api.postUI(Api.PATH_SEARCH_POST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
                searchPostParams.follow=0;
                if (loadingPopup != null) {
                    loadingPopup.dismiss();
                }
                adapter.loadMoreFail();

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                if (loadingPopup != null) {
                    loadingPopup.dismiss();
                }
                try {
                    if (StringUtil.isEmpty(responseStr)) {
                        return;
                    }

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
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
                    if (postCategoryList.size() <= 2) {
                        int twBlue = getResources().getColor(R.color.tw_blue, null);
                        EasyJSONArray wantPostCategoryList = responseObj.getSafeArray("datas.wantPostCategoryList");

                        for (Object object : wantPostCategoryList) {
                            PostCategory postCategory = (PostCategory) EasyJSONBase.jsonDecode(PostCategory.class, object.toString());
                            postCategoryList.add(postCategory);
                        }

                        SimpleTabManager tabManager = new SimpleTabManager(1) {
                            @Override
                            public void onClick(View v) {
                                int id = v.getId();
                                if (id==-2&&!User.isLogin()) {
                                    Util.startFragment(LoginFragment.newInstance());
                                    return;
                                }
                                boolean isRepeat = onSelect(v);
                                SLog.info("id[%d]", id);
                                if (isRepeat) {
                                    return;
                                }
                                if (id == -2) { // 點擊關注
                                    searchPostParams.follow = 1;

                                }else if (id == -1) { // 點擊全部
                                    searchPostParams.category = null;
                                    searchPostParams.follow = 0;

                                } else {
                                    searchPostParams.follow = 0;
                                    searchPostParams.category = getCategoryName(id);
                                }

                                SLog.info("category[%s]", searchPostParams.category);

                                currPage = 0;
                                loadPostData(currPage + 1);
                            }
                        };
                        int count = postCategoryList.size();
                        for (PostCategory postCategory : postCategoryList) {
                            count--;
                            SimpleTabButton button = new SimpleTabButton(_mActivity);
                            button.setId(postCategory.categoryId);
                            button.setText(postCategory.categoryName);
                            button.setSelectedColor(twBlue);
                            LinearLayout.LayoutParams layoutParams =
                                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            layoutParams.leftMargin = Util.dip2px(_mActivity, 8);
                            layoutParams.rightMargin = Util.dip2px(_mActivity, 8);
                            /*
                            if (count == 0) {
                                layoutParams.weight = 1.3f;
                            } else {
                                layoutParams.weight = 1;
                            }

                             */
                            llTabButtonContainer.addView(button, layoutParams);

                            tabManager.add(button);
                        }
                    }

                    // 如果是加載第一頁的數據，先清除舊數據
                    if (page == 1) {
                        postItemList.clear();
                    }
                    EasyJSONArray wantPostList = responseObj.getSafeArray("datas.wantPostList");
                    for (Object object : wantPostList) {
                        EasyJSONObject post = (EasyJSONObject) object;
                        PostItem item = new PostItem();

                        item.itemType = Constant.ITEM_TYPE_NORMAL;
                        item.postId = post.getInt("postId");

                        // 從想要帖內容中獲取第一張圖片為封面圖片
                        EasyJSONArray wantPostImages = post.getSafeArray("wantPostImages");
                        if (wantPostImages.length() > 0) {
                            EasyJSONObject postImageObj = wantPostImages.getObject(0);

                            item.coverImage = postImageObj.getSafeString("imageUrl");
                        } else {
                            wantPostImages=post.getSafeArray("wantPostGoodsVoList");
                            if (wantPostImages.length() > 0) {
                                EasyJSONObject postImageObj = wantPostImages.getObject(0);

                                item.coverImage = postImageObj.getSafeString("goodsImage");
                            }
                        }
                        if (item.coverImage == null) {
                            item.coverImage=post.getSafeString("coverImage");
                        }
                        item.postCategory = post.getSafeString("postCategory");
                        item.title = post.getSafeString("title");
                        item.content = post.getSafeString("content");
                        item.createTime = post.getSafeString("createTime");
                        item.postReply = post.getInt("postReply");
                        item.postFollow = post.getInt("postLike");
                        item.postView = post.getInt("postView");
                        item.deadline = post.getSafeString("expiresDate");
                        item.comeTrueState = post.getInt("comeTrueState");

                        EasyJSONObject memberVo = post.getSafeObject("memberVo");
                        // SLog.info("memberVo[%s]", memberVo);
                        if (!Util.isJsonObjectEmpty(memberVo)) {
                            item.authorAvatar = memberVo.getSafeString("avatar");
                            item.authorNickname = memberVo.getSafeString("nickName");
                        }
                        item.postFollow = post.getInt("postLike");

                        postItemList.add(item);
                    }

                    if (!hasMore && postItemList.size() > 1) {
                        // 如果全部加載完畢，添加加載完畢的提示
                        PostItem item = new PostItem();
                        item.itemType = Constant.ITEM_TYPE_LOAD_END_HINT;
                        postItemList.add(item);
                        loadingPopup.dismiss();
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    adapter.loadMoreComplete();
                    isPostDataLoaded = true;
                    adapter.setNewData(postItemList);
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
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

        updateMainSelectedFragment(MainFragment.CIRCLE_FRAGMENT);

        if (!isPostDataLoaded) {
            llTabButtonContainer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currPage = 0;
                    loadPostData(currPage + 1);
                }
            }, fromBus?200:1500);
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
            Util.showLoginFragment(requireContext());
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

        if (!StringUtil.isEmpty(this.searchPostParams.keyword)) {
            SearchHistoryUtil.saveSearchHistory(SearchType.POST.ordinal(), searchPostParams.keyword);
        }
    }

    public void setStandalone(boolean standalone) {
        isStandalone = standalone;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (isStandalone) {
            hideSoftInputPop();
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

    private void showFloatButton() {
        if (floatButtonShown ){
            return;
        }

        llFloatButtonContainer.setTranslationX(0);
        floatButtonShown = true;
    }

    private void hideFloatButton() {
        if (!floatButtonShown) {
            return;
        }

        llFloatButtonContainer.setTranslationX(Util.dip2px(_mActivity, 31));
        floatButtonShown = false;
    }

    @Override
    public void onSimpleCall(Object data) {
        if (data instanceof Integer) {
            int id = (int) data;
            if (id == R.id.et_keyword) {
                showSearchHistoryContainer();
            }
        }
    }

    private void showSearchHistoryContainer() {
        initSearchHistory();
        llSearchHistoryContainer.setVisibility(View.VISIBLE);
    }

    private void hideSearchHistoryContainer() {
        llSearchHistoryContainer.setVisibility(View.GONE);
    }
}
