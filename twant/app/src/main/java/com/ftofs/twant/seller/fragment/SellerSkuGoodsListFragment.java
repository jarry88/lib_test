package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.seller.adapter.SellerSkuListAdapter;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;

import java.util.List;

public class SellerSkuGoodsListFragment extends BaseFragment {
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
                if (id == R.id.btn_edit) {

                }
            }
        });
        rvList.setAdapter(adapter);
    }
}
