package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.hyphenate.chat.EMConversation;

/**
 * 常用語、常用版式設置Fragment
 * @author zwm
 */
public class CommonUsedSpeechSettingFragment extends BaseFragment implements View.OnClickListener {
    public static ChatFragment newInstance(EMConversation conversation, ChatFragment.Extra extra) {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        fragment.setConversation(conversation);
        if (extra != null) {
            fragment.setExtraData(extra);
        }

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
    }


    @Override
    public void onClick(View v) {

    }
}


