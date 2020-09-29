package com.ftofs.twant.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.DistributionOrderItem;

import java.util.List;

/**
 * 分銷系統訂單Adapter
 * @author zwm
 */
public class DistributionOrderAdapter extends BaseQuickAdapter<DistributionOrderItem, BaseViewHolder> {

    public DistributionOrderAdapter(int layoutResId, @Nullable List<DistributionOrderItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DistributionOrderItem item) {

    }
}
