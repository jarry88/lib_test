package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.TestAdapter;
import com.ftofs.twant.interfaces.ScrollableContainer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author linmeizhen
 * @date 2018/8/24
 * @description
 */
public class NestedScrollingShopFragment extends BaseFragment implements ScrollableContainer {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private int index;
    private GridLayoutManager gridLayoutManager;
    private TestAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_nested_scroll,null);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {

    }

    private void initView() {
        gridLayoutManager = new GridLayoutManager(_mActivity,1);
        recyclerview.setLayoutManager(gridLayoutManager);
        adapter = new TestAdapter();
        recyclerview.setAdapter(adapter);
    }

    public void setIndex(int index){
        this.index = index;
    }

    @Override
    public View getScrollableView() {
        return recyclerview;
    }
}

