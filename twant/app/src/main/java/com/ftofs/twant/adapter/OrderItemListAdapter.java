package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.util.StringUtil;

/**
 * 訂單列表里面的訂單項
 * 跟店鋪一一對應
 * @author zwm
 */
public class OrderItemListAdapter extends ViewGroupAdapter<OrderItem> {
    boolean showPayButton;

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     * @param showPayButton 是否顯示支付按鈕
     */
    public OrderItemListAdapter(Context context, ViewGroup container, int itemLayoutId, boolean showPayButton) {
        super(context, container, itemLayoutId);

        addClickableChildrenId(R.id.btn_cancel_order, R.id.btn_buy_again, R.id.btn_view_logistics, R.id.btn_order_comment);
        this.showPayButton = showPayButton;
    }

    @Override
    public void bindView(int position, View itemView, OrderItem itemData) {
        String skuCountText = context.getString(R.string.text_order_list_sku_count_template);
        skuCountText = String.format(skuCountText, itemData.orderSkuItemList.size());

        setText(itemView, R.id.tv_store_name, itemData.storeName);
        setText(itemView, R.id.tv_order_status, itemData.ordersStateName);
        setText(itemView, R.id.tv_sku_count, skuCountText);
        setText(itemView, R.id.tv_orders_amount, StringUtil.formatPrice(context, itemData.ordersAmount, 1));

        if (itemData.showMemberCancel) {
            itemView.findViewById(R.id.btn_cancel_order).setVisibility(View.VISIBLE);
        } else {
            itemView.findViewById(R.id.btn_cancel_order).setVisibility(View.GONE);
        }

        if (itemData.showMemberBuyAgain) {
            itemView.findViewById(R.id.btn_buy_again).setVisibility(View.VISIBLE);
        } else {
            itemView.findViewById(R.id.btn_buy_again).setVisibility(View.GONE);
        }

        if (itemData.showShipSearch) {
            itemView.findViewById(R.id.btn_view_logistics).setVisibility(View.VISIBLE);
        } else {
            itemView.findViewById(R.id.btn_view_logistics).setVisibility(View.GONE);
        }

        if (itemData.showEvaluation) {
            itemView.findViewById(R.id.btn_order_comment).setVisibility(View.VISIBLE);
        } else {
            itemView.findViewById(R.id.btn_order_comment).setVisibility(View.GONE);
        }

        LinearLayout llSkuItemContainer = itemView.findViewById(R.id.ll_sku_item_container);
        OrderSkuItemListAdapter adapter = new OrderSkuItemListAdapter(context, llSkuItemContainer, R.layout.common_sku_item);
        adapter.setData(itemData.orderSkuItemList);

        if (!showPayButton && (position == getItemCount() - 1)) { // 如果不顯示支付按鈕，并且是最后一項，則隱藏分隔線
            itemView.findViewById(R.id.vw_separator).setVisibility(View.GONE);
        }
    }
}

