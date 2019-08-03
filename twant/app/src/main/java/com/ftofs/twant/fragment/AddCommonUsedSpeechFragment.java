package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;

/**
 * 添加常用語、常用版式Fragment
 * @author zwm
 */
public class AddCommonUsedSpeechFragment extends BaseFragment implements View.OnClickListener {
    public static AddCommonUsedSpeechFragment newInstance() {
        Bundle args = new Bundle();

        AddCommonUsedSpeechFragment fragment = new AddCommonUsedSpeechFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_common_used_speech, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onClick(View v) {

    }
}
