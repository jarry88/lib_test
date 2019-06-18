package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.MyLikeArticleItem;

import java.util.List;

public class MyLikeArticleAdapter extends BaseQuickAdapter<MyLikeArticleItem, BaseViewHolder> {
    public MyLikeArticleAdapter(int layoutResId, @Nullable List<MyLikeArticleItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyLikeArticleItem item) {

    }
}
