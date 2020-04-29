package com.ftofs.twant.seller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.seller.entity.SellerOrderSkuItem;
import com.ftofs.twant.util.StringUtil;

public class SellerOrderGoodsAdapter extends ViewGroupAdapter<SellerOrderSkuItem> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public SellerOrderGoodsAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, SellerOrderSkuItem itemData) {
        ImageView goodsImage = itemView.findViewById(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.goodsImage)).centerCrop().into(goodsImage);

        // tv_goods_full_specs  tv_price
        ((TextView) itemView.findViewById(R.id.tv_goods_name)).setText(itemData.goodsName);
        ((TextView) itemView.findViewById(R.id.tv_goods_full_specs)).setText(itemData.goodsFullSpecs);
        ((TextView) itemView.findViewById(R.id.tv_price)).setText(StringUtil.formatPrice(context, itemData.goodsPrice, 0));
        ((TextView) itemView.findViewById(R.id.tv_buy_num)).setText("X " + itemData.buyNum);

        itemView.findViewById(R.id.tv_discount).setVisibility(itemData.isTimeLimitedDiscount ? View.VISIBLE : View.GONE);
        itemView.findViewById(R.id.tv_discount).setVisibility(itemData.isTimeLimitedDiscount ? View.VISIBLE : View.GONE);
        itemView.findViewById(R.id.tv_discount).setVisibility(itemData.isTimeLimitedDiscount ? View.VISIBLE : View.GONE);
    }
}
