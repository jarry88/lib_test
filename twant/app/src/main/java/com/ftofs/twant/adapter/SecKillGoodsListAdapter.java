package com.ftofs.twant.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.SecKillGoodsListItem;

import java.util.List;

public class SecKillGoodsListAdapter extends BaseQuickAdapter<SecKillGoodsListItem, BaseViewHolder> {
    Context context;

    public SecKillGoodsListAdapter(Context context, int layoutResId, @Nullable List<SecKillGoodsListItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SecKillGoodsListItem item) {

    }
}
