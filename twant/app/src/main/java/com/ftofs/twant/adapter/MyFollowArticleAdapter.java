package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.MyFollowArticleItem;

import java.util.List;

public class MyFollowArticleAdapter extends BaseQuickAdapter<MyFollowArticleItem, BaseViewHolder> {
    public MyFollowArticleAdapter(int layoutResId, @Nullable List<MyFollowArticleItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFollowArticleItem item) {

    }
}
