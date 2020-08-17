package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.SoldOutGoodsItem;
import com.ftofs.twant.util.StringUtil;

public class SoldOutGoodsAdapter extends ViewGroupAdapter<SoldOutGoodsItem> {
    Context context;

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public SoldOutGoodsAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);

        this.context = context;
    }

    @Override
    public void bindView(int position, View itemView, SoldOutGoodsItem itemData) {
        ImageView goodsImage = itemView.findViewById(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.goodsImage)).centerCrop().into(goodsImage);
        setText(itemView, R.id.tv_goods_name, itemData.goodsName);
        setText(itemView, R.id.tv_buy_num, "x" + itemData.buyNum);
        if (itemData.reason == SoldOutGoodsItem.REASON_SOLD_OUT) {
            itemData.reasonDesc = "該產品已售罄";
        } else if (itemData.reason == SoldOutGoodsItem.REASON_NOT_AVAILABLE) {
            itemData.reasonDesc = "該地區不支持配送";
        }
        setText(itemView, R.id.tv_reason, "原因：" + itemData.reasonDesc);
    }
}
