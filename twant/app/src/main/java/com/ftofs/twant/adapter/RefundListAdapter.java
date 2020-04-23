package com.ftofs.twant.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
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
                .setText(R.id.tv_goods_price_left, StringUtil.formatPrice(mContext, item.goodsPrice, 0,false))
                .setText(R.id.tv_buy_num, mContext.getString(R.string.times_sign) + " " + item.buyNum)
                .setText(R.id.tv_add_time, item.addTime)
                .setText(R.id.tv_refund_amount, StringUtil.formatPrice(mContext, item.goodsPayAmount, 0,false));

        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.goodsImage)).centerCrop().into(goodsImage);

        TextView btnCancelRefund = helper.getView(R.id.btn_cancel_refund);
        TextView btnReturnSend = helper.getView(R.id.btn_return_send);
        TextView btnViewRefundInfo = helper.getView(R.id.btn_view_refund_info);

        if (item.enableMemberCancel == 1) {
            btnCancelRefund.setVisibility(View.VISIBLE);
        } else {
            btnCancelRefund.setVisibility(View.GONE);
        }

        if (item.action == Constant.ACTION_REFUND) {
            btnCancelRefund.setText(R.string.text_cancel_refund);
            btnViewRefundInfo.setText(R.string.text_refund_detail);
        } else if (item.action == Constant.ACTION_RETURN) {
            btnCancelRefund.setText(R.string.text_cancel_return);
            btnViewRefundInfo.setText(R.string.text_return_detail);

            if (item.showMemberReturnShip == Constant.TRUE_INT) {
                btnReturnSend.setVisibility(View.VISIBLE);
            } else {
                btnReturnSend.setVisibility(View.GONE);
            }
        } else {
            btnCancelRefund.setText(R.string.text_cancel_complain);
            btnViewRefundInfo.setText(R.string.text_complain_detail);
        }

        if (item.action == Constant.ACTION_REFUND || item.action == Constant.ACTION_RETURN) {
            helper.setGone(R.id.ll_goods_info_container, true)
                    .setGone(R.id.ll_refund_amount_container, true);
        } else {
            helper.setGone(R.id.ll_goods_info_container, false)
                    .setGone(R.id.ll_refund_amount_container, false);
        }

        helper.addOnClickListener(R.id.btn_view_refund_info, R.id.btn_cancel_refund, R.id.btn_return_send);
    }
}
