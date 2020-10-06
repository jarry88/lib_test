package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SlantedWidget;

import java.util.List;


/**
 * 店鋪內商品搜索結果Adapter
 * @author zwm
 */
public class ShopGoodsSearchResultAdapter extends BaseMultiItemQuickAdapter<GoodsSearchItemPair, BaseViewHolder> {
    Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ShopGoodsSearchResultAdapter(Context context, List<GoodsSearchItemPair> data) {
        super(data);

        this.context = context;
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.shop_goods_search_item_pair);
        addItemType(Constant.ITEM_TYPE_FOOTER, R.layout.cross_border_home_footer);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, GoodsSearchItemPair item) {
        int itemType = item.getItemType();
        if (itemType == Constant.ITEM_TYPE_NORMAL) {
            if (item.left != null) {
                ImageView goodsImage = helper.getView(R.id.goods_image_left);
                Glide.with(context).load(StringUtil.normalizeImageUrl(item.left.imageSrc)).centerCrop().into(goodsImage);


                if (!StringUtil.isEmpty(item.left.nationalFlag)) {
                    ImageView imgGoodsNationalFlag = helper.getView(R.id.img_goods_national_flag_left);
                    //fitcenter塞到国旗圆框里
                    Glide.with(context).load(StringUtil.normalizeImageUrl(item.left.nationalFlag)).centerInside().into(imgGoodsNationalFlag);
                    imgGoodsNationalFlag.setVisibility(View.VISIBLE);
                }

                helper.setText(R.id.tv_goods_name_left, item.left.goodsName);
                TextView tvGoodsJingleLeft = helper.getView(R.id.tv_goods_jingle_left);
                if (StringUtil.isEmpty(item.left.jingle)) {
                    tvGoodsJingleLeft.setVisibility(View.GONE);
                } else {
                    tvGoodsJingleLeft.setText(item.left.jingle);
                    tvGoodsJingleLeft.setVisibility(View.VISIBLE);
                }

                TextView leftTextView = helper.getView(R.id.tv_goods_price_left);
                if (Util.noPrice(item.left.goodsModel)) {
                    UiUtil.toConsultUI(leftTextView);
                } else {
                    leftTextView.setText(StringUtil.formatPrice(context, item.left.price, 1,false));
                    UiUtil.toPriceUI(leftTextView,0);
                }


                helper.setGone(R.id.tv_freight_free_left, item.left.isFreightFree)
                        .setGone(R.id.tv_gift_left, item.left.hasGift);
//                        .setGone(R.id.tv_discount_left, item.left.hasDiscount)

                SlantedWidget slantedWidget = helper.getView(R.id.slanted_widget_left);
                if (item.left.showDiscountLabel) {
                    slantedWidget.setVisibility(View.VISIBLE);
                    slantedWidget.setDiscountInfo(context,item.left.extendPrice0, item.left.batchPrice0);
                } else {
                    slantedWidget.setVisibility(View.GONE);
                }

                helper.addOnClickListener(R.id.cl_container_left);

                // 設置是否顯示【跨城購】標籤
                helper.setGone(R.id.tv_cross_border_indicator_left, item.left.tariffEnable == Constant.TRUE_INT);
            }


            // 設置右邊item的可見性
            if (item.right != null) {
                ImageView goodsImage = helper.getView(R.id.goods_image_right);
                Glide.with(context).load(StringUtil.normalizeImageUrl(item.right.imageSrc)).centerCrop().into(goodsImage);


                if (!StringUtil.isEmpty(item.right.nationalFlag)) {
                    ImageView imgGoodsNationalFlag = helper.getView(R.id.img_goods_national_flag_right);
                    Glide.with(context).load(StringUtil.normalizeImageUrl(item.right.nationalFlag)).centerInside().into(imgGoodsNationalFlag);
                    imgGoodsNationalFlag.setVisibility(View.VISIBLE);
                }

                helper.setText(R.id.tv_goods_name_right, item.right.goodsName);
                TextView tvGoodsJingleRight = helper.getView(R.id.tv_goods_jingle_right);
                if (StringUtil.isEmpty(item.right.jingle)) {
                    tvGoodsJingleRight.setVisibility(View.GONE);
                } else {
                    tvGoodsJingleRight.setText(item.right.jingle);
                    tvGoodsJingleRight.setVisibility(View.VISIBLE);
                }

                TextView priceRight = helper.getView(R.id.tv_goods_price_right);
                if (Util.noPrice(item.right.goodsModel)) {
                    UiUtil.toConsultUI(priceRight);
                } else {
                    priceRight.setText(StringUtil.formatPrice(context, item.right.price, 1, false));
                    UiUtil.toPriceUI(priceRight, 0);
                }

                helper.setGone(R.id.tv_freight_free_right, item.right.isFreightFree)
                        .setGone(R.id.tv_gift_right, item.right.hasGift);
//                        .setGone(R.id.tv_discount_right, item.right.hasDiscount);

                SlantedWidget slantedWidget = helper.getView(R.id.slanted_widget_right);
                if (item.right.showDiscountLabel) {
                    slantedWidget.setVisibility(View.VISIBLE);
                    slantedWidget.setDiscountInfo(context, item.right.extendPrice0, item.right.batchPrice0);
                } else {
                    slantedWidget.setVisibility(View.GONE);
                }

                helper.addOnClickListener(R.id.cl_container_right);

                // 設置是否顯示【跨城購】標籤
                helper.setGone(R.id.tv_cross_border_indicator_right, item.right.tariffEnable == Constant.TRUE_INT);
            } else {
                helper.setGone(R.id.tv_freight_free_right, false)
                        .setGone(R.id.tv_cross_border_indicator_right, false);
            }
            boolean rightHandSideVisible = (item.right != null);
            helper.setGone(R.id.cl_container_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_name_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_jingle_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_price_right, rightHandSideVisible);
            if (item.right != null) {
                TextView tvGoodsJingleRight = helper.getView(R.id.tv_goods_jingle_right);
                if (StringUtil.isEmpty(item.right.jingle)) {
                    tvGoodsJingleRight.setVisibility(View.GONE);
                } else {
                    tvGoodsJingleRight.setText(item.right.jingle);
                    tvGoodsJingleRight.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
