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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_my_bill, this);
        Util.setOnClickListener(view, R.id.btn_register, this);
        Util.setOnClickListener(view, R.id.btn_login, this);
        Util.setOnClickListener(view, R.id.btn_setting, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_register) {
            MainFragment mainFragment = (MainFragment) getParentFragment();
            mainFragment.start(RegisterFragment.newInstance());
        } else if (id == R.id.btn_login) {
            MainFragment mainFragment = (MainFragment) getParentFragment();
            mainFragment.start(LoginFragment.newInstance());
        } else if (id == R.id.btn_setting) {
            MainFragment mainFragment = (MainFragment) getParentFragment();
            mainFragment.start(SettingFragment.newInstance());
        } else if (id == R.id.btn_my_bill) {
            MainFragment mainFragment = (MainFragment) getParentFragment();
            mainFragment.start(BillFragment.newInstance());
        }
    }
}
