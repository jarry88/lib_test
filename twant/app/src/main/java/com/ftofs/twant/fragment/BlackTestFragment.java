package com.ftofs.twant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SimpleViewPagerAdapter;
import com.ftofs.twant.entity.ElemeGroupedItem;
import com.google.gson.Gson;
import com.kunminx.linkage.LinkageRecyclerView;
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder;
import com.kunminx.linkage.bean.BaseGroupedItem;
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig;
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig;

/**
 * @author gzp
 * 顯示layout佈局界面
 */
public class BlackTestFragment extends BaseFragment{
    int res;
    LinkageRecyclerView linkage;
    public static BlackTestFragment newInstance() {
        BlackTestFragment fragment = new BlackTestFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.black_layout, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewpager = view.findViewById(R.id.view_pager);
        viewpager.setAdapter(new SimpleViewPagerAdapter(_mActivity,null));

        initLinkageDatas(linkage);


    }
    private void initLinkageDatas(LinkageRecyclerView linkage) {
        Gson gson = new Gson();
//        List<ElemeGroupedItem> items = gson.fromJson(getString(R.string.operators_json),
//                new TypeToken<List<ElemeGroupedItem>>() {
//                }.getType());
//
//        linkage.init(items, new ElemePrimaryAdapterConfig(), new ElemeSecondaryAdapterConfig());
    }

    @Override
    public boolean onBackPressedSupport() {
        hideSoftInputPop();
        return true;
    }
}
