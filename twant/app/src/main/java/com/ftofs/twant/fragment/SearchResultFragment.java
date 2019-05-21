package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsSearchResultAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 搜索结果Fragment
 * @author zwm
 */
public class SearchResultFragment extends BaseFragment implements View.OnClickListener {
    GoodsSearchResultAdapter adapter;

    List<GoodsSearchItem> goodsItemList = new ArrayList<>();

    public static SearchResultFragment newInstance(String searchTypeStr, String keyword) {
        Bundle args = new Bundle();

        args.putString("searchTypeStr", searchTypeStr);
        args.putString("keyword", keyword);
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
        String keyword = args.getString("keyword");

        Util.setOnClickListener(view, R.id.btn_back, this);

        RecyclerView rvGoodsList = view.findViewById(R.id.rv_goods_list);
        GridLayoutManager layoutManager = new GridLayoutManager(_mActivity, 2, GridLayoutManager.VERTICAL, false);
        rvGoodsList.setLayoutManager(layoutManager);
        adapter = new GoodsSearchResultAdapter(_mActivity, R.layout.goods_search_item, goodsItemList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                SLog.info("id[%d]", id);

            }
        });
        rvGoodsList.setAdapter(adapter);

        doSearch(searchType, keyword);
    }


    private void doSearch(SearchType searchType, String keyword) {
        SLog.info("searchType[%s], keyword[%s]", searchType, keyword);
        EasyJSONObject params;
        if (searchType == SearchType.GOODS) {
            params = EasyJSONObject.generate("keyword", keyword);
            Api.getUI(Api.PATH_SEARCH_GOODS, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseStr = response.body().string();
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

                            goodsItemList.add(new GoodsSearchItem(imageSrc, storeAvatarUrl, storeName, commonId, goodsName, jingle, price));
                        }

                        SLog.info("goodsItemList.size[%d]", goodsItemList.size());
                        adapter.setNewData(goodsItemList);
                    } catch (Exception e) {
                        SLog.info("Error!%s", e.getMessage());
                    }
                }
            });
        } else if (searchType == SearchType.STORE) {
            params = EasyJSONObject.generate();
            Api.postUI(Api.PATH_SEARCH_STORE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        } else {

        }
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
