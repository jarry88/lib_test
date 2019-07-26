package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

/**
 * 商城Fragment
 * @author zwm
 */
public class MallFragment extends BaseFragment implements View.OnClickListener {
    public static MallFragment newInstance() {
        Bundle args = new Bundle();

        MallFragment fragment = new MallFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mall, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_my_bill, this);

        Util.setOnClickListener(view, R.id.btn_to_be_paid, this);
        Util.setOnClickListener(view, R.id.btn_to_be_shipped, this);
        Util.setOnClickListener(view, R.id.btn_to_be_received, this);
        Util.setOnClickListener(view, R.id.btn_to_be_commented, this);
        Util.setOnClickListener(view, R.id.icon_return_or_exchange, this);

        Util.setOnClickListener(view, R.id.btn_my_express, this);
        Util.setOnClickListener(view, R.id.btn_my_store_coupon, this);
        Util.setOnClickListener(view, R.id.btn_my_footprint, this);
        Util.setOnClickListener(view, R.id.btn_my_address, this);
        Util.setOnClickListener(view, R.id.btn_my_bonus, this);
        Util.setOnClickListener(view, R.id.btn_my_trust_value, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_my_bill:
                Util.startFragment(BillFragment.newInstance(Constant.ORDER_STATUS_ALL));
                break;

            case R.id.btn_to_be_paid:
            case R.id.btn_to_be_shipped:
            case R.id.btn_to_be_received:
            case R.id.btn_to_be_commented:
                int orderStatus;
                if (id == R.id.btn_to_be_paid) {
                    orderStatus = Constant.ORDER_STATUS_TO_BE_PAID;
                } else if (id == R.id.btn_to_be_shipped) {
                    orderStatus = Constant.ORDER_STATUS_TO_BE_SHIPPED;
                } else if (id == R.id.btn_to_be_received) {
                    orderStatus = Constant.ORDER_STATUS_TO_BE_RECEIVED;
                } else {
                    orderStatus = Constant.ORDER_STATUS_TO_BE_COMMENTED;
                }
                Util.startFragment(BillFragment.newInstance(orderStatus));
                break;

            case R.id.icon_return_or_exchange:
                Util.startFragment(RefundFragment.newInstance());
                break;

            case R.id.btn_my_express:
                Util.startFragment(SendPackageFragment.newInstance());
                break;

            case R.id.btn_my_store_coupon:
                Util.startFragment(StoreCouponFragment.newInstance());
                break;

            case R.id.btn_my_footprint:
                Util.startFragment(FootprintFragment.newInstance());
                break;

            case R.id.btn_my_address:
                Util.startFragment(AddrManageFragment.newInstance());
                break;

            case R.id.btn_my_bonus:
                Util.startFragment(TrustValueFragment.newInstance(TrustValueFragment.DATA_TYPE_BONUS));
                break;

            case R.id.btn_my_trust_value:
                Util.startFragment(TrustValueFragment.newInstance(TrustValueFragment.DATA_TYPE_TRUST_VALUE));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
