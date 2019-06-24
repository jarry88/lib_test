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
import com.ftofs.twant.adapter.MyFollowArticleAdapter;
import com.ftofs.twant.adapter.MyFollowGoodsAdapter;
import com.ftofs.twant.adapter.MyFollowMemberAdapter;
import com.ftofs.twant.adapter.MyFollowRecruitmentAdapter;
import com.ftofs.twant.adapter.MyFollowStoreAdapter;
import com.ftofs.twant.adapter.TrustValueListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.MyFollowGoodsItem;
import com.ftofs.twant.entity.MyFollowStoreItem;
import com.ftofs.twant.entity.MyLikeStoreItem;
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
 * 我的關注Fragment
 * @author zwm
 */
public class MyFollowFragment extends BaseFragment implements View.OnClickListener {
    public static final int TAB_INDEX_STORE = 0;
    public static final int TAB_INDEX_GOODS = 1;
    public static final int TAB_INDEX_ARTICLE = 2;
    public static final int TAB_INDEX_RECRUITMENT = 3;
    public static final int TAB_INDEX_MEMBER = 4;

    List<MyFollowStoreItem> myFollowStoreItemList = new ArrayList<>();
    List<MyFollowGoodsItem> myFollowGoodsItemList = new ArrayList<>();

    int currTabIndex = TAB_INDEX_STORE;

    boolean storeDataLoaded = false;
    MyFollowStoreAdapter myFollowStoreAdapter;
    boolean goodsDataLoaded = false;
    MyFollowGoodsAdapter myFollowGoodsAdapter;
    boolean articleDataLoaded = false;
    MyFollowArticleAdapter myFollowArticleAdapter;
    boolean recruitmentDataLoaded = false;
    MyFollowRecruitmentAdapter myFollowRecruitmentAdapter;
    boolean memberDataLoaded = false;
    MyFollowMemberAdapter myFollowMemberAdapter;

    RecyclerView rvMyFollowList;

