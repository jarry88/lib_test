package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.entity.CustomActionData;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecMapItem;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class SellerSkuEditorFragment extends BaseFragment implements View.OnClickListener, SimpleCallback {
    List<SellerSpecMapItem> sellerSelectedSpecList;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    List<SellerSpecPermutation> permutationList = new ArrayList<>();


    public static SellerSkuEditorFragment newInstance(List<SellerSpecMapItem> sellerSelectedSpecList) {
        Bundle args = new Bundle();

        SellerSkuEditorFragment fragment = new SellerSkuEditorFragment();
        fragment.setArguments(args);
        fragment.sellerSelectedSpecList = sellerSelectedSpecList;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_sku_editor, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);


        int TOTAL_SKU_COUNT = 1;

        for (SellerSpecMapItem item : sellerSelectedSpecList) {
            TOTAL_SKU_COUNT *= item.sellerSpecItemList.size();
        }
        SLog.info("TOTAL_SKU_COUNT[%d]", TOTAL_SKU_COUNT);

        for (int i = 0; i < TOTAL_SKU_COUNT; i++) {
            int n = i;
            StringBuilder skuDesc = new StringBuilder();  // SKU規格描述
            SellerSpecPermutation permutation = new SellerSpecPermutation();

            for (int j = 0; j < sellerSelectedSpecList.size(); j++) {
                SellerSpecMapItem sellerSpecMapItem = sellerSelectedSpecList.get(j);

                int index = n % sellerSpecMapItem.sellerSpecItemList.size();
                SellerSpecItem specItem = sellerSpecMapItem.sellerSpecItemList.get(index);

                permutation.sellerSpecItemList.add(specItem);

                skuDesc.append(sellerSpecMapItem.sellerSpecItemList.get(index).name).append("/");

                n /= sellerSpecMapItem.sellerSpecItemList.size();
            }

            SLog.info("skuDesc[%s]", skuDesc.toString());
            permutation.specValueString = StringUtil.trim(skuDesc.toString(), new char[] {'/'});
            permutationList.add(permutation);
        }

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.viewpager);

        titleList.add("SKU商品");
        titleList.add("SKU圖片");
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));

        fragmentList.add(SellerSkuGoodsListFragment.newInstance(permutationList));
        fragmentList.add(SellerSkuImageListFragment.newInstance(sellerSelectedSpecList.get(0).sellerSpecItemList, this));

        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public void onSimpleCall(Object data) {
        if (data instanceof CustomActionData) {
            CustomActionData customActionData = (CustomActionData) data;

            if (CustomAction.CUSTOM_ACTION_SELLER_UPLOAD_SPEC_IMAGE_FINISH.equals(customActionData.data)) {
                Bundle bundle = new Bundle();
                bundle.putString("data", customActionData.toString());

                setFragmentResult(RESULT_OK, bundle);
            }
        }
    }
}
