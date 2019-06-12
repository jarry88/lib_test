package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsSearchResultAdapter;
import com.ftofs.twant.adapter.StoreSearchResultAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.StoreSearchItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.SearchHistoryUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.GoodsFilterDrawerPopupView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.enums.PopupPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 搜索结果Fragment
 * @author zwm
 */
public class SearchResultFragment extends BaseFragment implements View.OnClickListener {
    GoodsSearchResultAdapter mGoodsAdapter;
    StoreSearchResultAdapter mStoreAdapter;

    List<GoodsSearchItem> goodsItemList = new ArrayList<>();
    List<StoreSearchItem> storeItemList = new ArrayList<>();
    EditText etKeyword;
    EasyJSONObject paramsObj;
    String keyword;

    RecyclerView rvSearchResultList;
    ImageView btnGotoTop;
    ImageView btnGotoCart;

    public static SearchResultFragment newInstance(String searchTypeStr, String paramsStr) {
        Bundle args = new Bundle();

        args.putString("searchTypeStr", searchTypeStr);
        args.putString("paramsStr", paramsStr);
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String searchTypeStr = args.getString("searchTypeStr");
        SearchType searchType = SearchType.valueOf(searchTypeStr);
        String paramsStr = args.getString("paramsStr");
        paramsObj = (EasyJSONObject) EasyJSONObject.parse(paramsStr);

        btnGotoTop = view.findViewById(R.id.btn_goto_top);
        btnGotoTop.setOnClickListener(this);
        btnGotoCart = view.findViewById(R.id.btn_goto_cart);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_goods_filter, this);
        etKeyword = view.findViewById(R.id.et_keyword);
        if (paramsObj.exists("keyword")) {
            try {
                keyword = paramsObj.getString("keyword");
            } catch (EasyJSONException e) {
                e.printStackTrace();
            }
            etKeyword.setText(keyword);
        }

        EditTextUtil.cursorSeekToEnd(etKeyword);

        if (searchType == SearchType.GOODS) {
            view.findViewById(R.id.ll_goods_filter).setVisibility(View.VISIBLE);
        } else if (searchType == SearchType.STORE) {
            view.findViewById(R.id.ll_store_filter).setVisibility(View.VISIBLE);

            // 如果是搜索店鋪，隱藏【返回頂部】和【轉到購物車】按鈕
            btnGotoTop.setVisibility(View.GONE);
            btnGotoCart.setVisibility(View.GONE);
        }

        rvSearchResultList = view.findViewById(R.id.rv_search_result_list);

