package com.ftofs.twant.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.AddrItem;
import com.ftofs.twant.widget.ScaledButton;

import java.util.List;

public class AddrListAdapter extends BaseQuickAdapter<AddrItem, BaseViewHolder> {
    public AddrListAdapter(int layoutResId, @Nullable List<AddrItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddrItem item) {
        helper.setText(R.id.tv_receiver_name, item.realName);
        helper.setText(R.id.tv_mobile, item.mobPhone);
        helper.setText(R.id.tv_addr, item.areaInfo + " " + item.address);
        ScaledButton scaledButton = helper.getView(R.id.img_default_addr_indicator);
        if (item.isDefault == 1) {
            scaledButton.setIconResource(R.drawable.icon_cart_item_checked);
        } else {
            scaledButton.setIconResource(R.drawable.icon_cart_item_unchecked);
        }

        helper.addOnClickListener(R.id.btn_use_this_addr)
                .addOnClickListener(R.id.img_default_addr_indicator)
                .addOnClickListener(R.id.btn_edit)
                .addOnClickListener(R.id.btn_delete);
    }
}
