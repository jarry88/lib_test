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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsSearchResultAdapter;
import com.ftofs.twant.adapter.StoreSearchResultAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.FilterCategoryGroup;
import com.ftofs.twant.entity.FilterCategoryItem;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.StoreSearchItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
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
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 搜索结果Fragment
 * @author zwm
 */
public class SearchResultFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    SearchType searchType;

    int twBlack;
    int twRed;

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

    List<FilterCategoryGroup> filterCategoryGroupList = new ArrayList<>();

    ImageView iconPriceOrder;
    boolean sortPriceAsc = true;  // 是否用升序來進行價格排序

    public static final int STORE_SEARCH_SORT_GENERAL = 0;
    public static final int STORE_SEARCH_SORT_SALE = 1;
    public static final int STORE_SEARCH_SORT_FOLLOW = 2;
    int storeSortButtonIndex = STORE_SEARCH_SORT_GENERAL; // 店鋪搜索當前用哪種排序標準 0,1,2
    TextView[] storeSortButtons = new TextView[3];

    public static final int GOODS_SEARCH_SORT_GENERAL = 0;
    public static final int GOODS_SEARCH_SORT_SALE = 1;
    public static final int GOODS_SEARCH_SORT_PRICE = 2;
    int goodsSortButtonIndex = GOODS_SEARCH_SORT_GENERAL; // 商品搜索當前用哪種排序標準 0,1,2
    TextView[] goodsSortButtons = new TextView[3];

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
        searchType = SearchType.valueOf(searchTypeStr);
        String paramsStr = args.getString("paramsStr");
        paramsObj = (EasyJSONObject) EasyJSONObject.parse(paramsStr);

        twBlack = getResources().getColor(R.color.tw_black, null);
        twRed = getResources().getColor(R.color.tw_red, null);

        if (searchTypeStr.equals(SearchType.GOODS.name())) {
            view.findViewById(R.id.ll_store_filter).setVisibility(View.GONE);
            view.findViewById(R.id.ll_goods_filter).setVisibility(View.VISIBLE);
        } else if (searchTypeStr.equals(SearchType.STORE.name())) {
            view.findViewById(R.id.ll_goods_filter).setVisibility(View.GONE);
            view.findViewById(R.id.ll_store_filter).setVisibility(View.VISIBLE);
        }

        iconPriceOrder = view.findViewById(R.id.icon_price_order);

        storeSortButtons[STORE_SEARCH_SORT_GENERAL] = view.findViewById(R.id.btn_store_search_order_general);
        storeSortButtons[STORE_SEARCH_SORT_GENERAL].setOnClickListener(this);
        storeSortButtons[STORE_SEARCH_SORT_SALE] = view.findViewById(R.id.btn_store_search_order_sale);
        storeSortButtons[STORE_SEARCH_SORT_SALE].setOnClickListener(this);
        storeSortButtons[STORE_SEARCH_SORT_FOLLOW] = view.findViewById(R.id.btn_store_search_order_follow);
        storeSortButtons[STORE_SEARCH_SORT_FOLLOW].setOnClickListener(this);

        goodsSortButtons[GOODS_SEARCH_SORT_GENERAL] = view.findViewById(R.id.tv_text_general);
        goodsSortButtons[GOODS_SEARCH_SORT_SALE] = view.findViewById(R.id.tv_text_sale);
        goodsSortButtons[GOODS_SEARCH_SORT_PRICE] = view.findViewById(R.id.tv_text_price);
        Util.setOnClickListener(view, R.id.btn_sort_goods_general, this);
        Util.setOnClickListener(view, R.id.btn_sort_goods_sale, this);
        Util.setOnClickListener(view, R.id.btn_sort_goods_price, this);

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

        doSearch(searchType, keyword, null);
    }


    private void doSearch(SearchType searchType, String keyword, EasyJSONObject filter) {
        SLog.info("searchType[%s], keyword[%s]", searchType, keyword);

        final BasePopupView loadingPopup = new XPopup.Builder(getContext())
                .asLoading(getString(R.string.text_loading))
                .show();

        int searchTypeInt = searchType.ordinal();
        SearchHistoryUtil.saveSearchHistory(searchTypeInt, keyword);

        try {
            EasyJSONObject params = EasyJSONObject.generate();
            for (Map.Entry<String, Object> param : paramsObj.entrySet()) {
                params.set(param.getKey(), param.getValue().toString());
            }

            // 合并參數
            if (filter != null) {
                for (Map.Entry<String, Object> param : filter.entrySet()) {
                    params.set(param.getKey(), param.getValue().toString());
                }
            }

            SLog.info("params[%s]", params);

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

                            goodsItemList.clear();
                            filterCategoryGroupList.clear();

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
                                goodsItemList.add(new GoodsSearchItem(imageSrc, storeAvatarUrl, storeId,
                                        storeName, commonId, goodsName, jingle, price, nationalFlag));
                            }

                            SLog.info("goodsItemList.size[%d]", goodsItemList.size());
                            mGoodsAdapter.setNewData(goodsItemList);

                            // 讀取過濾條件數據
                            EasyJSONArray categoryNavVoList = responseObj.getArray("datas.filter.categoryNavVoList");
                            for (Object object : categoryNavVoList) {
                                EasyJSONObject categoryNavVo = (EasyJSONObject) object;
                                int headId = categoryNavVo.getInt("categoryId");
                                String headName = categoryNavVo.getString("categoryName");
                                FilterCategoryItem head = new FilterCategoryItem(headId, headName);

                                FilterCategoryGroup group = new FilterCategoryGroup();
                                group.head = head;
                                EasyJSONArray categoryList = categoryNavVo.getArray("categoryList");
                                for (Object object2 : categoryList) {
                                    EasyJSONObject category = (EasyJSONObject) object2;

                                    int categoryId = category.getInt("categoryId");
                                    String categoryName = category.getString("categoryName");

                                    FilterCategoryItem item = new FilterCategoryItem(categoryId, categoryName);
                                    group.list.add(item);
                                }
                                filterCategoryGroupList.add(group);
                            }
                        } catch (Exception e) {
                            SLog.info("Error!%s", e.getMessage());
                        }
                    }
                });
            } else if (searchType == SearchType.STORE) {
                Api.getUI(Api.PATH_SEARCH_STORE, params, new UICallback() {
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

                            storeItemList.clear();
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
        } catch (Exception e) {
            SLog.info("Error!%s", e.getMessage());
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
            case R.id.btn_store_search_order_general:
                if (storeSortButtonIndex == STORE_SEARCH_SORT_GENERAL) {
                    return;
                }
                doSearch(searchType, keyword, null);
                storeSortButtons[storeSortButtonIndex].setTextColor(twBlack);
                storeSortButtonIndex = STORE_SEARCH_SORT_GENERAL;
                storeSortButtons[storeSortButtonIndex].setTextColor(twRed);
                break;
            case R.id.btn_store_search_order_sale:
                if (storeSortButtonIndex == STORE_SEARCH_SORT_SALE) {
                    return;
                }
                doSearch(searchType, keyword, EasyJSONObject.generate("sort", "sale_desc"));
                storeSortButtons[storeSortButtonIndex].setTextColor(twBlack);
                storeSortButtonIndex = STORE_SEARCH_SORT_SALE;
                storeSortButtons[storeSortButtonIndex].setTextColor(twRed);
                break;
            case R.id.btn_store_search_order_follow:
                if (storeSortButtonIndex == STORE_SEARCH_SORT_FOLLOW) {
                    return;
                }
                doSearch(searchType, keyword, EasyJSONObject.generate("sort", "collect_desc"));
                storeSortButtons[storeSortButtonIndex].setTextColor(twBlack);
                storeSortButtonIndex = STORE_SEARCH_SORT_FOLLOW;
                storeSortButtons[storeSortButtonIndex].setTextColor(twRed);
                break;
            case R.id.btn_sort_goods_general:
                if (goodsSortButtonIndex == GOODS_SEARCH_SORT_GENERAL) {
                    return;
                }
                doSearch(searchType, keyword, null);
                switchGoodsSortIndicator(id);
                break;
            case R.id.btn_sort_goods_sale:
                if (goodsSortButtonIndex == GOODS_SEARCH_SORT_SALE) {
                    return;
                }
                doSearch(searchType, keyword, EasyJSONObject.generate("sort", "sale_desc"));
                switchGoodsSortIndicator(id);
                break;
            case R.id.btn_sort_goods_price:
                if (goodsSortButtonIndex == GOODS_SEARCH_SORT_PRICE) { // 再次點擊價格排序的話，切換升降序
                    sortPriceAsc = !sortPriceAsc;
                } else {
                    sortPriceAsc = true;
                }
                String priceSortStr = "price_desc";
                if (sortPriceAsc) {
                    priceSortStr = "price_asc";
                }
                doSearch(searchType, keyword, EasyJSONObject.generate("sort", priceSortStr));
                switchGoodsSortIndicator(id);
                break;
            default:
                break;
        }
    }

    /**
     * 切換當前商品排序的指示
     * @param id 點擊到哪個按鈕
     */
    private void switchGoodsSortIndicator(int id) {
        goodsSortButtons[goodsSortButtonIndex].setTextColor(twBlack);
        if (goodsSortButtonIndex == GOODS_SEARCH_SORT_PRICE) { // 如果原來是用價格排序，則隱藏價格升序、降序圖標
            iconPriceOrder.setVisibility(View.GONE);
        }
        if (id == R.id.btn_sort_goods_general) {
            goodsSortButtonIndex = GOODS_SEARCH_SORT_GENERAL;
        } else if (id == R.id.btn_sort_goods_sale) {
            goodsSortButtonIndex = GOODS_SEARCH_SORT_SALE;
        } else {
            goodsSortButtonIndex = GOODS_SEARCH_SORT_PRICE;
        }
        goodsSortButtons[goodsSortButtonIndex].setTextColor(twRed);
        if (goodsSortButtonIndex == GOODS_SEARCH_SORT_PRICE) { // 如果現在是用價格排序，則顯示價格升序、降序圖標
            if (sortPriceAsc) {
                iconPriceOrder.setImageResource(R.drawable.icon_price_sort_asc);
            } else {
                iconPriceOrder.setImageResource(R.drawable.icon_price_sort_desc);
            }
            iconPriceOrder.setVisibility(View.VISIBLE);
        }
    }


    private void showGoodsFilterPopup() {
        new XPopup.Builder(getContext())
                .popupPosition(PopupPosition.Right)//右边
                .hasStatusBarShadow(true) //启用状态栏阴影
                .asCustom(new GoodsFilterDrawerPopupView(_mActivity, filterCategoryGroupList, this))
                .show();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        EasyJSONObject params = (EasyJSONObject) extra;
        SLog.info("params[%s]", params.toString());

        doSearch(searchType, keyword, params);
    }
}