        if (searchType == SearchType.GOODS) {
            GridLayoutManager layoutManager = new GridLayoutManager(_mActivity, 2, GridLayoutManager.VERTICAL, false);
            rvSearchResultList.setLayoutManager(layoutManager);
            mGoodsAdapter = new GoodsSearchResultAdapter(_mActivity, R.layout.goods_search_item, goodsItemList);
            mGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    int id = view.getId();
                    SLog.info("id[%d]", id);

                }
            });
            rvSearchResultList.setAdapter(mGoodsAdapter);
            mGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    GoodsSearchItem goodsSearchItem = goodsItemList.get(position);
                    int commonId = goodsSearchItem.commonId;
                    MainFragment mainFragment = MainFragment.getInstance();
                    mainFragment.start(GoodsDetailFragment.newInstance(commonId));
                }
            });
            mGoodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    int id = view.getId();
                    if (id == R.id.btn_goto_store) {
                        GoodsSearchItem item = goodsItemList.get(position);
                        MainFragment mainFragment = MainFragment.getInstance();
                        mainFragment.start(ShopMainFragment.newInstance(item.storeId));
                    }
                }
            });
        } else if (searchType == SearchType.STORE) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvSearchResultList.setLayoutManager(layoutManager);
            mStoreAdapter = new StoreSearchResultAdapter(R.layout.store_search_item, storeItemList);
            mStoreAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    int id = view.getId();
                    SLog.info("id[%d]", id);

                }
            });
            rvSearchResultList.setAdapter(mStoreAdapter);
            mStoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    StoreSearchItem storeSearchItem = storeItemList.get(position);
                    MainFragment mainFragment = MainFragment.getInstance();
                    mainFragment.start(ShopMainFragment.newInstance(storeSearchItem.storeId));
                }
            });
        } else {

        }

        doSearch(searchType, keyword);
    }


    private void doSearch(SearchType searchType, String keyword) {
        SLog.info("searchType[%s], keyword[%s]", searchType, keyword);

        final BasePopupView loadingPopup = new XPopup.Builder(getContext())
                .asLoading("正在加載")
                .show();

        int searchTypeInt = searchType.ordinal();
        SearchHistoryUtil.saveSearchHistory(searchTypeInt, keyword);

        if (searchType == SearchType.GOODS) {
            Api.getUI(Api.PATH_SEARCH_GOODS, paramsObj, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
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

                        EasyJSONArray easyJSONArray = responseObj.getArray("datas.goodsList");
                        for (Object object : easyJSONArray) {
                            EasyJSONObject goods = (EasyJSONObject) object;

                            String imageSrc = goods.getString("imageSrc");
                            String storeAvatarUrl = goods.getString("storeAvatarUrl");
                            int storeId = goods.getInt("storeId");
                            String storeName = goods.getString("storeName");
                            int commonId = goods.getInt("commonId");
                            String goodsName = goods.getString("goodsName");
                            String jingle  = goods.getString("jingle");
                            float price;
                            int appUsable = goods.getInt("appUsable");
                            if (appUsable > 0) {
                                price = (float) goods.getDouble("appPrice0");
                            } else {
                                price = (float) goods.getDouble("batchPrice2");
                            }
                            String nationalFlag = Config.OSS_BASE_URL + "/" + goods.getString("adminCountry.nationalFlag");
                            SLog.info("adminCountry.nationalFlag[%s]", nationalFlag);
                            goodsItemList.add(new GoodsSearchItem(imageSrc, storeAvatarUrl, storeId,
                                    storeName, commonId, goodsName, jingle, price, nationalFlag));
                        }

                        SLog.info("goodsItemList.size[%d]", goodsItemList.size());
                        mGoodsAdapter.setNewData(goodsItemList);
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } else if (searchType == SearchType.STORE) {
            Api.getUI(Api.PATH_SEARCH_STORE, paramsObj, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
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

                        EasyJSONArray easyJSONArray = responseObj.getArray("datas.storeList");
                        for (Object object : easyJSONArray) {
                            EasyJSONObject store = (EasyJSONObject) object;

                            int storeId = store.getInt("storeId");
                            String storeAvatarUrl = store.getString("storeAvatarUrl");
                            String storeName = store.getString("storeName");
                            String mainBusiness = store.getString("storeZy");
                            String storeFigureImage = store.getString("storeFigureImage");
                            float distance = Float.valueOf(store.getString("distance"));
                            String shopDay = store.getString("shopDay");
                            int likeCount = store.getInt("likeCount");
                            int goodsCommonCount = store.getInt("goodsCommonCount");

                            // 獲取店鋪的前3個商品的圖片
                            List<String> goodsImageList = new ArrayList<>();
                            EasyJSONArray goodsCommonList = store.getArray("goodsCommonList");
                            for (Object object2 : goodsCommonList) {
                                EasyJSONObject goodsCommon = (EasyJSONObject) object2;
                                goodsImageList.add(goodsCommon.getString("imageSrc"));
                            }

                            storeItemList.add(new StoreSearchItem(storeId, storeAvatarUrl, storeName, mainBusiness,
                                    storeFigureImage, distance, shopDay, likeCount, goodsCommonCount, goodsImageList));
                        }

                        SLog.info("storeItemList.size[%d]", storeItemList.size());
                        mStoreAdapter.setNewData(storeItemList);
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } else {

        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_goods_filter:
                showGoodsFilterPopup();
                break;
            case R.id.btn_goto_top:
                rvSearchResultList.scrollToPosition(0);
                break;
            default:
                break;
        }
    }


    private void showGoodsFilterPopup() {
        new XPopup.Builder(getContext())
                .popupPosition(PopupPosition.Right)//右边
                .hasStatusBarShadow(true) //启用状态栏阴影
                .asCustom(new GoodsFilterDrawerPopupView(_mActivity))
                .show();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
