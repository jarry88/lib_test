package com.ftofs.twant.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;


/**
 * 我的
 * @author zwm
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    public static MyFragment newInstance() {
        Bundle args = new Bundle();

        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);

        return fragment;
    }

    TextView tvMobile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvMobile = view.findViewById(R.id.tv_mobile);

        Util.setOnClickListener(view, R.id.btn_my_bill, this);

        Util.setOnClickListener(view, R.id.btn_to_be_paid, this);
        Util.setOnClickListener(view, R.id.btn_to_be_shipped, this);
        Util.setOnClickListener(view, R.id.btn_to_be_received, this);
        Util.setOnClickListener(view, R.id.btn_to_be_commented, this);

        Util.setOnClickListener(view, R.id.btn_register, this);
        Util.setOnClickListener(view, R.id.img_avatar, this);
        Util.setOnClickListener(view, R.id.btn_setting, this);
        Util.setOnClickListener(view, R.id.btn_my_address, this);

        Util.setOnClickListener(view, R.id.icon_return_or_exchange, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        MainFragment mainFragment = (MainFragment) getParentFragment();

        switch (id) {
            case R.id.btn_register:
                mainFragment.start(RegisterFragment.newInstance());
                break;

            case R.id.img_avatar:
                // 判斷是否已經登錄，采取不同的動作
                int userId = User.getUserId();
                if (userId > 0) {
                    // 已經登錄，顯示【個人信息】
                    mainFragment.start(PersonalInfoFragment.newInstance());
                } else {
                    // 未登錄，顯示【登錄】頁面
                    mainFragment.start(LoginFragment.newInstance());
                }
                break;

            case R.id.btn_setting:
                mainFragment.start(SettingFragment.newInstance());
                break;

            case R.id.btn_my_bill:
                mainFragment.start(BillFragment.newInstance(Constant.ORDER_STATUS_ALL));
                break;

            case R.id.btn_my_address:
                mainFragment.start(AddrManageFragment.newInstance());
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
                mainFragment.start(BillFragment.newInstance(orderStatus));
                break;

            case R.id.icon_return_or_exchange:
                mainFragment.start(RefundFragment.newInstance());
                break;
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        // 更新手機號顯示
        String mobileEncrypt = Hawk.get(SPField.FIELD_MOBILE_ENCRYPT, "未登錄");
        tvMobile.setText(mobileEncrypt);
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
