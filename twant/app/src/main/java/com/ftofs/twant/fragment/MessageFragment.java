package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.util.Util;


/**
 * 消息
 * @author zwm
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    public static MessageFragment newInstance() {
        Bundle args = new Bundle();

        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_view_logistics_message, this);
        Util.setOnClickListener(view, R.id.btn_view_refund_message, this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        MainFragment mainFragment = MainFragment.getInstance();
        if (id == R.id.btn_view_logistics_message) {
            mainFragment.start(LogisticsMessageListFragment.newInstance(Constant.MESSAGE_CATEGORY_LOGISTICS));
        } else if (id == R.id.btn_view_refund_message) {
            mainFragment.start(LogisticsMessageListFragment.newInstance(Constant.MESSAGE_CATEGORY_REFUND));
        }
    }
}
