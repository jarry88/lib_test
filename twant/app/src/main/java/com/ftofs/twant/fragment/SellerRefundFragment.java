package com.ftofs.twant.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.seller.entity.SellerOrderFilterParams;
import com.ftofs.twant.seller.entity.SellerOrderRefundItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.fragment.SellerOrderInfoFragment;
import com.ftofs.twant.seller.fragment.SellerOrderListPageFragment;
import com.ftofs.twant.seller.fragment.SellerRefundDetailFragment;
import com.ftofs.twant.seller.widget.SellerOrderFilterDrawerPopupView;
import com.ftofs.twant.tangram.SloganView;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.google.android.material.tabs.TabLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.ftofs.twant.constant.PopupType;
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
public class SellerRefundFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, SimpleCallback {
    @BindView(R.id.vp_page_list)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    private SellerOrderFilterDrawerPopupView sellerOrderFilterDrawerPopupView;

    @OnClick(R.id.btn_filter)
    void showSearchOrderInfo() {

        if (sellerOrderFilterDrawerPopupView == null) {
            sellerOrderFilterDrawerPopupView = (SellerOrderFilterDrawerPopupView) new XPopup.Builder(_mActivity)
                    //右边
                    .popupPosition(PopupPosition.Right)
                    //启用状态栏阴影
                    .hasStatusBarShadow(true)
                    .asCustom(new SellerOrderFilterDrawerPopupView(_mActivity, PopupType.SELLER_REFUND_FILTER,this));
        }
        sellerOrderFilterDrawerPopupView.show();
    }

    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
    }
    private List<View> mViews;
    private PagerAdapter mPagerAdapter;
    private SellerReturnAdapter sellerReturnAdapter;
    private List<SellerOrderRefundItem> returnItemList;
    private SellerReturnAdapter sellerRefundAdapter;
    private List<SellerOrderRefundItem> refundItemList;
    private boolean hasMore;

    private int currPage;
    SellerOrderFilterParams filterParams; // 搜索過濾參數


    public static SellerRefundFragment newInstance() {
        SellerRefundFragment fragment = new SellerRefundFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
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
    }

    private void loadData(int page) {

        try{
            EasyJSONObject params =EasyJSONObject.generate("token", User.getToken(),"page",page);
            if (filterParams != null) {
                if (!StringUtil.isEmpty(filterParams.buyerNickname)) {
                    params.set("searchName", filterParams.buyerNickname);
                }

                if (!StringUtil.isEmpty(filterParams.goodsName)) {
                    params.set("goodsName", filterParams.goodsName);
                }

                if (!StringUtil.isEmpty(filterParams.orderSN)) {
                    params.set("ordersSn", filterParams.orderSN);
                }
                if (!StringUtil.isEmpty(filterParams.refundSN)) {
                    params.set("refundSN", filterParams.refundSN);
                }

                params.set("createTimeStart", filterParams.beginDate.toString() +" 00:00:00");
//                params.set("addTimeStart", filterParams.beginDate.toString()+" 00:00:00");
                params.set("createTimeEnd", filterParams.endDate.toString()+" 23:59:59");
//                params.set("addTimeEnd", filterParams.endDate.toString()+" 01:01:01");
                params.set("searchType", filterParams.searchType+" 01:01:01");

            }
            String url=tabLayout.getSelectedTabPosition()==0?Api.PATH_SELLER_REFUND_LIST:Api.PATH_SELLER_RETURN_LIST;
            SLog.info("url[%s], params[%s]", url, params.toString());

            Api.getUI(url, params, new UICallback() {
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
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void updateRefundView(EasyJSONObject responseObj) throws Exception {
        EasyJSONArray refundList = responseObj.getArray("datas.refundList");
        if (currPage == 0) {
            refundItemList.clear();
        }
        for (Object object : refundList) {
            EasyJSONObject item = (EasyJSONObject) object;
            SellerOrderRefundItem orderItem = SellerOrderRefundItem.parse(item);
            refundItemList.add(orderItem);
        }
        sellerRefundAdapter.setNewData(refundItemList);
        EasyJSONObject pageEntity = responseObj.getObject("datas.pageEntity");


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
        EasyJSONArray returnList = responseObj.getArray("datas.refundList");
        if (currPage == 0) {
            returnItemList.clear();
        }
        for (Object object : returnList) {
            returnItemList.add(SellerOrderRefundItem.parse((EasyJSONObject) object));
            SLog.info(" handle [%s]", returnItemList.get(0).getShowSellerHandle());
        }

        sellerReturnAdapter.setNewData(returnItemList);
        EasyJSONObject pageEntity = responseObj.getObject("datas.pageEntity");


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

        tabRefund.setCustomView(tabRefundView);
        tabReTurn.setCustomView(R.layout.tab_red_count_item);
        ((TextView)(tabRefund.getCustomView().findViewById(R.id.text1))).setText(getResources().getString(R.string.text_refund_tab));
        ((TextView)(tabReTurn.getCustomView().findViewById(R.id.text1))).setText(getResources().getString(R.string.text_refund_return_tab));
        tabLayout.setTabTextColors(Color.parseColor("#00B0FF"),Color.parseColor("#2A292A"));
        tabLayout.addTab(tabRefund);
        tabLayout.addTab(tabReTurn);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                reloadDataWithFilter(null);
                viewPager.setCurrentItem(tab.getPosition());
                ((TextView)(tab.getCustomView().findViewById(R.id.text1))).setTextColor(getResources().getColor(R.color.tw_blue));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView)(tab.getCustomView().findViewById(R.id.text1))).setTextColor(getResources().getColor(R.color.tw_black));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onSupportVisible() {
        currPage=0;
        loadData(currPage+1);

    }

    private void initView(View view) {
        mViews = new ArrayList<>();
        returnItemList = new ArrayList<>();
        refundItemList = new ArrayList<>();
        sellerReturnAdapter = new SellerReturnAdapter(R.layout.inflate_seller_refund_item,returnItemList);
        sellerRefundAdapter = new SellerReturnAdapter(R.layout.inflate_seller_refund_item,refundItemList);
        sellerRefundAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id =view.getId();
                SellerOrderRefundItem item = refundItemList.get(position);
                SLog.info(item.toString());
                if (id == R.id.btn_seller_refund_state) {
                    handleRefundItem(item);
                } else if (id == R.id.tv_refund_goods) {
                    Util.startFragment(SellerOrderInfoFragment.newInstance(item.getRefundId(),false));
                } else if (id == R.id.tv_orders_sn) {
                    Util.startFragment(SellerRefundDetailFragment.newInstance(item.getRefundId()));
                }
            }
        });
        sellerReturnAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id =view.getId();
                SellerOrderRefundItem item = returnItemList.get(position);

                if (id == R.id.btn_seller_refund_state) {
                    handleRefundItem(item);//0是指非refund類型
                } else if (id == R.id.tv_refund_goods) {
                    Util.startFragment(SellerOrderInfoFragment.newInstance(item.getRefundId(),true));

                } else if (id == R.id.tv_orders_sn) {
                    Util.startFragment(SellerRefundDetailFragment.newInstance(item.getRefundId(), Constant.SELLER_RETURN));
                }
            }
        });
