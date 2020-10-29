package com.ftofs.twant.widget.lmz;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linmeizhen
 * @date 2018/8/24
 * @description
 */
public class LmzNestedScrollingDemoFragment extends Fragment implements LmzScrollableContainer {
    RecyclerView rvList;
    private MainActivity mActivity;
    private GoodsAdapter adapter;
    private int index;

    List<String> goodsNameList = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lmz_nested_scrolling_demo, null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String goodsName;
        int itemCount;
        switch (index) {
            case 0:
                goodsName = "精选";
                itemCount = 20;
                break;
            case 1:
                goodsName = "女装";
                itemCount = 5;
                break;
            case 2:
                goodsName = "男装";
                itemCount = 30;
                break;
            case 3:
                goodsName = "鞋包";
                itemCount = 2;
                break;
            default:
                goodsName = "未知";
                itemCount = 1;
                break;
        }

        goodsNameList.clear();
        for (int i = 0; i < itemCount; i++) {
            goodsNameList.add(goodsName + "-" + i);
        }

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new GoodsAdapter(R.layout.goods_item, goodsNameList);
        rvList.setAdapter(adapter);
    }

    public void setIndex(int index){
        this.index = index;
    }

    @Override
    public View getScrollableView() {
        return rvList;
    }
}
