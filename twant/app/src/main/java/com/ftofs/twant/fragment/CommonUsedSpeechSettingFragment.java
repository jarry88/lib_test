package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

/**
 * 常用語、常用版式設置Fragment
 * @author zwm
 */
public class CommonUsedSpeechSettingFragment extends BaseFragment implements View.OnClickListener {
    public static CommonUsedSpeechSettingFragment newInstance() {
        Bundle args = new Bundle();

        CommonUsedSpeechSettingFragment fragment = new CommonUsedSpeechSettingFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_used_speech_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_add, this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_add) {
            startForResult(AddCommonUsedSpeechFragment.newInstance(), RequestCode.UPDATE_COMMON_USED_SPEECH.ordinal());
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}