    public static MyFollowFragment newInstance() {
        Bundle args = new Bundle();

        MyFollowFragment fragment = new MyFollowFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_follow, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        rvMyFollowList = view.findViewById(R.id.rv_my_follow_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvMyFollowList.setLayoutManager(layoutManager);
        myFollowStoreAdapter = new MyFollowStoreAdapter(R.layout.my_follow_store_item, myFollowStoreItemList);
        myFollowGoodsAdapter = new MyFollowGoodsAdapter(R.layout.my_follow_goods_item, myFollowGoodsItemList);

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
                        rvMyFollowList.setAdapter(myFollowStoreAdapter);
                    } else {
                        loadMyFollowStore();
                    }
                } else if (id == R.id.btn_goods) {
                    currTabIndex = TAB_INDEX_GOODS;
                    if (goodsDataLoaded) {
                        rvMyFollowList.setAdapter(myFollowGoodsAdapter);
                    } else {
                        loadMyFollowGoods();
                    }
                } else if (id == R.id.btn_article) {
                    currTabIndex = TAB_INDEX_ARTICLE;
                    if (articleDataLoaded) {
                        rvMyFollowList.setAdapter(myFollowArticleAdapter);
                    } else {
                        loadMyFollowArticle();
                    }
                } else if (id == R.id.btn_recruitment) {
                    currTabIndex = TAB_INDEX_RECRUITMENT;
                    if (recruitmentDataLoaded) {
                        rvMyFollowList.setAdapter(myFollowRecruitmentAdapter);
                    } else {
                        loadMyFollowRecruitment();
                    }
                } else if (id == R.id.btn_member) {
                    currTabIndex = TAB_INDEX_MEMBER;
                    if (memberDataLoaded) {
                        rvMyFollowList.setAdapter(myFollowRecruitmentAdapter);
                    } else {
                        loadMyFollowMember();
                    }
                }
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_store));
        simpleTabManager.add(view.findViewById(R.id.btn_goods));
        simpleTabManager.add(view.findViewById(R.id.btn_article));
        simpleTabManager.add(view.findViewById(R.id.btn_recruitment));
        simpleTabManager.add(view.findViewById(R.id.btn_member));

        loadMyFollowStore();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    private void loadMyFollowStore() {
        try {
            String memberName = User.getMemberName();
            if (StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName);
            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }


            final BasePopupView loadingPopup = new XPopup.Builder(getContext())
                    .asLoading(getString(R.string.text_loading))
                    .show();

            Api.postUI(Api.PATH_MY_FOLLOW_STORE, params, new UICallback() {
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

                        EasyJSONArray storeList = responseObj.getArray("datas.sotreList");  // sotreList拼寫錯誤
                        for (Object object : storeList) {
                            EasyJSONObject store = (EasyJSONObject) object;

                            MyFollowStoreItem myFollowStoreItem = new MyFollowStoreItem();
                            myFollowStoreItem.storeId = store.getInt("storeVo.storeId");
                            myFollowStoreItem.storeAvatarUrl = store.getString("storeVo.storeAvatarUrl");
                            myFollowStoreItem.storeName = store.getString("storeVo.storeName");
                            myFollowStoreItem.chainAreaInfo = store.getString("storeVo.chainAreaInfo");
                            myFollowStoreItem.distance = store.getString("storeVo.distance");
                            myFollowStoreItem.collectCount = store.getInt("storeVo.likeCount");

                            myFollowStoreItemList.add(myFollowStoreItem);
                        }
                        SLog.info("ITEM_COUNT[%d]", myFollowStoreItemList.size());
                        storeDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_STORE) {
                            rvMyFollowList.setAdapter(myFollowStoreAdapter);
                        }
                        myFollowStoreAdapter.setNewData(myFollowStoreItemList);
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void loadMyFollowGoods() {
        try {
            String memberName = User.getMemberName();
            if (StringUtil.isEmpty(memberName)) {
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName);
            String token = User.getToken();
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }


            final BasePopupView loadingPopup = new XPopup.Builder(getContext())
                    .asLoading(getString(R.string.text_loading))
                    .show();

            Api.postUI(Api.PATH_MY_FOLLOW_GOODS, params, new UICallback() {
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
                        for (Object object : goodsList) {
                            EasyJSONObject goods = (EasyJSONObject) object;
                            EasyJSONObject goodsCommon = goods.getObject("goodsCommon");

                            MyFollowGoodsItem myFollowGoodsItem = new MyFollowGoodsItem();
                            myFollowGoodsItem.commonId = goods.getInt("commonId");
                            myFollowGoodsItem.goodsName = goods.getString("goodsName");
                            myFollowGoodsItem.storeId = goods.getInt("storeVo.storeId");
                            myFollowGoodsItem.storeName = goods.getString("storeVo.storeName");
                            myFollowGoodsItem.imageSrc = goodsCommon.getString("imageSrc");
                            myFollowGoodsItem.jingle = goodsCommon.getString("jingle");
                            myFollowGoodsItem.goodsFavorite = goodsCommon.getInt("goodsFavorite");
                            myFollowGoodsItem.price = Util.getGoodsPrice(goodsCommon);

                            myFollowGoodsItemList.add(myFollowGoodsItem);
                        }
                        SLog.info("ITEM_COUNT[%d]", myFollowGoodsItemList.size());
                        goodsDataLoaded = true;
                        if (currTabIndex == TAB_INDEX_GOODS) {
                            rvMyFollowList.setAdapter(myFollowGoodsAdapter);
                        }
                        myFollowGoodsAdapter.setNewData(myFollowGoodsItemList);
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void loadMyFollowArticle() {
        articleDataLoaded = true;
        if (currTabIndex == TAB_INDEX_ARTICLE) {
            rvMyFollowList.setAdapter(myFollowArticleAdapter);
        }
    }

    private void loadMyFollowRecruitment() {
        recruitmentDataLoaded = true;
        if (currTabIndex == TAB_INDEX_RECRUITMENT) {
            rvMyFollowList.setAdapter(myFollowRecruitmentAdapter);
        }
    }

    private void loadMyFollowMember() {
        memberDataLoaded = true;
        if (currTabIndex == TAB_INDEX_MEMBER) {
            rvMyFollowList.setAdapter(myFollowMemberAdapter);
        }
    }
}
