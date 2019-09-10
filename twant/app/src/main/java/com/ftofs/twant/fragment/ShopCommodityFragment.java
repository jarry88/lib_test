package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ShopGoodsAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 店鋪商品Fragment
 * @author zwm
 */
public class ShopCommodityFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    ShopMainFragment parentFragment;

    RecyclerView rvGoodsList;
    ImageView imgPriceOrderIndicator;
    ShopGoodsAdapter adapter;

    List<Goods> goodsList = new ArrayList<>();

    boolean isStandalone;
    int storeId;
    EasyJSONObject paramsOriginal; // 傳進來的參數
    EasyJSONObject mExtra;
    boolean priceAsc;

    // 當前加載第幾頁
    int currPage = 0;
    boolean hasMore;


    /**
     * 新建一個實例
     * @param isStandalone
     * @param paramsStr JSON字符串格式參數  必傳，最少要傳一個storeId
     * @return
     */
    public static ShopCommodityFragment newInstance(boolean isStandalone, String paramsStr) {
        Bundle args = new Bundle();

        args.putBoolean("isStandalone", isStandalone);
        args.putString("paramsStr", paramsStr);
        SLog.info("paramsStr[%s]", paramsStr);

        ShopCommodityFragment fragment = new ShopCommodityFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_commodity, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (ShopMainFragment) getParentFragment();

        Bundle args = getArguments();
        isStandalone = args.getBoolean("isStandalone");
        String paramsStr = args.getString("paramsStr");
        paramsOriginal = (EasyJSONObject) EasyJSONObject.parse(paramsStr);
        try {
            storeId = paramsOriginal.getInt("storeId");
        } catch (EasyJSONException e) {
            e.printStackTrace();
        }


        if (isStandalone) {
            view.findViewById(R.id.tool_bar).setVisibility(View.VISIBLE);
            Util.setOnClickListener(view, R.id.btn_search_goods, this);
            Util.setOnClickListener(view, R.id.btn_back, this);
        } else {
            view.findViewById(R.id.tool_bar).setVisibility(View.GONE);
        }


        SimpleTabManager simpleTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                if (isRepeat && id != R.id.btn_order_price) {
                    SLog.info("重復點擊");
                    return;
                }

                imgPriceOrderIndicator.setVisibility(View.GONE);
                if (id == R.id.btn_order_general) { // 綜合
                    SLog.info("btn_order_general");
                    goodsList.clear();
                    mExtra = null;
                    currPage = 0;
                    loadStoreGoods(paramsOriginal, mExtra, 1);
                } else if (id == R.id.btn_order_sale) { // 銷量
                    SLog.info("btn_order_sale");
                    goodsList.clear();
                    currPage = 0;
                    mExtra = EasyJSONObject.generate("sort", "sale_desc");
                    loadStoreGoods(paramsOriginal, mExtra, 1);
                } else if (id == R.id.btn_order_new) { // 上新
                    SLog.info("btn_order_new");
                    goodsList.clear();
                    currPage = 0;
                    mExtra = EasyJSONObject.generate("sort", "new_desc");
                    loadStoreGoods(paramsOriginal, mExtra, 1);
                } else if (id == R.id.btn_order_price) { // 價格
                    if (isRepeat) {
                        // 如果是再次點擊的話，切換排序順序
                        priceAsc = !priceAsc;
                    } else {
                        // 如果是首次點擊的話，默認升序排序
                        priceAsc = true;
                    }
                    String sort;
                    if (priceAsc) { // 價格降序
                        sort = "price_desc";
                        imgPriceOrderIndicator.setImageResource(R.drawable.icon_price_sort_desc);
                    } else { // 價格升序
                        sort = "price_asc";
                        imgPriceOrderIndicator.setImageResource(R.drawable.icon_price_sort_asc);
                    }
                    imgPriceOrderIndicator.setVisibility(View.VISIBLE);

                    SLog.info("btn_order_price");
                    goodsList.clear();
                    currPage = 0;
                    mExtra = EasyJSONObject.generate("sort", sort);
                    loadStoreGoods(paramsOriginal, mExtra, 1);
                }
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_order_general));
        simpleTabManager.add(view.findViewById(R.id.btn_order_sale));
        simpleTabManager.add(view.findViewById(R.id.btn_order_new));
        simpleTabManager.add(view.findViewById(R.id.btn_order_price));

        rvGoodsList = view.findViewById(R.id.rv_goods_list);
        imgPriceOrderIndicator = view.findViewById(R.id.img_price_order_indicator);

        GridLayoutManager layoutManagerCommodity = new GridLayoutManager(_mActivity, 2);
        layoutManagerCommodity.setOrientation(GridLayoutManager.VERTICAL);
        rvGoodsList.setLayoutManager(layoutManagerCommodity);
        adapter = new ShopGoodsAdapter(_mActivity, goodsList);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                Goods goods = goodsList.get(position);
                if (goods.getItemType() == Goods.ITEM_TYPE_LOAD_END_HINT) {
                    // padding占滿整個列的寬度
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Goods goods = goodsList.get(position);
                // padding忽略點擊
                if (goods.getItemType() == Goods.ITEM_TYPE_LOAD_END_HINT) {
                    return;
                }
                Util.startFragment(GoodsDetailFragment.newInstance(goods.id, 0));
            }
        });
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvGoodsList);


        rvGoodsList.setAdapter(adapter);

        if (paramsOriginal.exists("sort")) {
            String sort = null;
            try {
                sort = paramsOriginal.getString("sort");
            } catch (EasyJSONException e) {
                e.printStackTrace();
            }

            if ("new_desc".equals(sort)) {  // 最新商品
                simpleTabManager.performClick(2);
            } else if ("sale_desc".equals(sort)) { // 店鋪熱賣
                simpleTabManager.performClick(1);
            }
        } else {
            loadStoreGoods(paramsOriginal, null, 1);
        }
    }

    /**
     * 加載店鋪商品
     * @param paramsOriginal
     * @param extra 額外的搜索參數
     * @param page 第幾頁
     */
    private void loadStoreGoods(EasyJSONObject paramsOriginal, EasyJSONObject extra, int page) {
        try {
            EasyJSONObject params = EasyJSONObject.generate();
            for (Map.Entry<String, Object> param : paramsOriginal.entrySet()) {
                params.set(param.getKey(), param.getValue().toString());
            }

            // 合并參數
            if (extra != null) {
                for (Map.Entry<String, Object> param : extra.entrySet()) {
                    params.set(param.getKey(), param.getValue().toString());
                }
            }

            params.set("page", page);

            SLog.info("店鋪內商品搜索,params[%s]", params.toString());
            Api.getUI(Api.PATH_SEARCH_GOODS_IN_STORE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    adapter.loadMoreFail();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);
                    try {
                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
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

                        EasyJSONArray goodsArray = responseObj.getArray("datas.goodsCommonList");
                        for (Object object : goodsArray) {
                            EasyJSONObject goodsObject = (EasyJSONObject) object;

                            int id = goodsObject.getInt("commonId");
                            // 商品圖片
                            String goodsImageUrl = goodsObject.getString("imageSrc");
                            // 商品名稱
                            String goodsName = goodsObject.getString("goodsName");
                            // 賣點
                            String jingle = goodsObject.getString("jingle");
                            // 獲取價格
                            double price = Util.getSpuPrice(goodsObject);

                            Goods goods = new Goods(id, goodsImageUrl, goodsName, jingle, price);
                            goodsList.add(goods);
                        }

                        if (!hasMore) {
                            // 如果全部加載完畢，添加加載完畢的提示
                            Goods goods = new Goods();
                            goodsList.add(goods);
                        }

                        adapter.setNewData(goodsList);
                        adapter.loadMoreComplete();

                        currPage++;
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!%s", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_search_goods) {
            Util.startFragment(ShopSearchFragment.newInstance(storeId, null));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (parentFragment != null) {
            // 如果父Fragment不為空，表明是依附在父Fragment中的，pop出父Fragment
            parentFragment.pop();
        } else {
            pop();
        }

        return true;
    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadStoreGoods(paramsOriginal, mExtra, currPage + 1);
    }
}

