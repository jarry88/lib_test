package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;

import java.util.List;

public class SkuSpecListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SkuSpecListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        SLog.info("ITEM[%s]", item);
        helper.setText(R.id.tv_specs, item);
    }
}
