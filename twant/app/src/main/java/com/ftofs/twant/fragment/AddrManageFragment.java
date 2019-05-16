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
 * 地址管理Fragment
 * @author zwm
 */
public class AddrManageFragment extends BaseFragment implements View.OnClickListener {
    public static AddrManageFragment newInstance() {
        Bundle args = new Bundle();

        AddrManageFragment fragment = new AddrManageFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addr_management, container, false);

        Util.setOnClickListener(view, R.id.btn_add_address, this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add_address) {
            MainFragment mainFragment = MainFragment.getInstance();
            mainFragment.start(AddAddressFragment.newInstance());
        }
    }
}
