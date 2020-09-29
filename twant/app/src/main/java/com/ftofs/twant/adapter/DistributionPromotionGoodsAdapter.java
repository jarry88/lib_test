package com.ftofs.twant.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.DistributionPromotionGoods;

import java.util.List;

public class DistributionPromotionGoodsAdapter extends BaseQuickAdapter<DistributionPromotionGoods, BaseViewHolder> {
    public DistributionPromotionGoodsAdapter(int layoutResId, @Nullable List<DistributionPromotionGoods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DistributionPromotionGoods item) {
        helper.setImageResource(R.id.ic_selected_indicator,
                item.selected ? R.drawable.ic_baseline_check_box_24 : R.drawable.ic_baseline_check_box_outline_blank_24);
    }
}
