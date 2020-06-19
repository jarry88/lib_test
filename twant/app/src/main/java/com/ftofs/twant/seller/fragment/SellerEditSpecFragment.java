package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.adapter.SellerSpecValueListAdapter;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecListItem;
import com.ftofs.twant.seller.entity.SellerSpecValueListItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * 添加或編輯規格組
 * @author zwm
 */
public class SellerEditSpecFragment extends BaseFragment implements View.OnClickListener {
    int action;

    RecyclerView rvList;
    SellerSpecValueListAdapter adapter;
    EditText etSpecName;

    int specId;
    String specName;
    List<SellerSpecValueListItem> specValueList = new ArrayList<>();

    public static SellerEditSpecFragment newInstance(int action, SellerSpecListItem sellerSpecListItem) {
        Bundle args = new Bundle();

        SellerEditSpecFragment fragment = new SellerEditSpecFragment();
        fragment.setArguments(args);
        fragment.action = action;
        if (action == Constant.ACTION_EDIT) {
            fragment.specId = sellerSpecListItem.specId;
            fragment.specName = sellerSpecListItem.specName;

            SLog.info("sellerSpecItemList[%d]", sellerSpecListItem.sellerSpecItemList.size());
            for (SellerSpecItem sellerSpecItem : sellerSpecListItem.sellerSpecItemList) {
                fragment.specValueList.add(new SellerSpecValueListItem(
                        SellerSpecValueListItem.ITEM_TYPE_NORMAL, sellerSpecItem.id, sellerSpecItem.name));
            }
        }

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_edit_spec, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);

        TextView tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        tvFragmentTitle.setText(action == Constant.ACTION_ADD ? "添加規格" : "編輯規格");

        // 底部的【添加規格】按鈕
        specValueList.add(new SellerSpecValueListItem(SellerSpecValueListItem.ITEM_TYPE_FOOTER, 0, null));

        etSpecName = view.findViewById(R.id.et_spec_name);
        if (!StringUtil.isEmpty(specName)) {
            etSpecName.setText(specName);
        }

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new SellerSpecValueListAdapter(specValueList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                if (id == R.id.btn_remove) { // 刪除規格值
                    specValueList.remove(position);
                    adapter.notifyItemRemoved(position);
                } else if (id == R.id.btn_add_spec_value) { // 添加規格值
                    int targetIndex = specValueList.size() - 1;
                    specValueList.add(targetIndex, new SellerSpecValueListItem(SellerSpecValueListItem.ITEM_TYPE_NORMAL, 0, ""));
                    adapter.notifyItemInserted(targetIndex);
                }
            }
        });
        rvList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {

        }
    }
}






