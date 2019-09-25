package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.GiftVo;
import com.ftofs.twant.util.StringUtil;

public class ConformGiftAdapter extends ViewGroupAdapter<GiftVo> {
    int limitAmount;
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public ConformGiftAdapter(Context context, ViewGroup container, int itemLayoutId, int limitAmount) {
        super(context, container, itemLayoutId);
        this.limitAmount = limitAmount;
    }

    @Override
    public void bindView(int position, View itemView, GiftVo itemData) {
        ImageView goodsImage = itemView.findViewById(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.imageSrc)).centerCrop().into(goodsImage);

        setText(itemView, R.id.tv_goods_name, itemData.goodsName);
        String giftHint = String.format("(滿%d元贈，贈完為止) X %d",  limitAmount, itemData.giftNum);
        setText(itemView, R.id.tv_gift_hint, giftHint);
    }
}
