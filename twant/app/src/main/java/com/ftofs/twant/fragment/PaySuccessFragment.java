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

import cn.snailpad.easyjson.EasyJSONObject;


/**
 * 訂單支付成功Fragment
 * @author zwm
 */
public class PaySuccessFragment extends BaseFragment implements View.OnClickListener {
    EasyJSONObject paramsObj;
    public static PaySuccessFragment newInstance(String paramsStr) {
        Bundle args = new Bundle();

        args.putString("paramsStr", paramsStr);
        PaySuccessFragment fragment = new PaySuccessFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_success, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String paramsStr = args.getString("paramsStr");

        paramsObj = (EasyJSONObject) EasyJSONObject.parse(paramsStr);

        Util.setOnClickListener(view, R.id.btn_view_order, this);
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        SLog.info("here");
        if (id == R.id.btn_view_order) {
            SLog.info("here");
            pop();
            // 轉去訂單列表，已跟進能確認過
            Util.startFragment(BillFragment.newInstance(Constant.ORDER_STATUS_TO_BE_SHIPPED));
        } else if (id == R.id.btn_goto_home) {
            popTo(MainFragment.class, false);
        }
    }
}

