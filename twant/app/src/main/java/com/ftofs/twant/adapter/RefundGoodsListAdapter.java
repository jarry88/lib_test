package com.ftofs.twant.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.RefundGoodsItem;
import com.ftofs.twant.util.StringUtil;

/**
 * 退款、退貨、投訴商品列表Adapter
 * @author zwm
 */
public class RefundGoodsListAdapter extends ViewGroupAdapter<RefundGoodsItem> {
    Context context;
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public RefundGoodsListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);

        this.context = context;
    }

    @Override
    public void bindView(int position, View itemView, RefundGoodsItem itemData) {
        ImageView goodsImage = itemView.findViewById(R.id.goods_image);
        Glide.with(context).load(itemData.goodsImageUrl).centerCrop().into(goodsImage);

        TextView tvGoodsName = itemView.findViewById(R.id.tv_goods_name);
        tvGoodsName.setText(itemData.goodsName);

        TextView tvGoodsFullSpecs = itemView.findViewById(R.id.tv_goods_full_specs);
        tvGoodsFullSpecs.setText(itemData.goodsFullSpecs);

        TextView tvPrice = itemView.findViewById(R.id.tv_price);
        tvPrice.setText(StringUtil.formatPrice(context, itemData.price, 0));

        TextView tvGoodsNum = itemView.findViewById(R.id.tv_goods_num);
        tvGoodsNum.setText(context.getString(R.string.times_sign) + " " + itemData.goodsNum);
    }
}
