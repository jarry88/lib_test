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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.entity.CustomActionData;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.adapter.SellerSkuListAdapter;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;
import com.ftofs.twant.seller.widget.SellerEditSkuInfoPopup;
import com.lxj.xpopup.XPopup;

import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

public class SellerSkuGoodsListFragment extends BaseFragment implements SimpleCallback {
    List<SellerSpecPermutation> sellerSpecPermutationList;
    RecyclerView rvList;
    SellerSkuListAdapter adapter;

    public static SellerSkuGoodsListFragment newInstance(List<SellerSpecPermutation> sellerSpecPermutationList) {
        Bundle args = new Bundle();

        SellerSkuGoodsListFragment fragment = new SellerSkuGoodsListFragment();
        fragment.setArguments(args);
        fragment.sellerSpecPermutationList = sellerSpecPermutationList;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_sku_goods_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new SellerSkuListAdapter(_mActivity, R.layout.seller_sku_list_item, sellerSpecPermutationList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                SellerSpecPermutation skuInfo = sellerSpecPermutationList.get(position);

                if (id == R.id.btn_edit) {
                    new XPopup.Builder(_mActivity)
                            .asCustom(new SellerEditSkuInfoPopup(_mActivity, position, skuInfo, SellerSkuGoodsListFragment.this))
                            .show();
                }
            }
        });
        rvList.setAdapter(adapter);
    }

    @Override
    public void onSimpleCall(Object data) {
        try {
            if (data instanceof CustomActionData) {
                CustomActionData customActionData = (CustomActionData) data;
                // 編輯彈窗保存時調用
                if (CustomAction.CUSTOM_ACTION_SELLER_EDIT_SKU_INFO.ordinal() == customActionData.action) {
                    EasyJSONObject dataObj = (EasyJSONObject) customActionData.data;

                    int position = dataObj.getInt("position");
                    SellerSpecPermutation permutation = sellerSpecPermutationList.get(position);

                    permutation.price = dataObj.getDouble("price");
                    permutation.storage = dataObj.getInt("stockStorage");
                    permutation.reserved = dataObj.getInt("reservedStorage");
                    permutation.goodsSN = dataObj.getSafeString("goodsSN");
                    adapter.notifyItemChanged(position);
                }
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public List<SellerSpecPermutation> collectSkuGoodsInfo() {
        return sellerSpecPermutationList;
    }
}
