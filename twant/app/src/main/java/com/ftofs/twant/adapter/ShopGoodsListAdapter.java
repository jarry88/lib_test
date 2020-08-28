package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.Util;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sxu.shadowdrawable.ShadowDrawable;

import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;


/**
 * 商店產品Adapter
 * @author zwm
 * 
 */
public class ShopGoodsListAdapter extends BaseMultiItemQuickAdapter<Goods, BaseViewHolder> {
    Context context;
    boolean isShopping = false;
    Typeface typeFace;

    public ShopGoodsListAdapter(Context context, @Nullable List<Goods> data) {
        super(data);
        this.context = context;

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.shop_goods_list_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint);
        addItemType(Constant.ITEM_TYPE_TITLE, R.layout.shop_commodity_title);
    }

    public ShopGoodsListAdapter(SupportActivity mActivity, List<Goods> goodsList, int resId) {
        super(goodsList);
        this.context = mActivity;
        this.isShopping = true;
        addItemType(Constant.ITEM_TYPE_NORMAL, resId);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint);
        addItemType(Constant.ITEM_TYPE_TITLE, R.layout.shop_commodity_title);
        this.typeFace = AssetsUtil.getTypeface(mActivity, "fonts/din_alternate_bold.ttf");
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods goods) {
        int itemViewType = helper.getItemViewType();
        if (itemViewType == Constant.ITEM_TYPE_NORMAL) {
            ShadowDrawable.setShadowDrawable(helper.itemView, Color.parseColor("#FFFFFF"), Util.dip2px(mContext, 3),
                    Color.parseColor("#19000000"), Util.dip2px(mContext, 3), 0, 0);
            if (isShopping) {
                helper.addOnClickListener(R.id.iv_goods_add);

                RoundedImageView goodsImage = helper.getView(R.id.img_goods_item);
                Glide.with(context).load(StringUtil.normalizeImageUrl(goods.imageUrl)).fitCenter().into(goodsImage);
                helper.setText(R.id.tv_goods_name, goods.name);
                helper.setText(R.id.tv_goods_comment, goods.jingle);
                TextView tvPrice = helper.getView(R.id.tv_goods_price);
                if (Util.noPrice(goods.goodsModel)) {
                    UiUtil.toConsultUI(tvPrice);
                    helper.getView(R.id.iv_goods_add).setVisibility(View.GONE);
                } else {
                    helper.getView(R.id.iv_goods_add).setVisibility(View.VISIBLE);


                    tvPrice.setText(StringUtil.formatPrice(context,  goods.price, 1,false));

                    tvPrice.setTypeface(typeFace);
                    UiUtil.toPriceUI(tvPrice,12);
                    if (goods.showDiscount) {
                        TextView tvOriginalPrice=helper.getView(R.id.tv_goods_original_price);
                        tvOriginalPrice.setVisibility(View.VISIBLE);
                        tvOriginalPrice.setText(StringUtil.formatPrice(mContext,goods.getOriginal(), 0, true));
                        tvOriginalPrice.setTypeface(typeFace);
                        // 原價顯示刪除線
                        tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }


//
            } else {
                ImageView goodsImage = helper.getView(R.id.img_goods);
                Glide.with(context).load(StringUtil.normalizeImageUrl(goods.imageUrl)).fitCenter().into(goodsImage);
                helper.setText(R.id.tv_goods_name, goods.name);
                if (Util.noPrice(goods.goodsModel)) {
                    helper.getView(R.id.btn_add_to_cart).setVisibility(View.GONE);
                    helper.setText(R.id.tv_goods_price_left, "詢價");
                } else {
                    helper.getView(R.id.btn_add_to_cart).setVisibility(View.VISIBLE);

                    helper.setText(R.id.tv_goods_price_left, StringUtil.formatPrice(context,  goods.price, 1,false));
                    helper.addOnClickListener(R.id.btn_add_to_cart);

                }
            }

        } else if (itemViewType == Constant.ITEM_TYPE_LOAD_END_HINT){
            helper.setText(R.id.tv_load_end_hint_content, " ");
            // 顯示即可，不用特別處理
        } else if (itemViewType == Constant.ITEM_TYPE_TITLE) {
            SLog.info("[%s]",goods.name);
            helper.setText(R.id.item_title, goods.name);
        }
    }
}

