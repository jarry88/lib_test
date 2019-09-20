package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.util.StringUtil;

/**
 * 訂單列表里面的訂單項
 * 跟店鋪一一對應
 * @author zwm
 */
public class OrderItemListAdapter extends ViewGroupAdapter<OrderItem> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public OrderItemListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, OrderItem itemData) {
        setText(itemView, R.id.tv_store_name, itemData.storeName);
        setText(itemView, R.id.tv_order_status, itemData.ordersStateName);
        String skuCountText = context.getString(R.string.text_order_list_sku_count_template);

        skuCountText = String.format(skuCountText, itemData.orderSkuItemList.size());
        setText(itemView, R.id.tv_sku_count, skuCountText);

        setText(itemView, R.id.tv_orders_amount, StringUtil.formatPrice(context, itemData.ordersAmount, 1));
    }
}

