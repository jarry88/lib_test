package com.ftofs.twant.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.lib_net.model.Goods;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class SellerOrderListAdapter extends BaseQuickAdapter<Goods, BaseViewHolder>{
    public SellerOrderListAdapter(int resId, List<Goods> data) {
        super(resId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        ((TextView)(helper.getView(R.id.tv_goods_name))).setText(item.name);
        ((TextView)(helper.getView(R.id.tv_goods_full_specs))).setText(item.jingle);
        ((TextView)(helper.getView(R.id.tv_sku_price))).setText(StringUtil.formatPrice(mContext,item.price,1));
        ((TextView)(helper.getView(R.id.tv_sku_count))).setText("X "+item.buyNum);
        ImageView imageView=helper.getView(R.id.goods_image);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.imageUrl)).centerCrop().into(imageView);
    }

}
