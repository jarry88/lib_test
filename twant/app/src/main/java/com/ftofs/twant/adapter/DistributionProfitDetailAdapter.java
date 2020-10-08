package com.ftofs.twant.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.DistributionProfitDetail;

import java.util.List;

/**
 * 分銷系統收益明細Adapter
 * @author zwm
 */
public class DistributionProfitDetailAdapter extends BaseMultiItemQuickAdapter<DistributionProfitDetail, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public DistributionProfitDetailAdapter(List<DistributionProfitDetail> data) {
        super(data);

        addItemType(Constant.ITEM_TYPE_HEADER, R.layout.distribution_profit_detail_header);
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.distribution_profit_detail_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DistributionProfitDetail item) {

    }
}
