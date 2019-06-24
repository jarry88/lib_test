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
import com.ftofs.twant.widget.ScaledButton;


/**
 * 消息
 * @author zwm
 */
public class MessageFragment extends BaseFragment implements View.OnClickListener {
    /**
     * 是否獨立的Fragment，還是依附于MainFragment
     */
    boolean isStandalone;
    ScaledButton btnBack;

    public static MessageFragment newInstance(boolean isStandalone) {
        Bundle args = new Bundle();

        args.putBoolean("isStandalone", isStandalone);
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

        Bundle args = getArguments();
        isStandalone = args.getBoolean("isStandalone");

        Util.setOnClickListener(view, R.id.btn_view_logistics_message, this);
        Util.setOnClickListener(view, R.id.btn_view_refund_message, this);

        btnBack = view.findViewById(R.id.btn_back);

        if (isStandalone) {
            btnBack.setOnClickListener(this);
            btnBack.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        MainFragment mainFragment = MainFragment.getInstance();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_view_logistics_message) {
            mainFragment.start(LogisticsMessageListFragment.newInstance(Constant.MESSAGE_CATEGORY_LOGISTICS));
        } else if (id == R.id.btn_view_refund_message) {
            mainFragment.start(LogisticsMessageListFragment.newInstance(Constant.MESSAGE_CATEGORY_REFUND));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (isStandalone) {
            pop();
            return true;
        }
        return false;
    }
}
