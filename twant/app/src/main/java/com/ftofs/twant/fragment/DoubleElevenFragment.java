package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;

/**
 * 雙11活動入口頁面
 * @author zwm
 */
public class DoubleElevenFragment extends BaseFragment implements View.OnClickListener {
    public static DoubleElevenFragment newInstance() {
        Bundle args = new Bundle();

        DoubleElevenFragment fragment = new DoubleElevenFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_double_eleven, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_go_shopping).setOnClickListener(this);
        view.findViewById(R.id.btn_play_game).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_go_shopping) {

        } else if (id == R.id.btn_play_game) {
            start(H5GameFragment.newInstance("http://gogo.so/del.jpg"));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}


