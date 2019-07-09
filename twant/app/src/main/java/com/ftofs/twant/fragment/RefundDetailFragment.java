package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.util.Util;


/**
 * 退款詳情Fragment
 * @author zwm
 */
public class RefundDetailFragment extends BaseFragment implements View.OnClickListener {
    int refundId;

    public static RefundDetailFragment newInstance(int refundId) {
        Bundle args = new Bundle();

        args.putInt("refundId", refundId);

        RefundDetailFragment fragment = new RefundDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refund_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        refundId = args.getInt("refundId");

        Util.setOnClickListener(view, R.id.btn_back, this);
    }
    
    
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }
}
