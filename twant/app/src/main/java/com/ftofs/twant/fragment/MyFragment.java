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
        Util.setOnClickListener(view, R.id.img_avatar, this);
        Util.setOnClickListener(view, R.id.btn_setting, this);

        Util.setOnClickListener(view, R.id.btn_mall, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        MainFragment mainFragment = (MainFragment) getParentFragment();

        switch (id) {
            case R.id.btn_mall:
                mainFragment.start(new MallFragment());
                break;
            case R.id.btn_setting:
                mainFragment.start(SettingFragment.newInstance());
                break;
            default:
                break;
        }
    }
}
