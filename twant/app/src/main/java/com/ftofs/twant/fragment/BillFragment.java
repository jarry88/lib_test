package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;

/**
 * 訂單頁面
 * @author zwm
 */
public class BillFragment extends BaseFragment implements View.OnClickListener {
    public static BillFragment newInstance() {
        Bundle args = new Bundle();

        BillFragment fragment = new BillFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.bill_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.text_bill_all)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.text_bill_to_be_paid)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.text_bill_to_be_sent)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.text_bill_to_be_received)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.text_bill_to_be_commented)));
    }

    @Override
    public void onClick(View v) {
        
    }
}
