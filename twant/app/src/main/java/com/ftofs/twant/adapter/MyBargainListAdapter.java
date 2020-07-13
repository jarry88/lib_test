package com.ftofs.twant.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.MyBargainListItem;

import java.util.List;

public class MyBargainListAdapter extends BaseQuickAdapter<MyBargainListItem, BaseViewHolder> {
    Context context;

    public MyBargainListAdapter(int layoutResId, @Nullable List<MyBargainListItem> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyBargainListItem item) {

    }
}
