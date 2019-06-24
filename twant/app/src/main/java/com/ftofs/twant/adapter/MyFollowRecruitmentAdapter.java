package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.MyFollowRecruitmentItem;

import java.util.List;

public class MyFollowRecruitmentAdapter extends BaseQuickAdapter<MyFollowRecruitmentItem, BaseViewHolder> {
    public MyFollowRecruitmentAdapter(int layoutResId, @Nullable List<MyFollowRecruitmentItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFollowRecruitmentItem item) {

    }
}

