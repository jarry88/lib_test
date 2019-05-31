package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreConformListAdapter;
import com.ftofs.twant.adapter.StoreDiscountListAdapter;
import com.ftofs.twant.adapter.StoreVoucherListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.StoreConform;
import com.ftofs.twant.entity.StoreDiscount;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
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

    List<StoreVoucher> storeVoucherList = new ArrayList<>();
    LinearLayout llVoucherContainer;
    StoreVoucherListAdapter voucherListAdapter;

    List<StoreConform> storeConformList = new ArrayList<>();
    LinearLayout llConformContainer;
    StoreConformListAdapter conformListAdapter;

    List<StoreDiscount> storeDiscountList = new ArrayList<>();
    LinearLayout llDiscountContainer;
    StoreDiscountListAdapter discountListAdapter;

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

        llVoucherContainer = view.findViewById(R.id.ll_voucher_container);
        voucherListAdapter = new StoreVoucherListAdapter(_mActivity, llVoucherContainer, R.layout.store_voucher_item);

        llConformContainer = view.findViewById(R.id.ll_conform_container);
        conformListAdapter = new StoreConformListAdapter(_mActivity, llConformContainer, R.layout.store_conform_item);

        llDiscountContainer = view.findViewById(R.id.ll_discount_container);
        discountListAdapter = new StoreDiscountListAdapter(_mActivity, llDiscountContainer, R.layout.store_discount_item);

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
                    "storeId", parentFragment.getShopId(),
                    "token", token);

            SLog.info("params[%s]", params);

            final BasePopupView loadingPopup = new XPopup.Builder(getContext())
                    .asLoading("正在加載")
                    .show();

            Api.postUI(Api.PATH_STORE_ACTIVITY, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    try {
                        EasyJSONArray voucherList = responseObj.getArray("datas.voucherList");
                        for (Object object : voucherList) { // PayObject
                            EasyJSONObject voucher = (EasyJSONObject) object;

                            StoreVoucher storeVoucher = new StoreVoucher(
                                    voucher.getInt("storeId"),
                                    voucher.getString("storeName"),
                                    (int) voucher.getDouble("templatePrice"),
                                    voucher.getString("limitAmountText"),
                                    voucher.getString("usableClientTypeText"),
                                    voucher.getString("useStartTime"),
                                    voucher.getString("useEndTime"),
                                    voucher.getInt("memberIsReceive"));
                            storeVoucherList.add(storeVoucher);
                        }
                        voucherListAdapter.setData(storeVoucherList);

                        EasyJSONArray conformList = responseObj.getArray("datas.conformList");
                        for (Object object : conformList) {
                            EasyJSONObject conform = (EasyJSONObject) object;

                            StoreConform storeConform = new StoreConform(
                                    (int) conform.getDouble("conformId"),
                                    (int) conform.getDouble("limitAmount"),
                                    (int) conform.getDouble("conformPrice"),
                                    conform.getString("startTime"),
                                    conform.getString("endTime"));
                            storeConformList.add(storeConform);
                        }
                        conformListAdapter.setData(storeConformList);

                        EasyJSONArray discountList = responseObj.getArray("datas.discountList");
                        for (Object object : discountList) {
                            EasyJSONObject discount = (EasyJSONObject) object;

                            StoreDiscount storeDiscount = new StoreDiscount(
                                    discount.getInt("discountId"),
                                    (float) discount.getDouble("discountRate"),
                                    discount.getInt("goodsCount"));
                            storeDiscountList.add(storeDiscount);
                        }
                        discountListAdapter.setData(storeDiscountList);

                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                        SLog.info("Error!loadStoreActivityData failed");
                    }
                }
            });
        } catch (Exception e) {

        }
    }
}
