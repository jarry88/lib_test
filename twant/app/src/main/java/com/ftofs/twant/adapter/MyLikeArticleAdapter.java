package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.PostItem;

import java.util.List;

public class MyLikeArticleAdapter extends BaseQuickAdapter<PostItem, BaseViewHolder> {
    public MyLikeArticleAdapter(int layoutResId, @Nullable List<PostItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostItem item) {

    }
}
