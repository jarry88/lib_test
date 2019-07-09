package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.RefundItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class RefundListAdapter extends BaseQuickAdapter<RefundItem, BaseViewHolder> {
    public RefundListAdapter(int layoutResId, @Nullable List<RefundItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RefundItem item) {
        helper.setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_order_status, item.orderStatus)
                .setText(R.id.tv_goods_name, item.goodsName)
                .setText(R.id.tv_goods_full_specs, item.goodsFullSpecs)
                .setText(R.id.tv_goods_price, StringUtil.formatPrice(mContext, item.goodsPrice, 0))
                .setText(R.id.tv_buy_num, mContext.getString(R.string.times_sign) + " " + item.buyNum)
                .setText(R.id.tv_add_time, item.addTime)
                .setText(R.id.tv_refund_amount, StringUtil.formatPrice(mContext, item.goodsPayAmount, 0));

        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.goodsImage)).centerCrop().into(goodsImage);

        if (item.enableMemberCancel == 1) {
            helper.setGone(R.id.btn_cancel_refund, true);
        }

        helper.addOnClickListener(R.id.btn_view_refund_info, R.id.btn_cancel_refund);
    }
}
