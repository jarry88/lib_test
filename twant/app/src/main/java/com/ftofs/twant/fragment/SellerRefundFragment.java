package com.ftofs.twant.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SellerReturnAdapter;
import com.ftofs.twant.adapter.SimpleViewPagerAdapter;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.view.RefundViewHolder;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 商家退单列表页
 * @author gzp
 */
public class SellerRefundFragment extends BaseFragment  {
    @BindView(R.id.vp_page_list)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private List<View> mViews;
    private PagerAdapter mPagerAdapter;
    private SellerReturnAdapter sellerReturnAdapter;
    private List<OrderItem> returnItemList;

    public static SellerRefundFragment newInstance() {
        SellerRefundFragment fragment = new SellerRefundFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_return_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);

    }

    private void initview(View view) {
        SLog.info("進入此頁面");
        mViews = new ArrayList<>();
//        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tabRefund = tabLayout.newTab();
        TabLayout.Tab tabReTurn = tabLayout.newTab();
        View tabRefundView =LayoutInflater.from(getContext()).inflate(R.layout.tab_red_count_item,null,false);
        ((TextView)tabRefundView.findViewById(R.id.tv_tab_name)).setText("1");
        tabRefund.setCustomView(tabRefundView);
        tabReTurn.setCustomView(R.layout.tab_red_count_item);
        tabRefund.setText(getResources().getString(R.string.text_refund_tab));
        tabReTurn.setText(getResources().getString(R.string.text_refund_return_tab));
        tabLayout.setTabTextColors(Color.parseColor("#2A292A"),Color.parseColor("#992A292A"));
        tabLayout.addTab(tabRefund);
        tabLayout.addTab(tabReTurn);
        RecyclerView recyclerRefundView = new RecyclerView(_mActivity);
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerRefundView.setLayoutParams(layoutParams);
        recyclerRefundView.setBackgroundColor(Color.BLUE);


        ViewGroup.MarginLayoutParams layoutParams1 = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        RecyclerView recyclerReturnView = new RecyclerView(_mActivity);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(_mActivity);
        recyclerReturnView.setLayoutManager(linearLayoutManager);
        recyclerReturnView.setLayoutParams(layoutParams1);
        recyclerReturnView.setBackgroundColor(Color.BLUE);
        returnItemList = new ArrayList<>();
        returnItemList.add(null);
        returnItemList.add(null);
        returnItemList.add(null);
        returnItemList.add(null);
        returnItemList.add(null);
        returnItemList.add(null);
        returnItemList.add(null);
        returnItemList.add(null);
        sellerReturnAdapter = new SellerReturnAdapter(R.layout.inflate_seller_refund_item,returnItemList);
        recyclerReturnView.setAdapter(sellerReturnAdapter);
        mViews.add(recyclerReturnView);
        Context context;
        TextView textView =new TextView(_mActivity);
        textView.setText("sdfsdf");
        mViews.add(recyclerReturnView);
        mViews.add(textView);


        LinearLayout llContainer = view.findViewById(R.id.ll_container);
        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        llContainer.addView(recyclerReturnView, params);

        mPagerAdapter = new SimpleViewPagerAdapter(_mActivity,mViews);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0f, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(mPagerAdapter);
    }


}
