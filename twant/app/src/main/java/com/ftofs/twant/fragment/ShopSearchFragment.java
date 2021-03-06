package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreSearchItemAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.lib_net.model.StoreLabel;
import com.ftofs.twant.entity.SearchItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 商店內產品搜索頁面
 * @author zwm
 */
public class ShopSearchFragment extends BaseFragment implements View.OnClickListener {
    int storeId;
    String extraData;

    EditText etKeyword;
    StoreSearchItemAdapter adapter;
    List<SearchItem> searchItemList = new ArrayList<>();

    /**
     * 新建新實例
     * @param storeId
     * @param extraData 如果extraData為空，會從網絡從重新加載extraData
     * @return
     */
    public static ShopSearchFragment newInstance(int storeId, String extraData) {
        Bundle args = new Bundle();

        args.putInt("storeId", storeId);
        args.putString("extraData", extraData);
        ShopSearchFragment fragment = new ShopSearchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        storeId = args.getInt("storeId");
        extraData = args.getString("extraData");

        etKeyword = view.findViewById(R.id.et_keyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch();
                    return true;
                }
                return false;
            }
        });

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_clear_all, this);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(_mActivity, 2);
        rvList.setLayoutManager(gridLayoutManager);
        adapter = new StoreSearchItemAdapter(searchItemList);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                SearchItem item = searchItemList.get(position);
                if (item.getItemType() == SearchItem.ITEM_TYPE_CATEGORY) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchItem item = searchItemList.get(position);
                // Label忽略點擊
                if (item.getItemType() == SearchItem.ITEM_TYPE_CATEGORY) { // 產品分類搜索
                    if (item.id == SearchItem.CATEGORY_ID_NEW) {
                        SLog.info("最新產品");
                        start(ShopCommodityFragment.newInstance(true, EasyJSONObject.generate(
                                "storeId", storeId,
                                "sort", "new_desc").toString()));
                    } else if (item.id == SearchItem.CATEGORY_ID_HOT) {
                        SLog.info("商店熱賣");
                        start(ShopCommodityFragment.newInstance(true, EasyJSONObject.generate(
                                "storeId", storeId,
                                "sort", "sale_desc").toString()));
                    } else {
                        SLog.info("id[%d], name[%s]", item.id, item.name);
                        start(ShopCommodityFragment.newInstance(true, EasyJSONObject.generate(
                                "storeId", storeId,
                                "labelId", item.id).toString()));
                    }
                } else { // 具體的產品，跳到產品詳情
                    redirectToGoodsDetailFragment(item.id);
                }
            }
        });
        rvList.setAdapter(adapter);

        if (!StringUtil.isEmpty(extraData)) {
            populateData(extraData);
        } else {
            loadShopSearchData();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_clear_all:
                etKeyword.setText("");
                break;
            default:
                break;
        }
    }

    private void doSearch() {
        // 顯示搜索結果頁面
        String keyword = etKeyword.getText().toString().trim();
        if (keyword.length() < 1) {
            ToastUtil.error(_mActivity, "請輸入搜索關鍵字");
            return;
        }
        hideSoftInput();
        // Util.startFragment(ShopCommodityFragment.newInstance(true, EasyJSONObject.generate("storeId", storeId, "keyword", keyword).toString()));
        Util.startFragment(ShopSearchResultFragment.newInstance(storeId, keyword));
    }

    /**
     * 填充數據
     * @param data JSON字符串格式
     */
    private void populateData(String data) {
        EasyJSONObject responseObj = EasyJSONObject.parse(data);
        if (ToastUtil.checkError(_mActivity, responseObj)) {
            return;
        }

        try {
            // 最新產品
            searchItemList.add(new SearchItem(SearchItem.ITEM_TYPE_CATEGORY, SearchItem.CATEGORY_ID_NEW, Util.getString(R.string.text_new_in_chinese)));
            EasyJSONArray newGoodsVoList = responseObj.getSafeArray("datas.newGoodsVoList");
            for (Object object : newGoodsVoList) {
                EasyJSONObject newGoodsVo = (EasyJSONObject) object;

                int commonId = newGoodsVo.getInt("commonId");
                String goodsName = newGoodsVo.getSafeString("goodsName");

                SearchItem searchItem = new SearchItem(SearchItem.ITEM_TYPE_GOODS, commonId, goodsName);
                searchItem.action = SearchItem.ACTION_GOTO_CATEGORY;
                searchItemList.add(searchItem);
            }

            // 商店熱賣
            searchItemList.add(new SearchItem(SearchItem.ITEM_TYPE_CATEGORY, SearchItem.CATEGORY_ID_HOT, getString(R.string.text_store_hot_item_chinese)));
            EasyJSONArray commendGoodsVoList = responseObj.getSafeArray("datas.commendGoodsVoList");
            for (Object object : commendGoodsVoList) {
                EasyJSONObject commendGoodsVo = (EasyJSONObject) object;

                int commonId = commendGoodsVo.getInt("commonId");
                String goodsName = commendGoodsVo.getSafeString("goodsName");

                SearchItem searchItem = new SearchItem(SearchItem.ITEM_TYPE_GOODS, commonId, goodsName);
                searchItem.action = SearchItem.ACTION_GOTO_GOODS;  // 跳轉到具體產品
                searchItemList.add(searchItem);
            }


            // 其它分類
            EasyJSONArray storeCategoryList = responseObj.getSafeArray("datas.storeCategoryList");
            for (Object object : storeCategoryList) {
                EasyJSONObject easyJSONObject = (EasyJSONObject) object;

                SearchItem searchItem = new SearchItem(SearchItem.ITEM_TYPE_CATEGORY,
                        easyJSONObject.getInt("storeLabelId"), easyJSONObject.getSafeString("storeLabelName"));
                searchItem.storeId = easyJSONObject.getInt("storeId");
                searchItem.action = SearchItem.ACTION_GOTO_CATEGORY;
                searchItemList.add(searchItem);

                EasyJSONArray storeLabelList = easyJSONObject.getSafeArray("storeLabelList");
                if (storeLabelList != null && storeLabelList.length() > 0) {
                    for (Object object2 : storeLabelList) {
                        EasyJSONObject easyJSONObject2 = (EasyJSONObject) object2;
                        StoreLabel storeLabel2 = new StoreLabel();
                        storeLabel2.setStoreLabelId(easyJSONObject2.getInt("storeLabelId"));
                        storeLabel2.setStoreLabelName(easyJSONObject2.getSafeString("storeLabelName"));
                        storeLabel2.setParentId(easyJSONObject2.getInt("parentId"));
                        storeLabel2.setStoreId(easyJSONObject2.getInt("storeId"));

                        // 2級Item
                        SearchItem searchItem2 = new SearchItem(SearchItem.ITEM_TYPE_GOODS,
                                easyJSONObject2.getInt("storeLabelId"), easyJSONObject2.getSafeString("storeLabelName"));
                        searchItem2.storeId = easyJSONObject2.getInt("storeId");
                        searchItem.action = SearchItem.ACTION_GOTO_CATEGORY;
                        searchItemList.add(searchItem2);
                    }
                }
            }

            adapter.setNewData(searchItemList);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    /**
     * 中轉到產品詳情頁面
     * @param commonId
     */
    private void redirectToGoodsDetailFragment(int commonId) {
        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
    }

    private void loadShopSearchData() {
        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", storeId);

        Api.getUI(Api.PATH_STORE_CATEGORY, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                populateData(responseStr);
            }
        });
    }
}
