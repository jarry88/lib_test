package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ShopGoodsAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 店鋪商品Fragment
 * @author zwm
 */
public class ShopCommodityFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    ShopMainFragment parentFragment;

    RecyclerView rvGoodsList;

    List<Goods> goodsList = new ArrayList<>();

    public static ShopCommodityFragment newInstance() {
        Bundle args = new Bundle();

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

        TabLayout tabLayout = view.findViewById(R.id.goods_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.goods_tab_title_general)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.goods_tab_title_sale)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.goods_tab_title_new)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.goods_tab_title_price)));

        rvGoodsList = view.findViewById(R.id.rv_goods_list);

        EasyJSONObject params = EasyJSONObject.generate("storeId", parentFragment.getShopId());
        Api.getUI(Api.PATH_SEARCH_GOODS_IN_STORE, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseStr = response.body().string();
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
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
                        double price;
                        // 獲取價格
                        int appUsable = goodsObject.getInt("appUsable");
                        if (appUsable > 0) {
                            price = goodsObject.getDouble("appPrice0");
                        } else {
                            price = goodsObject.getDouble("batchPrice2");
                        }

                        Goods goods = new Goods(id, goodsImageUrl, goodsName, jingle, price);
                        goodsList.add(goods);
                    }

                    GridLayoutManager layoutManagerCommodity = new GridLayoutManager(_mActivity, 2);
                    layoutManagerCommodity.setOrientation(GridLayoutManager.VERTICAL);
                    rvGoodsList.setLayoutManager(layoutManagerCommodity);
                    ShopGoodsAdapter adapter = new ShopGoodsAdapter(_mActivity, goodsList, ShopCommodityFragment.this);
                    rvGoodsList.setAdapter(adapter);
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {

    }

    /**
     * 打開商品詳情頁
     * @param id 商品Id
     */
    @Override
    public void onSelected(int type, int id, Object extra) {
        parentFragment.start(GoodsDetailFragment.newInstance(id));
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        ((SupportFragment) getParentFragment()).pop();
        return true;
    }
}

