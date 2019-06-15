package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.GiftItem;

import java.util.List;

public class GoodsGiftAdapter extends BaseQuickAdapter<GiftItem, BaseViewHolder> {
    Context context;
    public GoodsGiftAdapter(Context context, int layoutResId, @Nullable List<GiftItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftItem item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(item.imageSrc).centerCrop().into(goodsImage);

        helper.setText(R.id.tv_goods_name, item.goodsName);
        String giftHint = "(贈完為止) *" + item.giftNum;
        helper.setText(R.id.tv_gift_hint, giftHint);
    }
}
