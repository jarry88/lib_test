package com.ftofs.twant.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.seller.entity.SellerOrderRefundItem;

import java.util.List;

import javax.annotation.Nullable;

/**
 * @author gzp
 */
public class SellerReturnAdapter extends BaseQuickAdapter<SellerOrderRefundItem, BaseViewHolder> {

    public SellerReturnAdapter(int layoutResId, @Nullable List<SellerOrderRefundItem> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerOrderRefundItem item) {
        helper.addOnClickListener(R.id.tv_buyer, R.id.btn_ship);
        if (item == null) {
            return;
        }
        helper.setText(R.id.tv_orders_sn, item.getOrdersSnText())
                .setText(R.id.tv_create_time, item.getAddTime())
                .setText(R.id.tv_buyer, item.getNickName())
                .setText(R.id.tv_order_status, item.getGoodsState());
    }
}
