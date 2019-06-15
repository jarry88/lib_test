package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class GoodsVoucherListAdapter extends BaseQuickAdapter<StoreVoucher, BaseViewHolder> {
    public GoodsVoucherListAdapter(int layoutResId, @Nullable List<StoreVoucher> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreVoucher itemData) {
        helper.addOnClickListener(R.id.btn_receive_voucher_now);

        helper.setText(R.id.tv_template_price, StringUtil.formatPrice(mContext, itemData.templatePrice, 0));
        helper.setText(R.id.tv_limit_amount_text, itemData.limitAmountText);
        helper.setText(R.id.tv_store_name, itemData.storeName);
        helper.setText(R.id.tv_usable_client_type_text, "(" + itemData.usableClientTypeText + ")");
        String validTime = mContext.getString(R.string.text_valid_time) + ": " + itemData.useStartTime +
                "  -  " + itemData.useEndTime;
        helper.setText(R.id.tv_valid_time, validTime);

        if (itemData.memberIsReceive == Constant.ONE) {
            helper.setText(R.id.btn_receive_voucher_now, mContext.getString(R.string.text_been_received));
            helper.getView(R.id.rl_left_container).setBackgroundResource(R.drawable.grey_voucher);
            helper.getView(R.id.btn_receive_voucher_now).setBackgroundResource(R.drawable.grey_button);
        } else {
            helper.setText(R.id.btn_receive_voucher_now, mContext.getString(R.string.text_receive_now));
            helper.getView(R.id.rl_left_container).setBackgroundResource(R.drawable.pink_voucher);
            helper.getView(R.id.btn_receive_voucher_now).setBackgroundResource(R.drawable.pink_button);
        }
    }
}
