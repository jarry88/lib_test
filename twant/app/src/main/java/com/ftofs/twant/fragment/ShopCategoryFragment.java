package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreLabelListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.lib_net.model.StoreLabel;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;


/**
 * 商店分類Fragment
 * @author zwm
 */
public class ShopCategoryFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    ShopMainFragment parentFragment;

    List<StoreLabel> shopStoreLabelList = new ArrayList<>();
    StoreLabelListAdapter adapter;

    String responseStr;
    public static ShopCategoryFragment newInstance() {
        Bundle args = new Bundle();

        ShopCategoryFragment fragment = new ShopCategoryFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (ShopMainFragment) getParentFragment();

        Util.setOnClickListener(view, R.id.btn_search_goods, this);
        Util.setOnClickListener(view, R.id.btn_all_goods, this);

        RecyclerView rvOuterList = view.findViewById(R.id.rv_outer_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvOuterList.setLayoutManager(layoutManager);
        adapter = new StoreLabelListAdapter(_mActivity, R.layout.store_label_outer_item, shopStoreLabelList, this);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                StoreLabel storeLabel = shopStoreLabelList.get(position);

                int id = storeLabel.getStoreLabelId();
                String name = storeLabel.getStoreLabelName();
                SLog.info("id[%d], name[%s]", id, name);
                Util.startFragment(ShopCommodityFragment.newInstance(true, EasyJSONObject.generate(
                        "storeId", parentFragment.getStoreId(),
                        "labelId", id
                ).toString()));
            }
        });
        rvOuterList.setAdapter(adapter);

        loadShopCategoryData();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_search_goods:
                Util.startFragment(ShopSearchFragment.newInstance(parentFragment.getStoreId(), responseStr));
                break;
            case R.id.btn_all_goods:
                Util.startFragment(ShopCommodityFragment.newInstance(true, EasyJSONObject.generate("storeId", parentFragment.getStoreId()).toString()));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        ((SupportFragment) getParentFragment()).pop();
        return true;
    }

    private void loadShopCategoryData() {
        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", parentFragment.getStoreId()
        );
        Api.getUI(Api.PATH_STORE_CATEGORY, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                ShopCategoryFragment.this.responseStr = responseStr;

                try {
                    EasyJSONArray storeCategoryList = responseObj.getSafeArray("datas.storeCategoryList");
                    for (Object object : storeCategoryList) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        StoreLabel storeLabel = new StoreLabel();
                        storeLabel.setStoreLabelId(easyJSONObject.getInt("storeLabelId"));
                        storeLabel.setStoreLabelName(easyJSONObject.getSafeString("storeLabelName"));
                        storeLabel.setParentId(easyJSONObject.getInt("parentId"));
                        storeLabel.setStoreId(easyJSONObject.getInt("storeId"));
                        storeLabel.setIsFold(Constant.ONE);

                        EasyJSONArray storeLabelList = easyJSONObject.getSafeArray("storeLabelList");
                        if (storeLabelList != null && storeLabelList.length() > 0) {
                            List<StoreLabel> storeLabels = new ArrayList<>();
                            for (Object object2 : storeLabelList) {
                                EasyJSONObject easyJSONObject2 = (EasyJSONObject) object2;
                                StoreLabel storeLabel2 = new StoreLabel();
                                storeLabel2.setStoreLabelId(easyJSONObject2.getInt("storeLabelId"));
                                storeLabel2.setStoreLabelName(easyJSONObject2.getSafeString("storeLabelName"));
                                storeLabel2.setParentId(easyJSONObject2.getInt("parentId"));
                                storeLabel2.setStoreId(easyJSONObject2.getInt("storeId"));

                                storeLabels.add(storeLabel2);
                            }

                            storeLabel.setStoreLabelList(storeLabels);
                        }

                        if (storeLabel.getStoreLabelList() == null) {
                            storeLabel.setStoreLabelList(new ArrayList<StoreLabel>());
                        }

                        shopStoreLabelList.add(storeLabel);
                    }

                    adapter.setNewData(shopStoreLabelList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("storeLabelId[%d]", id);

        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", parentFragment.getStoreId(),
                "labelId", id);
        Util.startFragment(ShopCommodityFragment.newInstance(true, params.toString()));
    }
}
