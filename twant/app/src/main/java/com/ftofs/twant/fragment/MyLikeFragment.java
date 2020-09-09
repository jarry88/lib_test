package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsSearchResultAdapter;
import com.ftofs.twant.adapter.MyLikeArticleAdapter;
import com.ftofs.twant.adapter.MyLikeGoodsAdapter;
import com.ftofs.twant.adapter.MyLikeStoreAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.entity.MyFollowGoodsItem;
import com.ftofs.twant.entity.MyLikeStoreItem;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 我的讚想Fragment
 * @author zwm
 */
public class MyLikeFragment extends BaseFragment implements View.OnClickListener {
    List<MyLikeStoreItem> myLikeStoreItemList = new ArrayList<>();
    List<PostItem> myLikeArticleItemList = new ArrayList<>();

    public static final int TAB_INDEX_STORE = 0;
    public static final int TAB_INDEX_GOODS = 1;
    public static final int TAB_INDEX_ARTICLE = 2;
    int currTabIndex = TAB_INDEX_STORE;

    boolean storeDataLoaded = false;
    MyLikeStoreAdapter myLikeStoreAdapter;
    boolean goodsDataLoaded = false;
    MyLikeGoodsAdapter myLikeGoodsAdapter;
    boolean articleDataLoaded = false;
    MyLikeArticleAdapter myLikeArticleAdapter;

    RecyclerView rvMyLikeList;
    String memberName;
    private List<GoodsSearchItemPair> goodsItemPairList = new ArrayList<>();
    private GoodsSearchResultAdapter mGoodsAdapter;

    public static MyLikeFragment newInstance() {
        Bundle args = new Bundle();

        MyLikeFragment fragment = new MyLikeFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static MyLikeFragment newInstance(String memberName) {
        Bundle args = new Bundle();
        args.putString("memberName",memberName);
        MyLikeFragment fragment = new MyLikeFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_like, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        replaceWord(view);
        SimpleTabManager simpleTabManager = new SimpleTabManager(TAB_INDEX_STORE) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                if (isRepeat) {
                    return;
                }

                int id = v.getId();
                if (id == R.id.btn_store) {
                    currTabIndex = TAB_INDEX_STORE;
                    if (storeDataLoaded) {
                        rvMyLikeList.setAdapter(myLikeStoreAdapter);
                    } else {
                        loadMyLikeStore();
                    }
                    rvMyLikeList.setBackgroundColor(_mActivity.getColor(android.R.color.white));
                } else if (id == R.id.btn_goods) {
                    currTabIndex = TAB_INDEX_GOODS;
                    if (goodsDataLoaded) {
                        rvMyLikeList.setAdapter(mGoodsAdapter);
                    } else {
                        loadMyLikeGoods();
                    }
                    rvMyLikeList.setBackgroundColor(_mActivity.getColor(android.R.color.white));
                } else if (id == R.id.btn_article) {
                    currTabIndex = TAB_INDEX_ARTICLE;
                    if (articleDataLoaded) {
                        SLog.info("HERE?>");
                        rvMyLikeList.setAdapter(myLikeArticleAdapter);
                    } else {
                        loadMyLikeArticle();
                    }
                    rvMyLikeList.setBackgroundColor(_mActivity.getColor(R.color.tw_slight_grey));
                }
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_store));
        simpleTabManager.add(view.findViewById(R.id.btn_goods));
        simpleTabManager.add(view.findViewById(R.id.btn_article));

