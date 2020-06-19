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
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.adapter.SellerSpecListAdapter;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecListItem;
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

/**
 * 【賣家】規格組列表
 * @author zwm
 */
public class SellerSpecFragment extends BaseFragment implements View.OnClickListener {
    List<SellerSpecListItem> sellerSpecList = new ArrayList<>();
    SellerSpecListAdapter adapter;

    public static SellerSpecFragment newInstance() {
        Bundle args = new Bundle();

        SellerSpecFragment fragment = new SellerSpecFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_spec, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_add_spec, this);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new SellerSpecListAdapter(_mActivity, R.layout.seller_spec_list_item, sellerSpecList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                SellerSpecListItem item = sellerSpecList.get(position);

                if (id == R.id.btn_delete) {

                } else if (id == R.id.btn_edit) {
                    start(SellerEditSpecFragment.newInstance(Constant.ACTION_EDIT, item));
                }
            }
        });
        rvList.setAdapter(adapter);

        loadSellerSpecList();
    }

    private void loadSellerSpecList() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        SLog.info("url[%s], params[%s]", Api.PATH_SELLER_SPEC_LIST, params);
        Api.postUI(Api.PATH_SELLER_SPEC_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }

                    EasyJSONArray specAndValueList = responseObj.getSafeArray("datas.specAndValueList");
                    for (Object object : specAndValueList) {
                        EasyJSONObject specObj = (EasyJSONObject) object;

                        int storeId = specObj.getInt("storeId");
                        int specId = specObj.getInt("specId");
                        String specName = specObj.getSafeString("specName");


                        SellerSpecListItem sellerSpecListItem = new SellerSpecListItem(
                                storeId == Constant.FALSE_INT ? SellerSpecListItem.SPEC_TYPE_SYSTEM : SellerSpecListItem.SPEC_TYPE_CUSTOM,
                                specId, specName
                        );


                        EasyJSONArray specValueList = specObj.getSafeArray("specValueList");
                        for (Object object2 : specValueList) {
                            EasyJSONObject specValueObj = (EasyJSONObject) object2;
                            int specValueId = specValueObj.getInt("specValueId");
                            String specValueName = specValueObj.getSafeString("specValueName");

                            SellerSpecItem sellerSpecItem = new SellerSpecItem();
                            sellerSpecItem.type = SellerSpecItem.TYPE_SPEC_VALUE;
                            sellerSpecItem.id = specValueId;
                            sellerSpecItem.name = specValueName;

                            sellerSpecListItem.sellerSpecItemList.add(sellerSpecItem);
                        }

                        sellerSpecList.add(sellerSpecListItem);
                    }

                    adapter.setNewData(sellerSpecList);
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
        } else if (id == R.id.btn_add_spec) {
            start(SellerEditSpecFragment.newInstance(Constant.ACTION_ADD, null));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}






