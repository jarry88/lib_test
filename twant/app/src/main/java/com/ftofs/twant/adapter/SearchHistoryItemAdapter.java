package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;

import java.util.List;

public class SearchHistoryItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SearchHistoryItemAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_history, item);
    }
}
