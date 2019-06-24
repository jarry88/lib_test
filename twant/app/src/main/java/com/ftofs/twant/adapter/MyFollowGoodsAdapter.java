package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.MyFollowGoodsItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class MyFollowGoodsAdapter extends BaseQuickAdapter<MyFollowGoodsItem, BaseViewHolder> {
    public MyFollowGoodsAdapter(int layoutResId, @Nullable List<MyFollowGoodsItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFollowGoodsItem item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(mContext).load(item.imageSrc).centerCrop().into(goodsImage);

        helper.setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_goods_name, item.goodsName)
                .setText(R.id.tv_jingle, item.jingle)
                .setText(R.id.tv_price, StringUtil.formatPrice(mContext, item.price, 1))
                .setText(R.id.tv_follow_count, String.valueOf(item.goodsFavorite));
    }
}
