package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SellerReturnAdapter;
import com.ftofs.twant.adapter.SimpleViewPagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.seller.entity.SellerOrderRefundItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商家退单列表页
 * @author gzp
 */
public class SellerRefundFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.vp_page_list)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private List<View> mViews;
    private PagerAdapter mPagerAdapter;
    private SellerReturnAdapter sellerReturnAdapter;
    private List<SellerOrderRefundItem> returnItemList;
    private SellerReturnAdapter sellerRefundAdapter;
    private List<SellerOrderRefundItem> refundItemList;
    private boolean hasMore;
    private int currPage;


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
        initTabView(view);
        initView(view);

//        loadData(currPage+1);
    }

    private void loadData(int page) {
         EasyJSONObject params =EasyJSONObject.generate("token", User.getToken(),"page",page);
          SLog.info("params[%s]", params);
                 Api.getUI(Api.PATH_SELLER_REFUND_LIST, params, new UICallback() {
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
                             if (tabLayout.getSelectedTabPosition() == 0) {

                                 updateRefundView(responseObj);
                             } else {
                                 updateReturnView(responseObj);
                             }
                             currPage++;
                         } catch (Exception e) {
                             SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                         }
                     }
                 });
    }

    private void updateRefundView(EasyJSONObject responseObj) throws Exception {
        EasyJSONArray refundList = responseObj.getArray("datas.refundList");
        for (Object object : refundList) {
            EasyJSONObject item = (EasyJSONObject) object;
            SellerOrderRefundItem orderItem = SellerOrderRefundItem.parse(item);
            refundItemList.add(orderItem);
        }
        sellerRefundAdapter.setNewData(refundItemList);
        EasyJSONObject pageEntity = responseObj.getObject("datas.pageEntity");

        if (currPage == 0) {
            refundItemList.clear();
        }
        hasMore = pageEntity.getBoolean("hasMore");
        if (!hasMore) {
            sellerRefundAdapter.loadMoreEnd();
            sellerRefundAdapter.setEnableLoadMore(false);
        }
        if (!hasMore && refundItemList.size() > 1) {
            // 如果全部加載完畢，添加加載完畢的提示
//            loadingPopup.dismiss();
        }

    }
    private void updateReturnView(EasyJSONObject responseObj) throws Exception {
        EasyJSONArray returnList = responseObj.getArray("datas.returnList");
        for (Object object : returnList) {
            EasyJSONObject item = (EasyJSONObject) object;
            SellerOrderRefundItem orderItem = SellerOrderRefundItem.parse(item);
            refundItemList.add(orderItem);
        }
        sellerReturnAdapter.setNewData(returnItemList);
        EasyJSONObject pageEntity = responseObj.getObject("datas.pageEntity");

        if (currPage == 0) {
            returnItemList.clear();
        }
        hasMore = pageEntity.getBoolean("hasMore");
        if (!hasMore) {
            sellerReturnAdapter.loadMoreEnd();
            sellerReturnAdapter.setEnableLoadMore(false);
        }
        if (!hasMore && returnItemList.size() > 1) {
            // 如果全部加載完畢，添加加載完畢的提示
//            loadingPopup.dismiss();
        }

    }

    private void initTabView(View view) {
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currPage = 0;
                loadData(currPage+1);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void initView(View view) {
        mViews = new ArrayList<>();
        returnItemList = new ArrayList<>();
        refundItemList = new ArrayList<>();
        sellerReturnAdapter = new SellerReturnAdapter(R.layout.inflate_seller_refund_item,returnItemList);
        sellerRefundAdapter = new SellerReturnAdapter(R.layout.inflate_seller_refund_item,refundItemList);

//        tabLayout.setupWithViewPager(viewPager);

        RecyclerView recyclerRefundView = new RecyclerView(_mActivity);
        recyclerRefundView.setLayoutManager(new LinearLayoutManager(_mActivity));

        RecyclerView recyclerReturnView = new RecyclerView(_mActivity);
        recyclerReturnView.setLayoutManager(new LinearLayoutManager(_mActivity));

        returnItemList.add(null);
        returnItemList.add(null);
        refundItemList.add(null);
        refundItemList.add(null);
        sellerRefundAdapter.setNewData(refundItemList);
        sellerReturnAdapter.setNewData(returnItemList);
        sellerRefundAdapter.setEnableLoadMore(true);
        sellerRefundAdapter.setOnLoadMoreListener(this, recyclerRefundView);
        sellerReturnAdapter.setEnableLoadMore(true);
        sellerReturnAdapter.setOnLoadMoreListener(this, recyclerReturnView);

        recyclerReturnView.setAdapter(sellerReturnAdapter);
        recyclerRefundView.setAdapter(sellerRefundAdapter);
        mViews.add(recyclerRefundView);

        mViews.add(recyclerReturnView);

        mPagerAdapter = new SimpleViewPagerAdapter(_mActivity,mViews);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0f, true);
                currPage = 0;
                loadData(currPage+1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(mPagerAdapter);
    }
    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            sellerRefundAdapter.setEnableLoadMore(false);
            sellerReturnAdapter.setEnableLoadMore(false);
            return;
        }

        loadData(currPage+1);
    }

}
