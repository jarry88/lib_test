package com.ftofs.twant.seller.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreSortCriteriaAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.seller.entity.SellerOrderItem;
import com.ftofs.twant.seller.entity.SellerOrderSkuItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 賣家版訂單列表適配器
 * @author zwm
 */
public class SellerOrderAdapter extends BaseQuickAdapter<SellerOrderItem, BaseViewHolder> {
    Context context;

    public SellerOrderAdapter(Context context, int layoutResId, @Nullable List<SellerOrderItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerOrderItem item) {
        helper.addOnClickListener(R.id.tv_buyer);
        helper.setText(R.id.tv_orders_sn, item.ordersSnText)
                .setText(R.id.tv_create_time, item.createTime)
                .setText(R.id.tv_from, item.ordersFrom)
                .setText(R.id.tv_buyer, item.buyer)
                .setText(R.id.tv_order_status, item.ordersStateName);

        // 顯示商品列表
        LinearLayout llGoodsList = helper.getView(R.id.ll_goods_list);
        SellerOrderGoodsAdapter orderGoodsAdapter = new SellerOrderGoodsAdapter(context, llGoodsList, R.layout.seller_order_sku_item);
        orderGoodsAdapter.setData(item.goodsList);

        int totalGoodsCount = 0; // 商品總件數

        for (SellerOrderSkuItem goodsItem : item.goodsList) {
            totalGoodsCount += goodsItem.buyNum;
        }

        String orderInfo = String.format("共%d件商品，合計（%s）", totalGoodsCount, item.paymentName);

        helper.setText(R.id.tv_order_info, orderInfo)
            .setText(R.id.tv_amount, StringUtil.formatPrice(context, item.ordersAmount, 0))
            .setText(R.id.tv_freight_amount, StringUtil.formatPrice(context, item.freightAmount, 0));
    }
}
