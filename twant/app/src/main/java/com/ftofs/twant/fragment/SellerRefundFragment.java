package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SimpleViewPagerAdapter;
import com.ftofs.twant.log.SLog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SellerRefundFragment extends BaseFragment  {
    @BindView(R.id.vp_page_list)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private List<View> mViews;
    private PagerAdapter mPagerAdapter;

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
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tabRefund = tabLayout.newTab();
        TabLayout.Tab tabReTurn = tabLayout.newTab();
//        View tabRefundView =LayoutInflater.from(getContext()).inflate(R.layout.tab_red_count_item,null,false);
//        ((TextView)tabRefundView.findViewById(R.id.tv_tab_name)).setText("1");
//        tabRefund.setCustomView(tabRefundView);
//        tabReTurn.setCustomView(R.layout.tab_red_count_item);
//        tabRefund.setText(getResources().getString(R.string.text_refund_tab));
//        tabReTurn.setText(getResources().getString(R.string.text_refund_return_tab));
        tabLayout.setTabTextColors(Color.parseColor("#2A292A"),Color.parseColor("#992A292A"));
//        tabLayout.addTab(tabRefund);
        tabLayout.addTab(tabLayout.newTab().setText("第一個"));
        tabLayout.addTab(tabLayout.newTab().setText("第二個"));
//        tabLayout.addTab(tabReTurn);
        RecyclerView recyclerRefundView = new RecyclerView(_mActivity);
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerRefundView.setLayoutParams(layoutParams);

        recyclerRefundView.setBackgroundColor(Color.BLUE);
        RecyclerView recyclerReturnView = new RecyclerView(_mActivity);
        recyclerReturnView.setBackgroundColor(Color.WHITE);
        mViews.add(recyclerRefundView);
        mViews.add(recyclerReturnView);
        mPagerAdapter = new SimpleViewPagerAdapter(mViews);
        viewPager.setAdapter(mPagerAdapter);
    }


}
