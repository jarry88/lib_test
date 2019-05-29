package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.domain.store.StoreLabel;

import java.util.List;

public class StoreLabelListAdapter extends BaseQuickAdapter<StoreLabel, BaseViewHolder> {
    public StoreLabelListAdapter(int layoutResId, @Nullable List<StoreLabel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreLabel item) {

    }
}
