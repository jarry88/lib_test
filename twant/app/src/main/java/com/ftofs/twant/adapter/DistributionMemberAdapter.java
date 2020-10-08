package com.ftofs.twant.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.DistributionMember;

import java.util.List;

public class DistributionMemberAdapter extends BaseQuickAdapter<DistributionMember, BaseViewHolder> {
    public DistributionMemberAdapter(int layoutResId, @Nullable List<DistributionMember> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DistributionMember item) {

    }
}
