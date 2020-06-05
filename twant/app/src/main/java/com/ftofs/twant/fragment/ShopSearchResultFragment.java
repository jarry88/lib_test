package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ShopGoodsGridAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.GoodsPair;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 店鋪內商品搜索結果頁
 * @author zwm
 */
public class ShopSearchResultFragment extends BaseFragment implements View.OnClickListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    int storeId;
    String keyword;

    RecyclerView rvList;
    ShopGoodsGridAdapter shopGoodsGridAdapter;
    List<GoodsPair> goodsPairList = new ArrayList<>();  // 每行顯示兩個產品
    GoodsPair currGoodsPair;  // 當前處理的goodsPair，考慮到分頁時，加載到奇數個產品，所以要預存一下GoodsPair

    // 當前加載第幾頁
    int currPage = 0;
    boolean hasMore;

    EditText etKeyword;

    public static ShopSearchResultFragment newInstance(int storeId, String keyword) {
        Bundle args = new Bundle();

        ShopSearchResultFragment fragment = new ShopSearchResultFragment();
        fragment.setArguments(args);
        fragment.storeId = storeId;
        fragment.keyword = keyword;

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_search_result, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        shopGoodsGridAdapter = new ShopGoodsGridAdapter(_mActivity, goodsPairList);
        shopGoodsGridAdapter.setEnableLoadMore(true);
        shopGoodsGridAdapter.setOnLoadMoreListener(this, rvList);
        rvList.setAdapter(shopGoodsGridAdapter);

        etKeyword = view.findViewById(R.id.et_keyword);
        if (!StringUtil.isEmpty(keyword)) {
            etKeyword.setText(keyword);
        }

        loadStoreGoods(currPage + 1);
    }

    /**
     * 加載商店產品
     * @param page 第幾頁
     */
    private void loadStoreGoods(int page) {
        try {
            EasyJSONObject params = EasyJSONObject.generate(
                    "storeId", storeId,
                    "page", page
            );

            if (!StringUtil.isEmpty(keyword)) {
                params.set("keyword", keyword);
            }

            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
            SLog.info("商店內產品搜索,params[%s]", params.toString());
            Api.getUI(Api.PATH_SEARCH_GOODS_IN_STORE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    loadingPopup.dismiss();

                    ToastUtil.showNetworkError(_mActivity, e);
                    shopGoodsGridAdapter.loadMoreFail();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    SLog.info("responseStr[%s]", responseStr);
                    try {
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            shopGoodsGridAdapter.loadMoreFail();
                            return;
                        }

                        hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                        SLog.info("hasMore[%s]", hasMore);
                        if (!hasMore) {
                            shopGoodsGridAdapter.loadMoreEnd();
                            shopGoodsGridAdapter.setEnableLoadMore(false);
                        }

                        EasyJSONArray goodsArray = responseObj.getSafeArray("datas.goodsCommonList");
                        for (Object object : goodsArray) {
                            EasyJSONObject goodsObject = (EasyJSONObject) object;

                            int id = goodsObject.getInt("commonId");
                            // 產品圖片
                            String goodsImageUrl = goodsObject.getSafeString("imageSrc");
                            // 產品名稱
                            String goodsName = goodsObject.getSafeString("goodsName");
                            // 賣點
                            String jingle = goodsObject.getSafeString("jingle");
                            // 獲取價格
                            double price = Util.getSpuPrice(goodsObject);

                            Goods goods = new Goods(id, goodsImageUrl, goodsName, jingle, price);

                            if (currGoodsPair == null) {
                                currGoodsPair = new GoodsPair();
                            }

                            if (currGoodsPair.leftGoods == null) {
                                currGoodsPair.leftGoods = goods;
                            } else {
                                currGoodsPair.rightGoods = goods;
                                goodsPairList.add(currGoodsPair);

                                currGoodsPair = null;
                            }
                        }

                        // 如果剛好奇數個，可能沒添加到列表中
                        if (currGoodsPair != null) {
                            goodsPairList.add(currGoodsPair);
                            currGoodsPair = null;
                        }

                        shopGoodsGridAdapter.setNewData(goodsPairList);
                        shopGoodsGridAdapter.loadMoreComplete();

                        currPage++;
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    /**
     * 上滑加載更多
     */
    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            shopGoodsGridAdapter.setEnableLoadMore(false);
            return;
        }
        loadStoreGoods(currPage + 1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}



