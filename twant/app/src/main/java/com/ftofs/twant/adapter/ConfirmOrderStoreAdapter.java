package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.ConfirmOrderSkuItem;
import com.ftofs.twant.entity.ConfirmOrderStoreItem;
import com.ftofs.twant.util.Util;

import java.util.List;

public class ConfirmOrderStoreAdapter extends BaseQuickAdapter<ConfirmOrderStoreItem, BaseViewHolder> {
    Context context;
    String timesSign;

    public ConfirmOrderStoreAdapter(Context context, int layoutResId, @Nullable List<ConfirmOrderStoreItem> data) {
        super(layoutResId, data);
        this.context = context;

        timesSign = context.getResources().getString(R.string.times_sign);
    }

    @Override
    protected void convert(BaseViewHolder helper, ConfirmOrderStoreItem item) {
        helper.setText(R.id.tv_store_name, item.storeName);

        LinearLayout llSkuItemContainer = helper.getView(R.id.ll_sku_item_container);
        for (ConfirmOrderSkuItem confirmOrderSkuItem : item.confirmOrderSkuItemList) {
            LinearLayout skuItemView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.confirm_order_sku_item, null, false);

            ImageView goodsImageView = skuItemView.findViewById(R.id.goods_image);
            Glide.with(goodsImageView).load(confirmOrderSkuItem.goodsImage).into(goodsImageView);
            TextView goodsName = skuItemView.findViewById(R.id.tv_goods_name);
            goodsName.setText(confirmOrderSkuItem.goodsName);
            TextView tvFullSpecs = skuItemView.findViewById(R.id.tv_goods_full_specs);
            tvFullSpecs.setText(confirmOrderSkuItem.goodsFullSpecs);
            TextView tvBuyNum = skuItemView.findViewById(R.id.tv_sku_count);
            String buyNum = timesSign + " " + confirmOrderSkuItem.buyNum;
            tvBuyNum.setText(buyNum);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, Util.dip2px(context, 15), 0, 0);
            llSkuItemContainer.addView(skuItemView, layoutParams);
        }
    }
}
