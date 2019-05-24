package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ConfirmOrderSkuItem;
import com.ftofs.twant.entity.ConfirmOrderStoreItem;
import com.ftofs.twant.entity.ConfirmOrderSummaryItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import java.util.List;

public class ConfirmOrderStoreAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    Context context;
    String timesSign;

    public ConfirmOrderStoreAdapter(Context context, @Nullable List<MultiItemEntity> data) {
        super(data);
        addItemType(Constant.ITEM_VIEW_TYPE_COMMON, R.layout.confirm_order_store_item);
        addItemType(Constant.ITEM_VIEW_TYPE_SUMMARY, R.layout.confirm_order_summary_item);

        this.context = context;
        timesSign = context.getResources().getString(R.string.times_sign);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity multiItemEntity) {
        int itemViewType = helper.getItemViewType();

        if (itemViewType == Constant.ITEM_VIEW_TYPE_COMMON) {
            ConfirmOrderStoreItem item = (ConfirmOrderStoreItem) multiItemEntity;
            helper.setText(R.id.tv_store_name, item.storeName);
            helper.setText(R.id.tv_freight_amount, StringUtil.formatPrice(context, item.freightAmount, 0));

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
                TextView tvSkuPrice = skuItemView.findViewById(R.id.tv_sku_price);
                tvSkuPrice.setText(StringUtil.formatPrice(context, confirmOrderSkuItem.skuPrice, 0));

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, Util.dip2px(context, 15), 0, 0);
                llSkuItemContainer.addView(skuItemView, layoutParams);
            }
        } else {
            // 匯總數據
            ConfirmOrderSummaryItem item = (ConfirmOrderSummaryItem) multiItemEntity;
            helper.addOnClickListener(R.id.btn_change_pay_way);
            helper.setText(R.id.tv_pay_way, paymentTypeCodeToPayWayDesc(item.paymentTypeCode));
            helper.setText(R.id.tv_buy_item_amount, StringUtil.formatPrice(context, item.totalAmount, 0));
            helper.setText(R.id.tv_freight_amount, StringUtil.formatPrice(context, item.totalFreight, 0));
            helper.setText(R.id.tv_store_discount, "- " + StringUtil.formatPrice(context, item.storeDiscount, 0));
        }
    }

    private String paymentTypeCodeToPayWayDesc(String paymentTypeCode) {
        switch (paymentTypeCode) {
            case Constant.PAYMENT_TYPE_CODE_ONLINE:
                return context.getResources().getString(R.string.text_pay_online);
            case Constant.PAYMENT_TYPE_CODE_OFFLINE:
                return context.getResources().getString(R.string.text_pay_delivery);
            case Constant.PAYMENT_TYPE_CODE_CHAIN:
                return context.getResources().getString(R.string.text_pay_fetch);
            default:
                return null;
        }
    }
}
