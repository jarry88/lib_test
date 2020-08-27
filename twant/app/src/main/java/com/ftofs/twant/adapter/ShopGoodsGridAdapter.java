package com.ftofs.twant.adapter;

import android.content.Context;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.GoodsPair;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.Util;

import java.util.List;

public class ShopGoodsGridAdapter extends BaseMultiItemQuickAdapter<GoodsPair, BaseViewHolder> {
    Context context;

    public void setTitle(String title) {
        this.title = title;
    }

    String title;
    public ShopGoodsGridAdapter(Context context, @Nullable List<GoodsPair> data) {
        super(data);
        this.context = context;

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.shop_goods_grid_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint);
        addItemType(Constant.ITEM_TYPE_TITLE, R.layout.shop_commodity_title);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsPair goodsPair) {
        int itemViewType = helper.getItemViewType();

        if (itemViewType == Constant.ITEM_TYPE_NORMAL) {
            if (goodsPair.leftGoods != null) {
                Goods leftGoods = goodsPair.leftGoods;
                ImageView goodsImage = helper.getView(R.id.img_left_goods);
                Glide.with(context).load(leftGoods.imageUrl).fitCenter().into(goodsImage);
                helper.setText(R.id.tv_left_goods_name, leftGoods.name);
                TextView leftPrice=helper.getView(R.id.tv_left_goods_price);
                if (Util.noPrice(leftGoods.goodsModel)) {
                    helper.getView(R.id.btn_add_to_cart_left).setVisibility(View.GONE);
                    UiUtil.toConsultUI(leftPrice);
                } else {
                    helper.getView(R.id.btn_add_to_cart_left).setVisibility(View.VISIBLE);
                    leftPrice.setText(StringUtil.formatPrice(context,  leftGoods.price, 1,false));
                    UiUtil.toPriceUI(leftPrice,12);

                }
                helper.addOnClickListener(R.id.img_left_goods,R.id.btn_add_to_cart_left);
            }
            if (goodsPair.rightGoods != null) {
                Goods rightGoods = goodsPair.rightGoods;
                ImageView goodsImage = helper.getView(R.id.img_right_goods);
                Glide.with(context).load(rightGoods.imageUrl).fitCenter().into(goodsImage);
                TextView rightPrice = helper.getView(R.id.tv_right_goods_price);
                if (Util.noPrice(rightGoods.goodsModel)) {
                    UiUtil.toConsultUI(rightPrice);
                    helper.getView(R.id.btn_add_to_cart_right).setVisibility(View.GONE);
                } else {
                    rightPrice.setText( StringUtil.formatPrice(context, rightGoods.price, 1,false));
                    helper.getView(R.id.btn_add_to_cart_right).setVisibility(View.VISIBLE);

                    UiUtil.toPriceUI(rightPrice,12);
                }
                helper.setText(R.id.tv_right_goods_name, rightGoods.name);

                helper.setGone(R.id.img_right_goods, true)
                        .setVisible(R.id.ll_right_goods_container, true);

                helper.addOnClickListener(R.id.btn_add_to_cart_right,R.id.btn_add_to_cart_right);
            } else {
                helper.setGone(R.id.img_right_goods, false)
                        .setVisible(R.id.ll_right_goods_container, false);
            }
        }
        else if (itemViewType == Constant.ITEM_TYPE_LOAD_END_HINT){ // 加載完成的提示，顯示即可，不用特別處理
            helper.setText(R.id.tv_load_end_hint_content, " ");
        } else if (itemViewType == Constant.ITEM_TYPE_TITLE) {
            helper.setText(R.id.item_title, goodsPair.getItemTitle());
        }

    }
}

