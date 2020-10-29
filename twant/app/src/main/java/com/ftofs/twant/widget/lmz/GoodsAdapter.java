package com.ftofs.twant.widget.lmz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;

import java.util.List;

public class GoodsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public GoodsAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_goods_name, item);
    }
}
