package com.ftofs.twant.seller.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.seller.entity.SellerOrderItem;

import java.util.List;

/**
 * 賣家版訂單列表適配器
 * @author zwm
 */
public class SellerOrderAdapter extends BaseQuickAdapter<SellerOrderItem, BaseViewHolder> {
    public SellerOrderAdapter(int layoutResId, @Nullable List<SellerOrderItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerOrderItem item) {
        helper.addOnClickListener(R.id.tv_buyer);
    }
}
