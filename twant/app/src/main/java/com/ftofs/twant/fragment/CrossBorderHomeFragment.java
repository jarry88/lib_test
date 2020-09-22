package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.lib_net.model.Goods;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CrossBorderHomeAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CrossBorderActivityGoods;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.entity.CrossBorderHomeItem;
import com.ftofs.twant.entity.CrossBorderNavItem;
import com.ftofs.twant.entity.CrossBorderShoppingZoneItem;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.entity.Store;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 跨城購主頁
 * @author zwm
 */
public class CrossBorderHomeFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvList;
    CrossBorderHomeAdapter adapter;
    List<CrossBorderHomeItem> crossBorderHomeItemList = new ArrayList<>();

    List<CrossBorderBannerItem> bannerItemList;
    List<CrossBorderNavItem> navItemList;
    List<CrossBorderShoppingZoneItem> shoppingZoneList;
    List<CrossBorderActivityGoods> bargainGoodsList;
    List<CrossBorderActivityGoods> groupGoodsList;
    List<Store> storeList;

    public static CrossBorderHomeFragment newInstance(List<CrossBorderBannerItem> bannerItemList,
                                                      List<CrossBorderNavItem> navItemList,
                                                      List<CrossBorderShoppingZoneItem> shoppingZoneList,
                                                      List<CrossBorderActivityGoods> bargainGoodsList,
                                                      List<CrossBorderActivityGoods> groupGoodsList,
                                                      List<Store> storeList) {
        CrossBorderHomeFragment fragment = new CrossBorderHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        fragment.bannerItemList = bannerItemList;
        fragment.navItemList = navItemList;
        fragment.shoppingZoneList = shoppingZoneList;
        fragment.bargainGoodsList = bargainGoodsList;
        fragment.groupGoodsList = groupGoodsList;
        fragment.storeList = storeList;

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cross_border_home, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CrossBorderHomeItem header = new CrossBorderHomeItem();
        header.bannerItemList = bannerItemList;
        header.navItemList = navItemList;
        header.shoppingZoneList = shoppingZoneList;
        header.bargainGoodsList = bargainGoodsList;
        header.groupGoodsList = groupGoodsList;
        header.storeList = storeList;
        header.itemType = Constant.ITEM_TYPE_HEADER;

        crossBorderHomeItemList.add(header);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new CrossBorderHomeAdapter(_mActivity, crossBorderHomeItemList);
        rvList.setAdapter(adapter);

        loadData(1);
    }

    private void loadData(int page) {
        String url = Api.PATH_TARIFF_BUY_INDEX_GOODS;
        EasyJSONObject params = EasyJSONObject.generate(
                "page", page
        );

        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                /*
                for (int i = 0; i < 20; i++) {
                    CrossBorderHomeItem item = new CrossBorderHomeItem();
                    item.itemType = Constant.ITEM_TYPE_NORMAL;
                    crossBorderHomeItemList.add(item);
                }

                 */

                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    int index = 0;
                    GoodsSearchItemPair goodsPair = null;
                    EasyJSONArray goodsArray = responseObj.getSafeArray("datas.goodsList");
                    for (Object object : goodsArray) {
                        EasyJSONObject goodsObject = (EasyJSONObject) object;

                        GoodsSearchItem goods = new GoodsSearchItem(Constant.ITEM_TYPE_NORMAL);
                        goods.commonId = goodsObject.optInt("commonId");
                        goods.imageSrc = goodsObject.optString("imageName");
                        goods.goodsName = goodsObject.optString("goodsName");
                        goods.jingle = goodsObject.optString("jingle");
                        goods.price = Util.getSpuPrice(goodsObject);

                        if (index % 2 == 0) {
                            CrossBorderHomeItem item = new CrossBorderHomeItem();
                            goodsPair = new GoodsSearchItemPair(Constant.ITEM_TYPE_NORMAL);

                            item.itemType = Constant.ITEM_TYPE_NORMAL;
                            item.goodsPair = goodsPair;
                            crossBorderHomeItemList.add(item);

                            goodsPair.left = goods;
                        } else {
                            goodsPair.right = goods;
                        }

                        ++index;
                    }

                    adapter.setNewData(crossBorderHomeItemList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
