package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.GiftItem;

/**
 * 訂單列表里贈品的Adapter
 * 現在是每個訂單返回一組贈品數組
 * @author zwm
 */
public class OrderGiftItemListAdapter extends ViewGroupAdapter<GiftItem> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public OrderGiftItemListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, GiftItem itemData) {
        setText(itemView, R.id.tv_goods_name, itemData.goodsName);
        setText(itemView, R.id.tv_gift_num, "X " + itemData.giftNum);
    }
}
