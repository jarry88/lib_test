package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabButton;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.UnscrollableViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

public class SellerGoodsListFragment extends BaseFragment implements View.OnClickListener, SimpleCallback {
    SimpleTabButton[] tabButtons;

    UnscrollableViewPager viewPager;
    List<Fragment> fragmentList = new ArrayList<>();
    public static final int TAB_GOODS_IN_SALE = 0; // 出售中的商品
    public static final int TAB_GOODS_IN_STOCK = 1; // 倉庫中的商品

    int currTab;
    public static final int TAB_COUNT = 2;

    public static SellerGoodsListFragment newInstance() {
        return newInstance(TAB_GOODS_IN_SALE);
    }

    public static SellerGoodsListFragment newInstance(int tab) {
        Bundle args = new Bundle();

        SellerGoodsListFragment fragment = new SellerGoodsListFragment();
        fragment.setArguments(args);
        fragment.currTab = tab;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        tabButtons = new SimpleTabButton[TAB_COUNT];

        SimpleTabManager tabManager = new SimpleTabManager(TAB_GOODS_IN_SALE) {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                boolean isRepeat = onSelect(v);
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }

                if (id == R.id.tab_goods_in_sale) {
                    currTab = TAB_GOODS_IN_SALE;
                } else if (id == R.id.tab_goods_in_stock) {
                    currTab = TAB_GOODS_IN_STOCK;
                }

                SLog.info("currTab[%d]", currTab);
                viewPager.setCurrentItem(currTab);
            }
        };

        tabManager.add(view.findViewById(R.id.tab_goods_in_sale));
        tabManager.add(view.findViewById(R.id.tab_goods_in_stock));

        viewPager = view.findViewById(R.id.vp_page_list);
        viewPager.setCanScroll(false);
        viewPager.setOffscreenPageLimit(TAB_COUNT - 1);

        if (currTab != TAB_GOODS_IN_SALE) { // 如果不是默認的Tab, 選中對應的Tab
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tabManager.performClick(currTab);
                }
            }, 500);
        }

        List<String> titleList = new ArrayList<>();
        titleList.add("出售中的商品");
        titleList.add("倉庫中的商品");

        fragmentList.add(SellerGoodsListPageFragment.newInstance(TAB_GOODS_IN_SALE, this));
        fragmentList.add(SellerGoodsListPageFragment.newInstance(TAB_GOODS_IN_STOCK, this));

        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_filter) {

        }
    }

    @Override
    public void onSimpleCall(Object data) {
        EasyJSONObject dataObj = (EasyJSONObject) data;

        try {
            String action = dataObj.getSafeString("action");
            if (CustomAction.CUSTOM_ACTION_RELOAD_DATA.equals(action)) {
                for (Fragment fragment : fragmentList) {
                    SellerGoodsListPageFragment sellerGoodsListPageFragment = (SellerGoodsListPageFragment) fragment;
                    sellerGoodsListPageFragment.reloadData();
                }
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
}