//        tabLayout.setupWithViewPager(viewPager);

        RecyclerView recyclerRefundView = new RecyclerView(_mActivity);
        recyclerRefundView.setLayoutManager(new LinearLayoutManager(_mActivity));

        RecyclerView recyclerReturnView = new RecyclerView(_mActivity);
        recyclerReturnView.setLayoutManager(new LinearLayoutManager(_mActivity));
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
                ((TextView)(tabLayout.getTabAt(position).getCustomView().findViewById(R.id.text1))).setTextColor(getResources().getColor(R.color.tw_blue));
                ((TextView)(tabLayout.getTabAt(1-position).getCustomView().findViewById(R.id.text1))).setTextColor(getResources().getColor(R.color.tw_black));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(mPagerAdapter);
    }

    private void handleRefundItem(SellerOrderRefundItem item) {
        SLog.info("handle%s,sn[%s]",item.getShowSellerHandle(),item.toString());
        if (item.getShowSellerHandle() == SellerOrderRefundItem.RECEIVE_GOOD_HANDLE) {
            EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(), "refundId", item.getRefundId());
            SLog.info("params[%s]", params);
            Api.postUI(Api.PATH_SELLER_RETURN_RECEIVE_SAVE, params, new UICallback() {
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
                        ToastUtil.success(_mActivity, responseObj.getSafeString("datas.success"));

                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } else {
            if (viewPager.getCurrentItem() == 0) {

                Util.startFragment(SellerRefundDetailFragment.newInstance(item.getRefundId()));
            } else {
                Util.startFragment(SellerRefundDetailFragment.newInstance(item.getRefundId(),Constant.SELLER_RETURN));
            }
        }
//                    ToastUtil.success(_mActivity,"跳轉訂單詳情頁");
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

    @Override
    public void onSimpleCall(Object data) {

        SellerOrderFilterParams filterParams = (SellerOrderFilterParams) data;
        SLog.info("filterParams[%s]", filterParams.toString());
        reloadDataWithFilter(filterParams);
    }

    private void reloadDataWithFilter(SellerOrderFilterParams filterParams) {

        this.filterParams = filterParams;

        currPage = 0;
        loadData(currPage + 1);
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
