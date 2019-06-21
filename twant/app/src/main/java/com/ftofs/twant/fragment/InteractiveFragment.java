package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

/**
 * 互動Fragment
 * @author zwm
 */
public class InteractiveFragment extends BaseFragment implements View.OnClickListener {
    public static InteractiveFragment newInstance() {
        Bundle args = new Bundle();

        InteractiveFragment fragment = new InteractiveFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interactive, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_my_like, this);
        Util.setOnClickListener(view, R.id.btn_my_follow, this);
    }

    @Override
    public void onClick(View v) {
        MainFragment mainFragment = MainFragment.getInstance();
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_my_like:
                mainFragment.start(MyLikeFragment.newInstance());
                break;
            case R.id.btn_my_follow:
                mainFragment.start(MyFollowFragment.newInstance());
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
