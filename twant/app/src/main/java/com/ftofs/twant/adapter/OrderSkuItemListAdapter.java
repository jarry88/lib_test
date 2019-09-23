package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.OrderSkuItem;
import com.ftofs.twant.util.StringUtil;

/**
 * 訂單列表里面每一項Sku的Adapter
 * @author zwm
 */
public class OrderSkuItemListAdapter extends ViewGroupAdapter<OrderSkuItem> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public OrderSkuItemListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, OrderSkuItem itemData) {
        ImageView goodsImage = itemView.findViewById(R.id.goods_image);
        Glide.with(itemView).load(itemData.imageSrc).centerCrop().into(goodsImage);
        TextView tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
        tvGoodsName.setText(itemData.goodsName);
        TextView tvSkuPrice = itemView.findViewById(R.id.tv_sku_price);
        tvSkuPrice.setText("$ " + StringUtil.formatPrice(context, itemData.goodsPrice, 1));
        TextView tvGoodsFullSpecs = itemView.findViewById(R.id.tv_goods_full_specs);
        tvGoodsFullSpecs.setText(itemData.goodsFullSpecs);
        TextView tvSkuCount = itemView.findViewById(R.id.tv_sku_count);
        tvSkuCount.setText("X " + itemData.buyNum);
    }
}


