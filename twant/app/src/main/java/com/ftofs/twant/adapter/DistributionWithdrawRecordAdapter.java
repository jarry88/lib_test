package com.ftofs.twant.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.DistributionWithdrawRecord;

import java.util.List;

public class DistributionWithdrawRecordAdapter extends BaseQuickAdapter<DistributionWithdrawRecord, BaseViewHolder> {
    public DistributionWithdrawRecordAdapter(int layoutResId, @Nullable List<DistributionWithdrawRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DistributionWithdrawRecord item) {

    }
}
