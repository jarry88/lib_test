package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreVoucherVo;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class OrderVoucherListAdapter extends BaseQuickAdapter<StoreVoucherVo, BaseViewHolder> {
    String storeName;
    public OrderVoucherListAdapter(int layoutResId, String storeName, @Nullable List<StoreVoucherVo> data) {
        super(layoutResId, data);

        this.storeName = storeName;
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreVoucherVo itemData) {
        helper.setText(R.id.tv_template_price, StringUtil.formatPrice(mContext, itemData.price, 0));
        helper.setText(R.id.tv_limit_amount_text, itemData.limitText);
        helper.setText(R.id.tv_store_name, storeName);
        String validTime = mContext.getString(R.string.text_valid_time) + ": " + itemData.startTime +
                "  -  " + itemData.endTime;
        helper.setText(R.id.tv_valid_time, validTime);

        if (itemData.isInUse) {
            helper.setGone(R.id.img_voucher_in_use_indicator, true);
        } else {
            helper.setGone(R.id.img_voucher_in_use_indicator, false);
        }
    }
}
