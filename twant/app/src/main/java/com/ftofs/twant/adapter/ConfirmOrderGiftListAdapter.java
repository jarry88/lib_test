package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.entity.GiftItem;

public class ConfirmOrderGiftListAdapter extends ViewGroupAdapter<GiftItem> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public ConfirmOrderGiftListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, GiftItem itemData) {

    }
}
