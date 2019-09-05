package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreConformListAdapter;
import com.ftofs.twant.adapter.StoreDiscountListAdapter;
import com.ftofs.twant.adapter.StoreVoucherListAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreConform;
import com.ftofs.twant.entity.StoreDiscount;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;


/**
 * 店鋪活動Fragment
 * @author zwm
 */
public class ShopActivityFragment extends BaseFragment implements View.OnClickListener {
    /*
    voucherList[] 优惠券
    conformList[] 店铺满优惠
    discountList[] 限时折扣
     */
    ShopMainFragment parentFragment;

    LinearLayout llOuterContainer;

    List<StoreVoucher> storeVoucherList = new ArrayList<>();
    LinearLayout llVoucherWrapper;
    LinearLayout llVoucherContainer;
    StoreVoucherListAdapter voucherListAdapter;

    List<StoreConform> storeConformList = new ArrayList<>();
    LinearLayout llConformWrapper;
    LinearLayout llConformContainer;
    StoreConformListAdapter conformListAdapter;

    List<StoreDiscount> storeDiscountList = new ArrayList<>();
    LinearLayout llDiscountWrapper;
    LinearLayout llDiscountContainer;
    StoreDiscountListAdapter discountListAdapter;

    boolean isStoreActivityDataLoaded;

    public static ShopActivityFragment newInstance() {
        Bundle args = new Bundle();

        ShopActivityFragment fragment = new ShopActivityFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_activity, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (ShopMainFragment) getParentFragment();

        llOuterContainer = view.findViewById(R.id.ll_outer_container);

        llVoucherWrapper = view.findViewById(R.id.ll_voucher_wrapper);
        llVoucherContainer = view.findViewById(R.id.ll_voucher_container);
        voucherListAdapter = new StoreVoucherListAdapter(_mActivity, llVoucherContainer, R.layout.store_voucher_item);
        voucherListAdapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_receive_voucher_now) {
                    StoreVoucher storeVoucher = storeVoucherList.get(position);
                    // 檢查未領取才調用領取接口
                    if (storeVoucher.memberIsReceive == Constant.ZERO) {
                        receiveVoucher(position, storeVoucher.templateId);
                    }
                }
            }
        });

        llConformWrapper = view.findViewById(R.id.ll_conform_wrapper);
        llConformContainer = view.findViewById(R.id.ll_conform_container);
        conformListAdapter = new StoreConformListAdapter(_mActivity, llConformContainer, R.layout.store_conform_item);
        conformListAdapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_participate_activity) {
                    EasyJSONObject params = EasyJSONObject.generate(
                            "storeId", parentFragment.getStoreId(),
                            "conformId", storeConformList.get(position).conformId);

                    Util.startFragment(ShopCommodityFragment.newInstance(true, params.toString()));
                }
            }
        });

        llDiscountWrapper = view.findViewById(R.id.ll_discount_wrapper);
        llDiscountContainer = view.findViewById(R.id.ll_discount_container);
        discountListAdapter = new StoreDiscountListAdapter(_mActivity, llDiscountContainer, R.layout.store_discount_item);
        discountListAdapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_participate_activity) {
                    EasyJSONObject params = EasyJSONObject.generate(
                            "storeId", parentFragment.getStoreId(),
                            "discountId", storeDiscountList.get(position).discountId);

                    Util.startFragment(ShopCommodityFragment.newInstance(true, params.toString()));
                }
            }
        });

        loadStoreActivityData();
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

    /**
     * 加載店鋪活動數據
     */
    private void loadStoreActivityData() {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                SLog.info("Error!token 為空");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate(
                    "storeId", parentFragment.getStoreId(),
                    "token", token);

            SLog.info("params[%s]", params);

            final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                    .asLoading(getString(R.string.text_loading))
                    .show();

            Api.postUI(Api.PATH_STORE_ACTIVITY, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    SLog.info("loadStoreActivityData.responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    // 記錄有多少種空數據
                    int emptyDataCount = 0;
                    try {
                        EasyJSONArray voucherList = responseObj.getArray("datas.voucherList");
                        if (voucherList.length() > 0) {
                            for (Object object : voucherList) {
                                EasyJSONObject voucher = (EasyJSONObject) object;

                                StoreVoucher storeVoucher = new StoreVoucher(
                                        voucher.getInt("storeId"),
                                        voucher.getInt("templateId"),
                                        voucher.getString("storeName"),
                                        voucher.getInt("templatePrice"),
                                        voucher.getString("limitAmountText"),
                                        voucher.getString("usableClientTypeText"),
                                        voucher.getString("useStartTime"),
                                        voucher.getString("useEndTime"),
                                        voucher.getInt("memberIsReceive"));
                                storeVoucherList.add(storeVoucher);
                            }
                            voucherListAdapter.setData(storeVoucherList);
                        } else {
                            llVoucherWrapper.setVisibility(View.GONE);
                            emptyDataCount++;
                        }


                        EasyJSONArray conformList = responseObj.getArray("datas.conformList");
                        if (conformList.length() > 0) {
                            for (Object object : conformList) {
                                EasyJSONObject conform = (EasyJSONObject) object;

                                StoreConform storeConform = new StoreConform(
                                        conform.getInt("storeId"),
                                        conform.getInt("conformId"),
                                        conform.getInt("limitAmount"),
                                        conform.getInt("conformPrice"),
                                        conform.getString("startTime"),
                                        conform.getString("endTime"));
                                storeConformList.add(storeConform);
                            }
                            conformListAdapter.setData(storeConformList);
                        } else {
                            llConformWrapper.setVisibility(View.GONE);
                            emptyDataCount++;
                        }


                        EasyJSONArray discountList = responseObj.getArray("datas.discountList");
                        if (discountList.length() > 0) {
                            for (Object object : discountList) {
                                EasyJSONObject discount = (EasyJSONObject) object;

                                StoreDiscount storeDiscount = new StoreDiscount(
                                        discount.getInt("storeId"),
                                        discount.getInt("discountId"),
                                        (float) discount.getDouble("discountRate"),
                                        discount.getInt("goodsCount"));
                                storeDiscountList.add(storeDiscount);
                            }
                            discountListAdapter.setData(storeDiscountList);
                        } else {
                            llDiscountWrapper.setVisibility(View.GONE);
                            emptyDataCount++;
                        }

                        // 如果3種數據都為空，顯示沒有數據的提示
                        if (emptyDataCount == 3) {
                            llOuterContainer.removeAllViews();

                            View root = LayoutInflater.from(_mActivity).inflate(R.layout.no_result_empty_view, llOuterContainer, true);
                            TextView tvEmptyHint = root.findViewById(R.id.tv_empty_hint);
                            tvEmptyHint.setText(R.string.no_store_activity_hint);
                        }

                        isStoreActivityDataLoaded = true;
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                        SLog.info("Error!loadStoreActivityData failed");
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void receiveVoucher(final int position, int templateId) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "templateId", templateId);

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_RECEIVE_VOUCHER, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                ToastUtil.success(_mActivity, "領取成功");
                StoreVoucher storeVoucher = storeVoucherList.get(position);
                storeVoucher.memberIsReceive = 1;
                voucherListAdapter.setData(storeVoucherList);
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!isStoreActivityDataLoaded) {
            loadStoreActivityData();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
