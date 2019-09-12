package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.GiftItem;

/**
 * 確認訂單Sku贈品列表Adapter
 * @author zwm
 */
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
        setText(itemView, R.id.tv_goods_name, itemData.goodsName);
        String giftNum = String.format("X %d", itemData.giftNum);
        setText(itemView, R.id.tv_gift_num, giftNum);
    }
}
