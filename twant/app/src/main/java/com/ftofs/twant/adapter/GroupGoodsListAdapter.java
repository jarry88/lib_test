package com.ftofs.twant.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.GroupGoods;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class GroupGoodsListAdapter extends BaseQuickAdapter<GroupGoods, BaseViewHolder> {
    Context context;
    public GroupGoodsListAdapter(Context context, int layoutResId, @Nullable List<GroupGoods> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GroupGoods item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageName)).centerCrop().into(goodsImage);

        helper.setText(R.id.tv_goods_name, item.goodsName)
                .setText(R.id.tv_goods_jingle, item.jingle)
                .setText(R.id.tv_group_price_label, item.joinedNum + "人拼團價")
                .setText(R.id.tv_group_price, StringUtil.formatPrice(context, item.groupPrice, 0))
                .setText(R.id.tv_original_price, StringUtil.formatPrice(context, item.goodsPrice, 0));
    }
}

