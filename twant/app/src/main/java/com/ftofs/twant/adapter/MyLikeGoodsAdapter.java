package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.MyLikeGoodsItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class MyLikeGoodsAdapter extends BaseQuickAdapter<MyLikeGoodsItem, BaseViewHolder> {
    public MyLikeGoodsAdapter(int layoutResId, @Nullable List<MyLikeGoodsItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyLikeGoodsItem item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(mContext).load(item.imageSrc).centerCrop().into(goodsImage);

        helper.setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_goods_name, item.goodsName)
                .setText(R.id.tv_jingle, item.jingle)
                .setText(R.id.tv_price, StringUtil.formatPrice(mContext, item.price, 1,false))
                .setText(R.id.tv_like_count, String.valueOf(item.likeCount));
    }
}
