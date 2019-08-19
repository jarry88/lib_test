package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.MyLikeArticleAdapter;
import com.ftofs.twant.adapter.MyLikeGoodsAdapter;
import com.ftofs.twant.adapter.MyLikeStoreAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.MyLikeGoodsItem;
import com.ftofs.twant.entity.MyLikeStoreItem;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 我的點贊Fragment
 * @author zwm
 */
public class MyLikeFragment extends BaseFragment implements View.OnClickListener {
    List<MyLikeStoreItem> myLikeStoreItemList = new ArrayList<>();
    List<MyLikeGoodsItem> myLikeGoodsItemList = new ArrayList<>();
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

    public static MyLikeFragment newInstance() {
        Bundle args = new Bundle();

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
                } else if (id == R.id.btn_goods) {
                    currTabIndex = TAB_INDEX_GOODS;
                    if (goodsDataLoaded) {
                        rvMyLikeList.setAdapter(myLikeGoodsAdapter);
                    } else {
                        loadMyLikeGoods();
                    }
                } else if (id == R.id.btn_article) {
                    currTabIndex = TAB_INDEX_ARTICLE;
                    if (articleDataLoaded) {
                        rvMyLikeList.setAdapter(myLikeArticleAdapter);
                    } else {
                        loadMyLikeArticle();
                    }
                }
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_store));
        simpleTabManager.add(view.findViewById(R.id.btn_goods));
        simpleTabManager.add(view.findViewById(R.id.btn_article));

        rvMyLikeList = view.findViewById(R.id.rv_my_like_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvMyLikeList.setLayoutManager(layoutManager);
        myLikeStoreAdapter = new MyLikeStoreAdapter(R.layout.my_like_store_item, myLikeStoreItemList);
        myLikeGoodsAdapter = new MyLikeGoodsAdapter(R.layout.my_like_goods_item, myLikeGoodsItemList);
        myLikeArticleAdapter = new MyLikeArticleAdapter(R.layout.my_like_article_item, myLikeArticleItemList);

        rvMyLikeList.setAdapter(myLikeStoreAdapter);

        loadMyLikeStore();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    private void loadMyLikeStore() {
        try {
            String token = User.getToken();
            String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName, "token", token);

            final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                    .asLoading(getString(R.string.text_loading))
                    .show();

            Api.postUI(Api.PATH_MY_LIKE_STORE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONArray storeList = responseObj.getArray("datas.storeList");
                        for (Object object : storeList) {
                            EasyJSONObject store = (EasyJSONObject) object;

                            MyLikeStoreItem myLikeStoreItem = new MyLikeStoreItem();
                            myLikeStoreItem.storeId = store.getInt("storeVo.storeId");
                            myLikeStoreItem.storeAvatar = store.getString("storeVo.storeAvatarUrl");
                            myLikeStoreItem.storeName = store.getString("storeVo.storeName");
                            myLikeStoreItem.storeAddress = store.getString("storeVo.chainAreaInfo");
                            myLikeStoreItem.storeDistanceStr = store.getString("storeVo.distance");
                            myLikeStoreItem.likeCount = store.getInt("storeVo.likeCount");

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
            String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName, "token", token);

            final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                    .asLoading(getString(R.string.text_loading))
                    .show();

            Api.postUI(Api.PATH_MY_LIKE_GOODS, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONArray goodsList = responseObj.getArray("datas.goodsList");
                        if (!Util.isJsonNull(goodsList)) {
                            for (Object object : goodsList) {
                                EasyJSONObject goods = (EasyJSONObject) object;

                                MyLikeGoodsItem myLikeGoodsItem = new MyLikeGoodsItem();
                                myLikeGoodsItem.commonId = goods.getInt("commonId");
                                myLikeGoodsItem.goodsName = goods.getString("goodsName");
                                myLikeGoodsItem.storeId = goods.getInt("storeVo.storeId");
                                myLikeGoodsItem.storeName = goods.getString("storeVo.storeName");
                                myLikeGoodsItem.imageSrc = goods.getString("imageSrc");
                                myLikeGoodsItem.jingle = goods.getString("jingle");
                                myLikeGoodsItem.likeCount = goods.getInt("goodsLike");
                                myLikeGoodsItem.price = Util.getGoodsPrice(goods);

                                myLikeGoodsItemList.add(myLikeGoodsItem);
                            }
                        }

                        goodsDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_GOODS) { // 防止異步返回結果時，已經切換到其它TAB頁面
                            rvMyLikeList.setAdapter(myLikeGoodsAdapter);
                        }
                        myLikeGoodsAdapter.setNewData(myLikeGoodsItemList);
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void loadMyLikeArticle() {
        try {
            String token = User.getToken();
            String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(token) || StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName, "token", token);

            final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                    .asLoading(getString(R.string.text_loading))
                    .show();

            SLog.info("params[%s]", params);
            Api.postUI(Api.PATH_MY_LIKE_POST, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONArray wantPostList = responseObj.getArray("datas.wantPostList");
                        if (!Util.isJsonNull(wantPostList)) {
                            for (Object object : wantPostList) {
                                EasyJSONObject post = (EasyJSONObject) object;

                                PostItem postItem = new PostItem();


                                myLikeArticleItemList.add(postItem);
                            }
                        }

                        articleDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_ARTICLE) { // 防止異步返回結果時，已經切換到其它TAB頁面
                            rvMyLikeList.setAdapter(myLikeArticleAdapter);
                        }
                        myLikeArticleAdapter.setNewData(myLikeArticleItemList);
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {

        }
    }
}
