package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.repository.Repository;
import com.ftofs.twant.widget.SimpleTabManager;

public class RefundFragment extends BaseFragment {
    public static RefundFragment newInstance() {
        Bundle args = new Bundle();
        RefundFragment fragment = new RefundFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        try {
            Repository.getRefundList();
            Repository.getReturnList();
        }catch (Exception e){
            e.printStackTrace();
        }
        */



        SimpleTabManager simpleTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                if (isRepeat) {
                    SLog.info("重復點擊");
                    return;
                }


            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_refund));
        simpleTabManager.add(view.findViewById(R.id.btn_return));
        simpleTabManager.add(view.findViewById(R.id.btn_complain));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refund, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
