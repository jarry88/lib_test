package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreVoucherListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 店鋪券Fragment
 * @author zwm
 */
public class StoreCouponFragment extends BaseFragment implements View.OnClickListener {
    int couponType = Constant.COUPON_TYPE_PLATFORM;

    List<StoreVoucher> availableStoreVoucherList = new ArrayList<>();
    List<StoreVoucher> unavailableStoreVoucherList = new ArrayList<>();

    StoreVoucherListAdapter availableAdapter;
    StoreVoucherListAdapter unavailableAdapter;

    public static StoreCouponFragment newInstance() {
        Bundle args = new Bundle();

        StoreCouponFragment fragment = new StoreCouponFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_coupon, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_receive_store_coupon, this);

        LinearLayout llAvailableStoreCouponContainer = view.findViewById(R.id.ll_available_store_coupon_container);
        availableAdapter = new StoreVoucherListAdapter(_mActivity, llAvailableStoreCouponContainer, R.layout.store_voucher_item);

        LinearLayout llUnavailableStoreCouponContainer = view.findViewById(R.id.ll_unavailable_store_coupon_container);
        unavailableAdapter = new StoreVoucherListAdapter(_mActivity, llUnavailableStoreCouponContainer, R.layout.store_voucher_item);

        SimpleTabManager simpleTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                if (isRepeat) {
                    SLog.info("重復點擊");
                    return;
                }


                if (id == R.id.btn_my_store_coupon) {
                    SLog.info("btn_my_store_coupon");
                    couponType = Constant.COUPON_TYPE_STORE;
                } else if (id == R.id.btn_my_platform_coupon) {
                    SLog.info("btn_my_platform_coupon");
                    couponType = Constant.COUPON_TYPE_PLATFORM;
                }
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_my_platform_coupon));
        simpleTabManager.add(view.findViewById(R.id.btn_my_store_coupon));

        loadData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_receive_store_coupon) {
            start(ReceiveStoreCouponFragment.newInstance(couponType));
        }
    }



    private void loadData() {
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
                                0
                        );
                        availableStoreVoucherList.add(storeVoucher);
                    }
                    SLog.info("length[%d]", availableStoreVoucherList.size());
                    availableAdapter.setData(availableStoreVoucherList);

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
                                1
                        );
                        unavailableStoreVoucherList.add(storeVoucher);
                    }
                    SLog.info("length[%d]", unavailableStoreVoucherList.size());
                    unavailableAdapter.setData(unavailableStoreVoucherList);
                } catch (Exception e) {
                    SLog.info("Error!%s", e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}


