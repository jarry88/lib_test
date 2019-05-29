package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.AddrListAdapter;
import com.ftofs.twant.adapter.StoreLabelListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.domain.store.StoreLabel;
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


/**
 * 店鋪分類Fragment
 * @author zwm
 */
public class ShopCategoryFragment extends BaseFragment implements View.OnClickListener {
    ShopMainFragment parentFragment;

    List<StoreLabel> shopStoreLabelList = new ArrayList<>();
    BaseQuickAdapter adapter;
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

        RecyclerView rvOuterList = view.findViewById(R.id.rv_outer_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rvOuterList.setLayoutManager(layoutManager);
        adapter = new StoreLabelListAdapter(R.layout.store_label_outer_item, shopStoreLabelList);
        rvOuterList.setAdapter(adapter);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        ((SupportFragment) getParentFragment()).pop();
        return true;
    }

    private void loadShopCategoryData() {
        EasyJSONObject params = EasyJSONObject.generate(
                "storeId", parentFragment.getShopId()
        );
        Api.getUI(Api.PATH_STORE_CATEGORY, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONArray storeCategoryList = responseObj.getArray("datas.storeCategoryList");
                    for (Object object : storeCategoryList) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        StoreLabel storeLabel = new StoreLabel();
                        storeLabel.setStoreLabelId(easyJSONObject.getInt("storeLabelId"));
                        storeLabel.setStoreLabelName(easyJSONObject.getString("storeLabelName"));
                        storeLabel.setParentId(easyJSONObject.getInt("parentId"));
                        storeLabel.setStoreId(easyJSONObject.getInt("storeId"));

                        EasyJSONArray storeLabelList = easyJSONObject.getArray("storeLabelList");
                        if (storeLabelList != null && storeLabelList.length() > 0) {
                            List<StoreLabel> storeLabels = new ArrayList<>();
                            for (Object object2 : storeLabelList) {
                                EasyJSONObject easyJSONObject2 = (EasyJSONObject) object2;
                                StoreLabel storeLabel2 = new StoreLabel();
                                storeLabel2.setStoreLabelId(easyJSONObject2.getInt("storeLabelId"));
                                storeLabel2.setStoreLabelName(easyJSONObject2.getString("storeLabelName"));
                                storeLabel2.setParentId(easyJSONObject2.getInt("parentId"));
                                storeLabel2.setStoreId(easyJSONObject2.getInt("storeId"));

                                storeLabels.add(storeLabel2);
                            }

                            storeLabel.setStoreLabelList(storeLabels);
                        }

                        shopStoreLabelList.add(storeLabel);
                    }

                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
