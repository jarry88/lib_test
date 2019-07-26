package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ftofs.twant.R;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

/**
 * 通用設置Fragment
 * @author zwm
 */
public class UniversalFragment extends BaseFragment implements View.OnClickListener {
    public static UniversalFragment newInstance() {
        Bundle args = new Bundle();

        UniversalFragment fragment = new UniversalFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_universal, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_logout, this);
        Util.setOnClickListener(view, R.id.btn_personal_info, this);
        Util.setOnClickListener(view, R.id.btn_security_setting, this);
        Util.setOnClickListener(view, R.id.btn_about_takewant, this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_personal_info:
                Util.startFragment(PersonalInfoFragment.newInstance());
                break;
            case R.id.btn_security_setting:
                Util.startFragment(SecuritySettingFragment.newInstance());
                break;
            case R.id.btn_logout:
                User.logout();
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_LOGOUT_SUCCESS, null);
                pop();
                break;
            case R.id.btn_about_takewant:
                Util.startFragment(AboutFragment.newInstance());
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

