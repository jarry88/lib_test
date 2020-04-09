package com.ftofs.twant.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ShopRelativePostAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.SloganView;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.impl.LoadingPopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class ShopRelativePostFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private int storeId;
    private Unbinder unbinder;
    private SimpleTabManager simpleTabManager;
    private boolean hasMore;
    private int totalPage;
    // 當前第幾頁
    int currPage = 0;
    private boolean showShopPost =true;
    private List<PostItem> shopPostList = new ArrayList<>();
    private ShopRelativePostAdapter adapter;
    private boolean isPostDataLoaded;

    @OnClick(R.id.btn_back)
    public void back() {
        hideSoftInputPop();
    }

    @OnClick({R.id.stb_good_post})
    public void showGoodPost() {
        hideSoftInputPop();
    }
    @OnClick({R.id.stb_shop_post})
    public void showShopPost() {
        hideSoftInputPop();
    }
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_shop_relative_post)
    RecyclerView rvRelativeList;

    public static ShopRelativePostFragment newInstance(int storeId) {

        ShopRelativePostFragment fragment = new ShopRelativePostFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.storeId = storeId;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_relative_post_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ShopRelativePostAdapter(shopPostList);
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvRelativeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        rvRelativeList.setLayoutManager(linearLayoutManager);
        rvRelativeList.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Util.startFragment(PostDetailFragment.newInstance(shopPostList.get(position).postId));
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        simpleTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }
                if (id == R.id.stb_shop_post) {
                    showShopPost = true;
                } else {
                    showShopPost = false;
                }
                currPage = 0;
                loadShopPost(currPage+1);
            }
        };

        simpleTabManager.add(view.findViewById(R.id.stb_shop_post));
        simpleTabManager.add(view.findViewById(R.id.stb_good_post));
        loadShopPost(currPage+1);

    }

    private void loadShopPost(int page) {
        String token = User.getToken();
        EasyJSONObject params = EasyJSONObject.generate("token",token,"storeId",storeId,"page",page);
        SLog.info("params %s",params.toString());
        final BasePopupView loadingPopup =Util.createLoadingPopup(_mActivity).show();
        Api.getUI(showShopPost?Api.PATH_STORE_POST:Api.PATH_STORE_GOODS_POST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingPopup.dismiss();
                adapter.loadMoreFail();
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr %s",responseStr);
                loadingPopup.dismiss();

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {

                    return;
                }
                try {
                    EasyJSONArray storePostList = responseObj.getArray(showShopPost?"datas.storePostList":"datas.goodsPostList");
                    if (currPage == 0) {
                        shopPostList.clear();
                    }
                    if (responseObj.exists("datas.showPage")) {
                        hasMore = responseObj.getBoolean("datas.showPage.hasMore");
                        if (!hasMore) {
                            adapter.loadMoreEnd();
                            adapter.setEnableLoadMore(false);
                        }
                    }

                    if (!Util.isJsonArrayEmpty(storePostList)) {
                        for (Object object:storePostList) {
                            EasyJSONObject postItem = (EasyJSONObject) object;
                            PostItem post = new PostItem();
                            post.itemType = Constant.ITEM_TYPE_NORMAL;
                            post.title = postItem.getSafeString("title");
                            post.postId = postItem.getInt("postId");
                            post.content = postItem.getSafeString("content");
                            post.isDelete = postItem.getInt("isDelete");
                            post.postView = postItem.getInt("postView");
                            post.postLike = postItem.getInt("postLike");
                            post.postFollow = postItem.getInt("postFavor");
                            post.postThumb = postItem.getInt("postReply");
                            if (!showShopPost) {
                                post.goodsimage = postItem.getSafeString("coverImage");
                            }
                            post.createTime = postItem.getSafeString("createTime");
                            if (postItem.exists("memberVo")) {
                                post.authorAvatar = postItem.getSafeString("memberVo.avatar");
                                post.authorNickname = postItem.getSafeString("memberVo.nickName");
                            }
                            shopPostList.add(post);
                        }
                    }

//                    totalPage = responseObj.getInt("datas.showPage.totalPage");

                    if (!hasMore && shopPostList.size() > 1) {
                        // 如果全部加載完畢，添加加載完畢的提示
                        PostItem item = new PostItem();
                        item.itemType = Constant.ITEM_TYPE_LOAD_END_HINT;
                        shopPostList.add(item);
                        loadingPopup.dismiss();
                    }

                    swipeRefreshLayout.setRefreshing(false);
                    adapter.loadMoreComplete();
                    isPostDataLoaded = true;
                    SLog.info("shopPostListSize [%d]",shopPostList.size());
                    adapter.setNewData(shopPostList);
                    currPage++;
                }catch (Exception e) {
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
        } else if (id == R.id.stb_good_post) {

        } else if (id == R.id.stb_shop_post) {

        }
    }
    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }

        loadShopPost(currPage+1);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        SLog.info("onRefresh");
        currPage = 0;
        isPostDataLoaded = false;
        loadShopPost(currPage + 1);
    }
}
