package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ftofs.twant.R;
import com.gzp.lib_common.base.BaseFragment;

import org.jetbrains.annotations.NotNull;


/**
 * 如何參與砍價說明頁面
 * @author zwm
 */
public class BargainInstructionFragment extends BaseFragment implements View.OnClickListener {
    public static BargainListFragment newInstance() {
        Bundle args = new Bundle();

        BargainListFragment fragment = new BargainListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bargain_instruction, container, false);
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


