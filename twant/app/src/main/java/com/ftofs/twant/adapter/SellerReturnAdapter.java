package com.ftofs.twant.adapter;

import android.graphics.Paint;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.seller.entity.SellerOrderRefundItem;
import com.ftofs.twant.util.StringUtil;

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
        helper.addOnClickListener(R.id.tv_orders_sn, R.id.btn_seller_refund_state,R.id.tv_refund_goods);
        if (item == null) {
            return;
        }
        helper.setText(R.id.tv_orders_sn, item.getOrdersSnText())
                .setText(R.id.tv_apply_time, item.getAddTime())
                .setText(R.id.tv_buyer, item.getNickName()+"("+item.getMemberName()+")")
                .setText(R.id.tv_refund_goods, String.valueOf(item.getGoodsCount())+"件")
                .setText(R.id.tv_refund_amount, StringUtil.formatPrice(mContext, item.getRefundAmount(), 0))
                .setText(R.id.tv_refund_sn, item.getRefundSnText())
                .setText(R.id.tv_refund_state, item.refundStateText)
                .setText(R.id.tv_refund_status, item.refundStateText)
                .setText(R.id.tv_seller_process_state, item.sellerStateText)
                .setText(R.id.tv_platform_process_state, item.adminStateText)
                .setText(R.id.btn_seller_refund_state, item.showSellerHandleText);
        ((TextView)helper.getView(R.id.tv_refund_goods)).setPaintFlags(Paint. UNDERLINE_TEXT_FLAG);
        ((TextView)helper.getView(R.id.tv_refund_goods)).getPaint().setAntiAlias(true);//抗锯齿;
        ((TextView)helper.getView(R.id.tv_orders_sn)).setPaintFlags(Paint. UNDERLINE_TEXT_FLAG);
        ((TextView)helper.getView(R.id.tv_refund_state)).setTextColor(mContext.getColor(item.getRefundState()<=2?R.color.tw_yellow:R.color.tw_green));
    }
}
