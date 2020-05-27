package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreGoodsPair;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.UiUtil;

public class StoreGoodsListAdapter extends ViewGroupAdapter<StoreGoodsPair> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public StoreGoodsListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
        addClickableChildrenId(R.id.ll_left_item_container, R.id.ll_right_item_container);
    }

    @Override
    public void bindView(int position, View itemView, StoreGoodsPair itemData) {
        // SLog.info("__position[%d]", position);
        if (itemData.leftItem != null) {
            setText(itemView, R.id.tv_left_goods_name, itemData.leftItem.goodsName);
            setText(itemView, R.id.tv_left_goods_jingle, itemData.leftItem.jingle);
            setText(itemView, R.id.tv_left_price, StringUtil.formatPrice(context, itemData.leftItem.price, 0,false));

            TextView leftPrice = itemView.findViewById(R.id.tv_left_price);
            UiUtil.toPriceUI(leftPrice,12);
            ImageView leftGoodsImage = itemView.findViewById(R.id.left_goods_image);
            Glide.with(context).load(itemData.leftItem.imageSrc).fitCenter().into(leftGoodsImage);

            ImageView leftGoodsProp = itemView.findViewById(R.id.left_goods_prop);
            if (itemData.type == StoreGoodsPair.TYPE_NEW) {
                leftGoodsProp.setImageResource(R.drawable.icon_goods_new);
            } else {
                leftGoodsProp.setImageResource(R.drawable.icon_goods_hot);
            }
        } else {
            ((ViewGroup) itemView.findViewById(R.id.ll_left_item_container)).removeAllViews();
        }

        if (itemData.rightItem != null) {
            setText(itemView, R.id.tv_right_goods_name, itemData.rightItem.goodsName);
            setText(itemView, R.id.tv_right_goods_jingle, itemData.rightItem.jingle);
            setText(itemView, R.id.tv_right_price, StringUtil.formatPrice(context, itemData.rightItem.price, 0,false));

            TextView rightPrice = itemView.findViewById(R.id.tv_right_price);
            UiUtil.toPriceUI(rightPrice,12);
            ImageView rightGoodsImage = itemView.findViewById(R.id.right_goods_image);
            Glide.with(context).load(itemData.rightItem.imageSrc).fitCenter().into(rightGoodsImage);

            ImageView rightGoodsProp = itemView.findViewById(R.id.right_goods_prop);
            if (itemData.type == StoreGoodsPair.TYPE_NEW) {
                rightGoodsProp.setImageResource(R.drawable.icon_goods_new);
            } else {
                rightGoodsProp.setImageResource(R.drawable.icon_goods_hot);
            }
        } else {
            ((ViewGroup) itemView.findViewById(R.id.ll_right_item_container)).removeAllViews();
        }
    }
}
