package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.order.OrderDetailGoodsItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class OrderDetailGoodsAdapter extends BaseQuickAdapter<OrderDetailGoodsItem, BaseViewHolder> {
    Context context;
    String timesSign;

    public OrderDetailGoodsAdapter(Context context, int layoutResId, @Nullable List<OrderDetailGoodsItem> data) {
        super(layoutResId, data);
        this.context = context;

        timesSign = context.getString(R.string.times_sign);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailGoodsItem item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(item.imageSrc).centerCrop().into(goodsImage);
        helper.setText(R.id.tv_goods_name, item.goodsName);
        helper.setText(R.id.tv_goods_full_specs, item.goodsFullSpecs);
        helper.setText(R.id.tv_goods_price, StringUtil.formatPrice(context, item.goodsPrice, 0));
        helper.setText(R.id.tv_buy_item_amount, timesSign + " " + item.buyNum);
    }
}