        rvMyLikeList = view.findViewById(R.id.rv_my_like_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvMyLikeList.setLayoutManager(layoutManager);
        mGoodsAdapter = new GoodsSearchResultAdapter(_mActivity, goodsItemPairList);
        mGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_goto_store_left || id == R.id.btn_goto_store_right) {
                    GoodsSearchItemPair pair = goodsItemPairList.get(position);
                    int storeId;
                    if (id == R.id.btn_goto_store_left) {
                        storeId = pair.left.storeId;
                    } else {
                        storeId = pair.right.storeId;
                    }

                    Util.startFragment(ShopMainFragment.newInstance(storeId));
                } else if (id == R.id.cl_container_left || id == R.id.cl_container_right) {
                    GoodsSearchItemPair pair = goodsItemPairList.get(position);
                    int commonId;
                    if (id == R.id.cl_container_left) {
                        commonId = pair.left.commonId;
                    } else {
                        commonId = pair.right.commonId;
                    }

                    Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                } else if (id == R.id.btn_play_game) {
//                    String url = Util.makeChristmasH5Url();
                    String url = Util.makeSpringH5Url();
                    SLog.info("sprint_url[%s]", url);
                    if (url == null) {
                        Util.showLoginFragment();
                        return;
                    }
                    start(H5GameFragment.newInstance(url, true));
                } else if (id == R.id.btn_back) {
                    pop();
                }
            }
        });
        mGoodsAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                GoodsSearchItemPair pair = goodsItemPairList.get(position);
                int itemType = pair.getItemType();
                if (itemType == Constant.ITEM_TYPE_NORMAL) {
                    return 1;
                } else if (itemType == Constant.ITEM_TYPE_LOAD_END_HINT || itemType == Constant.ITEM_TYPE_BANNER) {
                    return 2;
                }
                return 1;
            }
        });
        myLikeStoreAdapter = new MyLikeStoreAdapter(R.layout.my_like_store_item, myLikeStoreItemList);
        myLikeStoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MyLikeStoreItem myLikeStoreItem = myLikeStoreItemList.get(position);
                start(ShopMainFragment.newInstance(myLikeStoreItem.storeId));
            }
        });
        myLikeArticleAdapter = new MyLikeArticleAdapter(R.layout.my_like_article_item, myLikeArticleItemList);
        myLikeArticleAdapter.setOnItemClickListener((adapter, view1, position) -> {
            PostItem postItem = myLikeArticleItemList.get(position);
            start(PostDetailFragment.newInstance(postItem.postId));
        });

        rvMyLikeList.setAdapter(myLikeStoreAdapter);

        loadMyLikeStore();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void loadMyLikeStore() {
        try {
            String token = User.getToken();
            //String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
                return;
            }

            String url = Api.PATH_MY_LIKE_STORE;
            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName, "token", token);

            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                            return;
                        }

                        EasyJSONArray storeList = responseObj.getSafeArray("datas.storeList");
                        for (Object object : storeList) {
                            EasyJSONObject store = (EasyJSONObject) object;

                            MyLikeStoreItem myLikeStoreItem = new MyLikeStoreItem();
                            myLikeStoreItem.storeId = store.getInt("storeVo.storeId");
                            myLikeStoreItem.storeAvatar = store.getSafeString("storeVo.storeAvatarUrl");
                            myLikeStoreItem.storeName = store.getSafeString("storeVo.storeName");
                            myLikeStoreItem.storeAddress = store.getSafeString("storeVo.chainAreaInfo");
                            myLikeStoreItem.storeDistanceStr = store.getSafeString("storeVo.distance");
                            myLikeStoreItem.likeCount = store.getInt("storeVo.likeCount");
                            myLikeStoreItem.storeFigureImageUrl = store.getSafeString("storeVo.storeFigureImageUrl");
                            myLikeStoreItem.className = store.getSafeString("storeVo.className").split(",")[0];

                            myLikeStoreItemList.add(myLikeStoreItem);
                        }
                        storeDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_STORE) { // 防止異步返回結果時，已經切換到其它TAB頁面
                            rvMyLikeList.setAdapter(myLikeStoreAdapter);
                        }
                        myLikeStoreAdapter.setNewData(myLikeStoreItemList);
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void loadMyLikeGoods() {
        try {
            String token = User.getToken();
            //String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
                return;
            }

            String url = Api.PATH_MY_LIKE_GOODS;
            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName, "token", token);
            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

            SLog.info("params[%s]", params);
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                            return;
                        }

                        EasyJSONArray goodsList = responseObj.getSafeArray("datas.goodsList");
                        GoodsSearchItemPair pair = null;
                        for (Object object : goodsList) {
                            EasyJSONObject goods = (EasyJSONObject) object;

                            MyFollowGoodsItem good = new MyFollowGoodsItem();
                            good.commonId = goods.getInt("commonId");
                            good.goodsName = goods.getSafeString("goodsName");
                            good.storeId = goods.getInt("storeVo.storeId");
                            good.storeName = goods.getSafeString("storeVo.storeName");
                            good.storeAvatarUrl = goods.getSafeString("storeVo.storeAvatarUrl");
                            good.imageSrc = goods.getSafeString("imageSrc");
                            good.jingle = goods.getSafeString("jingle");
                            good.likeCount = goods.getInt("goodsLike");
                            good.price = Util.getSpuPrice(goods);
                            good.goodsModel = StringUtil.safeModel(goods);

                            GoodsSearchItem goodsSearchItem = new GoodsSearchItem(good.imageSrc,good.storeAvatarUrl,good.storeId,good.storeName,good.commonId,good.goodsName,good.jingle,good.price,null);
                            goodsSearchItem.goodsModel = good.goodsModel;
                            if (pair == null) {
                                pair = new GoodsSearchItemPair(Constant.ITEM_TYPE_NORMAL);
                                pair.left = goodsSearchItem;
                            } else {
                                pair.right = goodsSearchItem;
                                goodsItemPairList.add(pair);
                                pair = null;
                            }
                        }

                        goodsDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_GOODS) { // 防止異步返回結果時，已經切換到其它TAB頁面
                            rvMyLikeList.setAdapter(mGoodsAdapter);
                        }
                        mGoodsAdapter.setNewData(goodsItemPairList);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void loadMyLikeArticle() {
        try {
            String token = User.getToken();
            //String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
                return;
            }

            String url = Api.PATH_MY_LIKE_POST;
            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName, "token", token);
            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

            SLog.info("params[%s]", params);
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                            return;
                        }

                        EasyJSONArray wantPostList = responseObj.getSafeArray("datas.wantPostList");
                        for (Object object : wantPostList) {
                            EasyJSONObject post = (EasyJSONObject) object;
                            if (post.getInt("postType") == PostItem.POST_TYPE_WANT){
                                PostItem postItem = new PostItem();
                                SLog.info("Post!%s", post.toString());
                                postItem.postId = post.getInt("postId");
                                postItem.coverImage = post.getSafeString("coverImage");
                                postItem.postCategory = post.getSafeString("postCategory");
                                postItem.title = post.getSafeString("title");
                                postItem.authorAvatar = post.getSafeString("memberVo.avatar");
                                postItem.authorNickname = post.getSafeString("memberVo.nickName");
                                postItem.postThumb = post.getInt("postLike");

                                myLikeArticleItemList.add(postItem);
                            }
                        }

                        articleDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_ARTICLE) { // 防止異步返回結果時，已經切換到其它TAB頁面
                            rvMyLikeList.setAdapter(myLikeArticleAdapter);
                        }
                        myLikeArticleAdapter.setNewData(myLikeArticleItemList);
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
    private void replaceWord(View v){
        memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME,null);
        if(getArguments().containsKey("memberName")){
            if(!memberName.equals(getArguments().getString("memberName"))){
                memberName = getArguments().getString("memberName");
                ((TextView) v.findViewById(R.id.tv_fragment_title)).setText(getString(R.string.text_him_like));
            }
        }
    }
}
