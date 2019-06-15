package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.GoodsConformItem;

import java.util.List;

public class GoodsConformAdapter extends BaseQuickAdapter<GoodsConformItem, BaseViewHolder> {
    Context context;
    public GoodsConformAdapter(Context context, int layoutResId, @Nullable List<GoodsConformItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsConformItem item) {
        String conformDesc = context.getString(R.string.text_conform_desc_template);
        conformDesc = String.format(conformDesc, item.limitAmount);
        helper.setText(R.id.tv_conform_desc, conformDesc);
        String validTime = context.getString(R.string.text_valid_time) + ": " + item.startTime.substring(0, 10) + " - " + item.endTime.substring(0, 10);
        helper.setText(R.id.tv_valid_time, validTime);

        // 包郵
        if (item.isFreeFreight == 0) {
            helper.setGone(R.id.ll_free_freight_ind_container, false);
        }

        // 立減
        if (item.conformPrice > 0) {
            String instantDiscountDesc = context.getString(R.string.text_instant_discount_desc);
            instantDiscountDesc = String.format(instantDiscountDesc, item.conformPrice);
            helper.setText(R.id.tv_instant_discount_desc, instantDiscountDesc);
        } else {
            helper.setGone(R.id.ll_instant_discount_ind_container, false);
        }

        // 送券
        if (item.templateId > 0) {
            String presentVoucherDesc = context.getString(R.string.text_present_voucher_desc);
            presentVoucherDesc = String.format(presentVoucherDesc, item.templatePrice);
            helper.setText(R.id.tv_present_voucher_desc, presentVoucherDesc);
        } else {
            helper.setGone(R.id.ll_present_voucher_ind_container, false);
        }
    }
}

