package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.entity.MyFriendListItem;

import java.util.List;

public class MyFriendListAdapter extends BaseQuickAdapter<MyFriendListItem, BaseViewHolder> {
    public MyFriendListAdapter(int layoutResId, @Nullable List<MyFriendListItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFriendListItem item) {

    }
}
