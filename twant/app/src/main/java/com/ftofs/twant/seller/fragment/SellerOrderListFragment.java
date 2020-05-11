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
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.CouponListFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabButton;
import com.ftofs.twant.widget.SimpleTabManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家訂單列表頁面
 * @author zwm
 */
public class SellerOrderListFragment extends BaseFragment implements View.OnClickListener {
    SimpleTabButton[] tabButtons;

    ViewPager viewPager;

    int currTab;
    public static final int TAB_COUNT = Constant.ORDER_STATUS_CANCELLED + 1;

    public static SellerOrderListFragment newInstance() {
        return newInstance(Constant.ORDER_STATUS_ALL);
    }

    public static SellerOrderListFragment newInstance(int tab) {
        Bundle args = new Bundle();

        SellerOrderListFragment fragment = new SellerOrderListFragment();
        fragment.setArguments(args);
        fragment.currTab = tab;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_order_list, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        tabButtons = new SimpleTabButton[TAB_COUNT];

        tabButtons[Constant.ORDER_STATUS_ALL] = view.findViewById(R.id.tab_all);
        tabButtons[Constant.ORDER_STATUS_TO_BE_PAID] = view.findViewById(R.id.tab_to_be_paid);
        tabButtons[Constant.ORDER_STATUS_TO_BE_SHIPPED] = view.findViewById(R.id.tab_to_be_shipped);
        tabButtons[Constant.ORDER_STATUS_TO_BE_RECEIVED] = view.findViewById(R.id.tab_to_be_received);
        tabButtons[Constant.ORDER_STATUS_TO_BE_COMMENTED] = view.findViewById(R.id.tab_finished);
        tabButtons[Constant.ORDER_STATUS_CANCELLED] = view.findViewById(R.id.tab_cancelled);

        SimpleTabManager tabManager = new SimpleTabManager(Constant.ORDER_STATUS_ALL) {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                boolean isRepeat = onSelect(v);
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }

                if (id == R.id.tab_all) {
                    currTab = Constant.ORDER_STATUS_ALL;
                } else if (id == R.id.tab_to_be_paid) {
                    currTab = Constant.ORDER_STATUS_TO_BE_PAID;
                } else if (id == R.id.tab_to_be_shipped) {
                    currTab = Constant.ORDER_STATUS_TO_BE_SHIPPED;
                } else if (id == R.id.tab_to_be_received) {
                    currTab = Constant.ORDER_STATUS_TO_BE_RECEIVED;
                } else if (id == R.id.tab_finished) {
                    currTab = Constant.ORDER_STATUS_TO_BE_COMMENTED;
                } else if (id == R.id.tab_cancelled) {
                    currTab = Constant.ORDER_STATUS_CANCELLED;
                }
                
                SLog.info("currTab[%d]", currTab);
                viewPager.setCurrentItem(currTab);
            }
        };

        tabManager.add(view.findViewById(R.id.tab_all));
        tabManager.add(view.findViewById(R.id.tab_to_be_paid));
        tabManager.add(view.findViewById(R.id.tab_to_be_shipped));
        tabManager.add(view.findViewById(R.id.tab_to_be_received));
        tabManager.add(view.findViewById(R.id.tab_finished));
        tabManager.add(view.findViewById(R.id.tab_cancelled));


        viewPager = view.findViewById(R.id.vp_page_list);
        viewPager.setOffscreenPageLimit(TAB_COUNT - 1);

        if (currTab != Constant.ORDER_STATUS_ALL) { // 如果不是默認的Tab, 選中對應的Tab
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tabManager.performClick(currTab);
                }
            }, 500);
        }

        List<String> titleList = new ArrayList<>();
        titleList.add("全部");
        titleList.add("待付款");
        titleList.add("待發貨");
        titleList.add("已發貨");
        titleList.add("已完成");
        titleList.add("已取消");


        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(SellerOrderListPageFragment.newInstance(Constant.ORDER_STATUS_ALL));
        fragmentList.add(SellerOrderListPageFragment.newInstance(Constant.ORDER_STATUS_TO_BE_PAID));
        fragmentList.add(SellerOrderListPageFragment.newInstance(Constant.ORDER_STATUS_TO_BE_SHIPPED));
        fragmentList.add(SellerOrderListPageFragment.newInstance(Constant.ORDER_STATUS_TO_BE_RECEIVED));
        fragmentList.add(SellerOrderListPageFragment.newInstance(Constant.ORDER_STATUS_TO_BE_COMMENTED));
        fragmentList.add(SellerOrderListPageFragment.newInstance(Constant.ORDER_STATUS_CANCELLED));

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
}

