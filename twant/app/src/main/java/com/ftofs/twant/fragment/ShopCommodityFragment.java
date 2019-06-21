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
public class ShopCommodityFragment extends BaseFragment implements View.OnClickListener {
    ShopMainFragment parentFragment;

    RecyclerView rvGoodsList;
    ImageView imgPriceOrderIndicator;
    ShopGoodsAdapter adapter;

    List<Goods> goodsList = new ArrayList<>();

    int storeId;
    EasyJSONObject paramsOriginal;
    boolean priceAsc;



    /**
     * 新建一個實例
     * @param paramsStr JSON字符串格式參數  必傳，最少要傳一個storeId
     * @return
     */
    public static ShopCommodityFragment newInstance(String paramsStr) {
        Bundle args = new Bundle();

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
        String paramsStr = args.getString("paramsStr");
        paramsOriginal = (EasyJSONObject) EasyJSONObject.parse(paramsStr);
        try {
            storeId = paramsOriginal.getInt("storeId");
        } catch (EasyJSONException e) {
            e.printStackTrace();
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
                if (id == R.id.btn_order_general) {
                    SLog.info("btn_order_general");
                    loadStoreGoods(paramsOriginal, null);
                } else if (id == R.id.btn_order_sale) {
                    SLog.info("btn_order_sale");
                    EasyJSONObject extra = EasyJSONObject.generate("sort", "sale_desc");
                    loadStoreGoods(paramsOriginal, extra);
                } else if (id == R.id.btn_order_new) {
                    SLog.info("btn_order_new");
                    EasyJSONObject extra = EasyJSONObject.generate("sort", "new_desc");
                    loadStoreGoods(paramsOriginal, extra);
                } else if (id == R.id.btn_order_price) {
                    if (isRepeat) {
                        // 如果是再次點擊的話，切換排序順序
                        priceAsc = !priceAsc;
                    } else {
                        // 如果是首次點擊的話，默認升序排序
                        priceAsc = true;
                    }
                    String sort;
                    if (priceAsc) {
                        sort = "price_desc";
                        imgPriceOrderIndicator.setImageResource(R.drawable.icon_price_sort_desc);
                    } else {
                        sort = "price_asc";
                        imgPriceOrderIndicator.setImageResource(R.drawable.icon_price_sort_asc);
                    }
                    imgPriceOrderIndicator.setVisibility(View.VISIBLE);

                    SLog.info("btn_order_price");
                    EasyJSONObject extra = EasyJSONObject.generate("sort", sort);
                    loadStoreGoods(paramsOriginal, extra);
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
                if (goods.getItemType() == Goods.ITEM_TYPE_PADDING) {
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
                if (goods.getItemType() == Goods.ITEM_TYPE_PADDING) {
                    return;
                }
                MainFragment mainFragment = MainFragment.getInstance();
                mainFragment.start(GoodsDetailFragment.newInstance(goods.id));
            }
        });
        rvGoodsList.setAdapter(adapter);

        loadStoreGoods(paramsOriginal, null);
    }

    /**
     * 加載店鋪商品
     * @param paramsOriginal
     * @param extra 額外的搜索參數
     */
    private void loadStoreGoods(EasyJSONObject paramsOriginal, EasyJSONObject extra) {
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

            SLog.info("店鋪內商品搜索,params[%s]", params.toString());
            Api.getUI(Api.PATH_SEARCH_GOODS_IN_STORE, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);
                    try {
                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        EasyJSONArray goodsArray = responseObj.getArray("datas.goodsCommonList");
                        goodsList.clear();
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
                            double price = Util.getGoodsPrice(goodsObject);

                            Goods goods = new Goods(id, goodsImageUrl, goodsName, jingle, price);
                            goodsList.add(goods);
                        }
                        // 添加一個防止底部工具欄擋住的Item
                        goodsList.add(new Goods());
                        adapter.setNewData(goodsList);
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
}

