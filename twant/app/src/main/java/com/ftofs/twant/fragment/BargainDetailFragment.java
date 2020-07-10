package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.util.Util;

/**
 * 砍價詳情頁
 * @author zwm
 */
public class BargainDetailFragment extends BaseFragment implements View.OnClickListener {
    public static BargainListFragment newInstance() {
        Bundle args = new Bundle();

        BargainListFragment fragment = new BargainListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bargain_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_invite_friend, this);

        Util.setOnClickListener(view, R.id.btn_back, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_invite_friend) {

        } else if (id == R.id.btn_buy_now) {
            Util.startFragment(ConfirmOrderFragment.newInstance(Constant.FALSE_INT, null));
        }
    }
}

