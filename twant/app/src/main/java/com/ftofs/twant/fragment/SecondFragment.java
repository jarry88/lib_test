package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.TestAdapter;
import com.gzp.lib_common.base.BaseFragment;

import org.jetbrains.annotations.NotNull;

public class SecondFragment extends BaseFragment {
    RecyclerView rvList;
    TestAdapter testAdapter;

    public static SecondFragment newInstance() {
        Bundle args = new Bundle();

        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        testAdapter = new TestAdapter();
        rvList.setAdapter(testAdapter);

        setNestedScrollingEnabled(false);
    }


    public void setNestedScrollingEnabled(boolean enabled) {
        rvList.setNestedScrollingEnabled(enabled);
    }
}
