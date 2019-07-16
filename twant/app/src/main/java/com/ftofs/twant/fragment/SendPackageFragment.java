package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;


/**
 * 寄包裹Fragment
 * @author zwm
 */
public class SendPackageFragment extends BaseFragment implements View.OnClickListener {
    public static SendPackageFragment newInstance() {
        Bundle args = new Bundle();

        SendPackageFragment fragment = new SendPackageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_package, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_query_package, this);
        Util.setOnClickListener(view, R.id.btn_input_sender_info, this);
        Util.setOnClickListener(view, R.id.btn_input_receiver_info, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        MainFragment mainFragment = MainFragment.getInstance();

        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_query_package:
                mainFragment.start(QueryPackageFragment.newInstance());
                break;
            case R.id.btn_input_sender_info:
                mainFragment.start(SenderInfoFragment.newInstance(SenderInfoFragment.DATA_TYPE_SENDER_INFO));
                break;
            case R.id.btn_input_receiver_info:
                mainFragment.start(SenderInfoFragment.newInstance(SenderInfoFragment.DATA_TYPE_RECEIVER_INFO));
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
