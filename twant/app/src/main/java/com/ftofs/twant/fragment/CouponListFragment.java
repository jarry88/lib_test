package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreVoucherListAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 卡券列表
 * @author zwm
 */
public class CouponListFragment extends BaseFragment implements View.OnClickListener {
    int couponType;

    TextView tvEmptyHint;

    ScrollView svContainer;
    LinearLayout llEmptyViewContainer;
    LinearLayout llUnavailableCouponOuterContainer;

    TextView tvUnavailableCouponTitle;

    List<StoreVoucher> availableVoucherList = new ArrayList<>();
    List<StoreVoucher> unavailableVoucherList = new ArrayList<>();
    StoreVoucherListAdapter availableCouponAdapter;
    StoreVoucherListAdapter unavailableCouponAdapter;

    public static CouponListFragment newInstance(int couponType) {
        Bundle args = new Bundle();

        args.putInt("couponType", couponType);
        CouponListFragment fragment = new CouponListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coupon_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        couponType = args.getInt("couponType");
        tvUnavailableCouponTitle = view.findViewById(R.id.tv_unavailable_coupon_title);

        svContainer = view.findViewById(R.id.sv_container);
        llEmptyViewContainer = view.findViewById(R.id.ll_empty_view_container);
        llUnavailableCouponOuterContainer = view.findViewById(R.id.ll_unavailable_coupon_outer_container);
        tvEmptyHint = view.findViewById(R.id.tv_empty_hint);

        LinearLayout llAvailableCouponContainer = view.findViewById(R.id.ll_available_coupon_container);
        availableCouponAdapter = new StoreVoucherListAdapter(_mActivity, llAvailableCouponContainer, R.layout.store_voucher_item);
        availableCouponAdapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                StoreVoucher storeVoucher = availableVoucherList.get(position);
                if (storeVoucher.storeId > 0) {
                    Util.startFragment(ShopMainFragment.newInstance(storeVoucher.storeId));
                }
            }
        });

        LinearLayout llUnavailableStoreCouponContainer = view.findViewById(R.id.ll_unavailable_coupon_container);
        unavailableCouponAdapter = new StoreVoucherListAdapter(_mActivity, llUnavailableStoreCouponContainer, R.layout.store_voucher_item);

        if (couponType == Constant.COUPON_TYPE_STORE) {
            tvUnavailableCouponTitle.setText("不可用店鋪券");
            loadStoreCouponData();
        } else if (couponType == Constant.COUPON_TYPE_PLATFORM) {
            tvUnavailableCouponTitle.setText("不可用平台券");
            loadPlatformCouponData();
        }
    }

    @Override
    public void onClick(View v) {
    }


    /**
     * 獲取平台券列表
     */
    private void loadPlatformCouponData() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        Api.postUI(Api.PATH_PLATFORM_COUPON_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    // 平台券列表
                    EasyJSONArray couponList = responseObj.getArray("datas.couponList");
                    for (Object object : couponList) {
                        EasyJSONObject voucher = (EasyJSONObject) object;

                        int couponState = voucher.getInt("couponState");
                        StoreVoucher storeVoucher = new StoreVoucher(
                                0,
                                0,
                                voucher.getString("useGoodsRangeExplain"),
                                voucher.getInt("couponPrice"),
                                voucher.getString("limitAmountText"),
                                voucher.getString("usableClientTypeText"),
                                voucher.getString("useStartTimeText"),
                                voucher.getString("useEndTimeText"),
                                couponState == 0
                        );
                        if (couponState == 0) {
                            availableVoucherList.add(storeVoucher);
                        } else {
                            unavailableVoucherList.add(storeVoucher);
                        }

                    }
                    SLog.info("length[%d]", availableVoucherList.size());
                    availableCouponAdapter.setData(availableVoucherList);
                    unavailableCouponAdapter.setData(unavailableVoucherList);

                    emptyDataHandler(availableVoucherList.size(), unavailableVoucherList.size());
                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }


    /**
     * 獲取店鋪券列表
     */
    private void loadStoreCouponData() {
        String token = User.getToken();

        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token);

        Api.postUI(Api.PATH_STORE_COUPON_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    // 可用的優惠券
                    EasyJSONArray usableList = responseObj.getArray("datas.useable");  // useable服務器端拼寫錯誤
                    for (Object object : usableList) {
                        EasyJSONObject voucher = (EasyJSONObject) object;

                        StoreVoucher storeVoucher = new StoreVoucher(
                                voucher.getInt("store.storeId"),
                                voucher.getInt("templateId"),
                                voucher.getString("store.storeName"),
                                voucher.getInt("price"),
                                voucher.getString("limitAmountText"),
                                voucher.getString("voucherUsableClientTypeText"),
                                voucher.getString("startTime"),
                                voucher.getString("endTime"),
                                true
                        );
                        storeVoucher.received = true;
                        availableVoucherList.add(storeVoucher);
                    }
                    SLog.info("length[%d]", availableVoucherList.size());
                    availableCouponAdapter.setData(availableVoucherList);

                    // 不可用的優惠券
                    EasyJSONArray unusableList = responseObj.getArray("datas.unUseable");
                    for (Object object : unusableList) {
                        EasyJSONObject voucher = (EasyJSONObject) object;

                        StoreVoucher storeVoucher = new StoreVoucher(
                                voucher.getInt("store.storeId"),
                                voucher.getInt("templateId"),
                                voucher.getString("store.storeName"),
                                voucher.getInt("price"),
                                voucher.getString("limitAmountText"),
                                voucher.getString("voucherUsableClientTypeText"),
                                voucher.getString("startTime"),
                                voucher.getString("endTime"),
                                false
                        );
                        unavailableVoucherList.add(storeVoucher);
                    }

                    SLog.info("length[%d]", unavailableVoucherList.size());
                    unavailableCouponAdapter.setData(unavailableVoucherList);

                    emptyDataHandler(availableVoucherList.size(), unavailableVoucherList.size());
                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }

    /**
     * 數據為空時的處理（顯示數據空提示）
     */
    private void emptyDataHandler(int availableCouponCount, int unavailableCouponCount) {
        if (unavailableCouponCount == 0) {
            if (availableCouponCount == 0) { // 如果沒有可用的券和不可用的券
                svContainer.setVisibility(View.GONE);
                llEmptyViewContainer.setVisibility(View.VISIBLE);

                tvEmptyHint.setText(R.string.no_data_hint);
            } else { // 如果沒有不可用的券，但有可用的券
                llUnavailableCouponOuterContainer.setVisibility(View.GONE);
            }
        }
    }
}
