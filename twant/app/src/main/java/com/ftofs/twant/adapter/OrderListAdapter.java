package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.entity.OrderSkuItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 訂單列表Adapter
 * @author zwm
 */
public class OrderListAdapter extends BaseQuickAdapter<OrderItem, BaseViewHolder> {
    Context context;
    String currencyTypeSign;
    String timesSign;

    public OrderListAdapter(Context context, int layoutResId, List data) {
        super(layoutResId, data);
        this.context = context;
        currencyTypeSign = context.getResources().getString(R.string.currency_type_sign);
        timesSign = context.getResources().getString(R.string.times_sign);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderItem item) {
        // helper.setText(R.id.tv_area, item.getAreaName());
        LinearLayout llSkuItemContainer = helper.getView(R.id.ll_sku_item_container);
        llSkuItemContainer.removeAllViews();

        for (OrderSkuItem orderSkuItem : item.orderSkuItemList) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.common_sku_item, null, false);

            ImageView goodsImage = itemView.findViewById(R.id.goods_image);
            Glide.with(itemView).load(orderSkuItem.imageSrc).into(goodsImage);
            TextView tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
            tvGoodsName.setText(orderSkuItem.goodsName);
            TextView tvSkuPrice = itemView.findViewById(R.id.tv_sku_price);
            tvSkuPrice.setText(currencyTypeSign + " " + orderSkuItem.goodsPrice);
            TextView tvGoodsFullSpecs = itemView.findViewById(R.id.tv_goods_full_specs);
            tvGoodsFullSpecs.setText(orderSkuItem.goodsFullSpecs);
            TextView tvSkuCount = itemView.findViewById(R.id.tv_sku_count);
            tvSkuCount.setText(timesSign + " " + orderSkuItem.buyNum);

            llSkuItemContainer.addView(itemView);
        }

        helper.setText(R.id.tv_store_name, item.storeName);
        helper.setText(R.id.tv_order_status, item.ordersStateName);
        String skuCountText = context.getString(R.string.text_order_list_sku_count_template);
        skuCountText = String.format(skuCountText, item.orderSkuItemList.size());
        helper.setText(R.id.tv_sku_count, skuCountText);

        helper.setText(R.id.tv_orders_amount, StringUtil.formatPrice(context, item.ordersAmount, 1));


        if (item.showPayButton) {
            // 子View點擊事件
            helper.addOnClickListener(R.id.btn_pay_order);
        } else {
            helper.setGone(R.id.btn_pay_order, false);
        }


        // 將payId附加到View
        helper.getView(R.id.btn_pay_order).setTag(item.payId);
    }
}
