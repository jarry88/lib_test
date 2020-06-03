package com.ftofs.twant.seller.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class SellerSkuListAdapter extends BaseQuickAdapter<SellerSpecPermutation, BaseViewHolder> {
    Context context;

    public SellerSkuListAdapter(Context context, int layoutResId, @Nullable List<SellerSpecPermutation> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerSpecPermutation item) {
        helper.addOnClickListener(R.id.btn_edit);
        helper.setText(R.id.tv_spec_value_str, item.specValueString)
                .setText(R.id.tv_price, StringUtil.formatPrice(context, item.price, 0) + " MOP")
                .setText(R.id.tv_goods_storage, "庫存: " + item.storage)
                .setText(R.id.tv_reserved_storage, "預存庫存: " + item.reserved);

        if (StringUtil.isEmpty(item.goodsSN)) {
            item.goodsSN = "";
        }
        helper.setText(R.id.tv_goods_sn, "商品編號：" + item.goodsSN);
    }
}
