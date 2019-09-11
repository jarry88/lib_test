package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreVoucherVo;

import java.util.List;

public class OrderVoucherListAdapter extends BaseQuickAdapter<StoreVoucherVo, BaseViewHolder> {
    public OrderVoucherListAdapter(int layoutResId, @Nullable List<StoreVoucherVo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreVoucherVo itemData) {
        helper.addOnClickListener(R.id.btn_receive_voucher_now);

        /*
        helper.setText(R.id.tv_template_price, StringUtil.formatPrice(mContext, itemData.templatePrice, 0));
        helper.setText(R.id.tv_limit_amount_text, itemData.limitAmountText);
        helper.setText(R.id.tv_store_name, itemData.storeName);
        helper.setText(R.id.tv_usable_client_type_text, "(" + itemData.usableClientTypeText + ")");
        String validTime = mContext.getString(R.string.text_valid_time) + ": " + itemData.useStartTime +
                "  -  " + itemData.useEndTime;
        helper.setText(R.id.tv_valid_time, validTime);

        if (itemData.isInUse) {
            helper.setGone(R.id.img_voucher_in_use_indicator, true);
        } else {
            helper.setGone(R.id.img_voucher_in_use_indicator, false);
        }
        */
    }
}
