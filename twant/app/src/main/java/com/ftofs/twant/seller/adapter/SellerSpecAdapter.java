package com.ftofs.twant.seller.adapter;


import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.seller.entity.SellerSpecItem;

import java.util.List;

public class SellerSpecAdapter extends BaseQuickAdapter<SellerSpecItem, BaseViewHolder> {
    Context context;

    int twBlack;
    int twBlue;

    public SellerSpecAdapter(Context context, int layoutResId, @Nullable List<SellerSpecItem> data) {
        super(layoutResId, data);

        this.context = context;

        twBlue = context.getColor(R.color.tw_blue);
        twBlack = context.getColor(R.color.tw_black);
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerSpecItem item) {
        helper.setText(R.id.tv_name, item.name);
        helper.setTextColor(R.id.tv_name, item.selected ? twBlue : twBlack);
        helper.setGone(R.id.tv_indicator, item.selected);
    }
}


