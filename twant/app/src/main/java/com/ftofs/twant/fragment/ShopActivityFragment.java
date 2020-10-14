package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.lib_net.model.CouponStoreVo;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.coupon_store.ShopCouponStoreListFragment;
import com.ftofs.twant.entity.StoreVoucher;
import com.google.android.material.tabs.TabLayout;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BasePopupView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;


/**
 * 商店活動Fragment
 * @author zwm
 */
public class ShopActivityFragment extends BaseFragment implements View.OnClickListener {
    /*
    voucherList[] 优惠券
    conformList[] 店铺满优惠
    discountList[] 限时折扣
     */
    ShopMainFragment parentFragment;
    ViewPager mViewPager;
    TabLayout tabLayout;
    LinearLayout llOuterContainer;
    //券倉
    List<StoreVoucher> couponStoreList = new ArrayList<>();
    LinearLayout llCouponStoreWrapper;
    LinearLayout llCouponStoreContainer;
    ViewGroupAdapter<CouponStoreVo> couponStoreListAdapter;


    List<Fragment> fragments;
    LinearLayout llPlaceholderContainer;
    TextView tvEmptyHint;
    Timer timer;
    public static ShopActivityFragment newInstance() {
        Bundle args = new Bundle();

        ShopActivityFragment fragment = new ShopActivityFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_activity, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (ShopMainFragment) getParentFragment();
        tabLayout = view.findViewById(R.id.tab_layout);
        mViewPager = view.findViewById(R.id.view_pager);

        tvEmptyHint = view.findViewById(R.id.tv_empty_hint);
        llPlaceholderContainer = view.findViewById(R.id.ll_placeholder_container);
        llOuterContainer = view.findViewById(R.id.ll_outer_container);


        couponStoreListAdapter =new ViewGroupAdapter<CouponStoreVo>(_mActivity,llCouponStoreContainer,R.layout.store_voucher_item) {
            @Override
            public void bindView(int position, View itemView, CouponStoreVo itemData) {
//                String token = User.getToken();
//                if (StringUtil.isEmpty(token)) {
//                    Util.showLoginFragment(requireContext());
//                    return;
//                }
                Util.startFragment(ShopCouponStoreListFragment.newInstance(0));
            }
        };
        couponStoreListAdapter.setItemClickListener((adapter, view1, position) -> {

        });
        fragments = new ArrayList<>();
        fragments.add(ShopCouponStoreListFragment.Companion.newInstance(0));
        fragments.add(ShopActivityVoucherListFragment.newInstance());

        List<String> titleList = new ArrayList<String>();
        titleList.add("1");
        titleList.add("2");
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(),titleList, fragments);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);

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


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    private void loadCouponStoreData() {
        try {
            String token = User.getToken();
            //允许未登录情况查看活动

            EasyJSONObject params = EasyJSONObject.generate(
                    "storeId", parentFragment.getStoreId());
            if (!StringUtil.isEmpty(token)) {
                params.set("token", token);
            }

            SLog.info("_11params[%s]", params);

            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
            String path = Api.PATH_COUPON_STORE;
            if (Util.inDev()) {
                path = "http://192.168.5.32:8100/" + path;
            }
            Api.postUI(path, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    SLog.info("loadStoreActivityData.responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    // 記錄有多少種空數據
                    int emptyDataCount = 0;
                    try {
//                        // 優惠券
//                        EasyJSONArray voucherList = responseObj.getSafeArray("datas.voucherList");
//                        storeVoucherList.clear();
//                        if (voucherList.length() > 0) {
//                            for (Object object : voucherList) {
//                                EasyJSONObject voucher = (EasyJSONObject) object;
//
//                                int state = Constant.COUPON_STATE_UNRECEIVED;
//                                if (voucher.exists("memberIsReceive")) {
//                                    int memberIsReceive = voucher.getInt("memberIsReceive");
//                                    if (memberIsReceive == 1) {
//                                        state = Constant.COUPON_STATE_RECEIVED;
//                                    }
//                                }
//                                StoreVoucher storeVoucher = new StoreVoucher(
//                                        voucher.getInt("storeId"),
//                                        voucher.getInt("templateId"),
//                                        voucher.getSafeString("storeName"),
//                                        voucher.getSafeString("storeAvatar"),
//                                        voucher.getInt("templatePrice"),
//                                        voucher.getSafeString("limitAmountText"),
//                                        voucher.getSafeString("usableClientTypeText"),
//                                        voucher.getSafeString("useStartTime"),
//                                        voucher.getSafeString("useEndTime"),
//                                        state);
//                                storeVoucherList.add(storeVoucher);
//                            }
//                            voucherListAdapter.setData(storeVoucherList);
//                        } else {
//                            llVoucherWrapper.setVisibility(View.GONE);
//                            emptyDataCount++;
//                        }
//
//
//                        // 如果3種數據都為空，顯示沒有數據的提示
//                        if (emptyDataCount == 3) {
//                            svContainer.setVisibility(View.GONE);
//                            llPlaceholderContainer.setVisibility(View.VISIBLE);
//                            tvEmptyHint.setText(R.string.no_store_activity_hint);
//                        }
//
//                        isStoreActivityDataLoaded = true;
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
