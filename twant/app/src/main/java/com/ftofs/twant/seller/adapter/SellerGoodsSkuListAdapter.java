package com.ftofs.twant.seller.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.seller.entity.SellerSkuListItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class SellerGoodsSkuListAdapter extends BaseQuickAdapter<SellerSkuListItem, BaseViewHolder> {
    Context context;

    public SellerGoodsSkuListAdapter(Context context, int layoutResId, @Nullable List<SellerSkuListItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerSkuListItem item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.goodsImage)).centerCrop().into(goodsImage);
        helper.setText(R.id.tv_goods_full_specs, item.goodsFullSpecs)
                .setText(R.id.tv_sku, "SKU：" + item.goodsId)
                .setText(R.id.tv_price, StringUtil.formatPrice(context, item.goodsPrice, 0))
                .setText(R.id.tv_goods_storage, "庫存：" + item.goodsStorage)
                .setText(R.id.tv_common_id, "商品編號：" + item.commonId);
    }
}
