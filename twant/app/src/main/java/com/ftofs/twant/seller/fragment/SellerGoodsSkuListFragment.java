package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.seller.adapter.SellerGoodsSkuListAdapter;
import com.ftofs.twant.seller.entity.SellerSkuListItem;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SellerGoodsSkuListFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvList;
    List<SellerSkuListItem> skuList = new ArrayList<>();

    int commonId;
    SellerGoodsSkuListAdapter adapter;

    public static SellerGoodsSkuListFragment newInstance(int commonId) {
        Bundle args = new Bundle();

        SellerGoodsSkuListFragment fragment = new SellerGoodsSkuListFragment();
        fragment.setArguments(args);
        fragment.commonId = commonId;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_sku_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new SellerGoodsSkuListAdapter(_mActivity, R.layout.seller_goods_sku_list_item, skuList);
        rvList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_GOODS_SKU_LIST;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "commonId", commonId);

        SLog.info("params[%s]", params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                    return;
                }

                try {
                    EasyJSONArray skuListArr = responseObj.getSafeArray("datas.skuList");

                    for (Object object : skuListArr) {
                        SellerSkuListItem sellerSkuListItem = new SellerSkuListItem();
                        EasyJSONObject item = (EasyJSONObject) object;

                        sellerSkuListItem.goodsId = item.getInt("goodsId");
                        sellerSkuListItem.commonId = commonId;
                        sellerSkuListItem.goodsFullSpecs = item.getSafeString("goodsFullSpecs");
                        sellerSkuListItem.goodsPrice = item.getDouble("goodsPrice0");
                        sellerSkuListItem.goodsStorage = item.getInt("goodsStorage");
                        sellerSkuListItem.goodsImage = item.getSafeString("imageSrc");

                        skuList.add(sellerSkuListItem);
                    }

                    adapter.setNewData(skuList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
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



