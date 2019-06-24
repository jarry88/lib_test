package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.MyFollowMemberItem;

import java.util.List;

public class MyFollowMemberAdapter extends BaseQuickAdapter<MyFollowMemberItem, BaseViewHolder> {
    public MyFollowMemberAdapter(int layoutResId, @Nullable List<MyFollowMemberItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFollowMemberItem item) {

    }
}
