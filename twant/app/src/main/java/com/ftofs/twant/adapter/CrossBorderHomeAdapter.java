package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CrossBorderActivityGoods;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.entity.CrossBorderHomeItem;
import com.ftofs.twant.entity.CrossBorderNavItem;
import com.ftofs.twant.entity.CrossBorderNavPane;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.entity.Store;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BackgroundDrawable;
import com.ftofs.twant.widget.GridLayout;
import com.ftofs.twant.widget.SlantedWidget;
import com.gzp.lib_common.utils.SLog;

import java.util.List;

public class CrossBorderHomeAdapter extends BaseMultiItemQuickAdapter<CrossBorderHomeItem, BaseViewHolder> {
    Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CrossBorderHomeAdapter(Context context, List<CrossBorderHomeItem> data) {
        super(data);

        this.context = context;

        addItemType(Constant.ITEM_TYPE_HEADER, R.layout.cross_border_home_header);
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.cross_border_home_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CrossBorderHomeItem item) {
        int itemType = item.getItemType();

        if (itemType == Constant.ITEM_TYPE_HEADER) {
            helper.addOnClickListener(R.id.btn_goto_shopping_zone,
                    R.id.btn_view_more_bargain, R.id.btn_view_more_group, R.id.btn_view_more_best_store);


            // Banner圖列表
            RecyclerView rvBannerList = helper.getView(R.id.rv_banner_list);
            rvBannerList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderBannerAdapter bannerAdapter =
                    new CrossBorderBannerAdapter(context, R.layout.cross_border_banner_item, item.bannerItemList);
            bannerAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CrossBorderBannerItem bannerItem = item.bannerItemList.get(position);
                    Util.handleClickLink(bannerItem.linkTypeApp, bannerItem.linkValueApp);
                }
            });
            rvBannerList.setAdapter(bannerAdapter);


            // 導航區
            RecyclerView rvNavList = helper.getView(R.id.rv_nav_list);
            rvNavList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderNavAdapter navAdapter = new CrossBorderNavAdapter(context, R.layout.cross_border_nav_pane, item.navItemCount, item.navPaneList);
            navAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    int id = view.getId();
                    CrossBorderNavPane navPane = item.navPaneList.get(position);

                    int[] itemIdArr = new int[] {R.id.nav_1, R.id.nav_2, R.id.nav_3, R.id.nav_4, R.id.nav_5,
                            R.id.nav_6, R.id.nav_7, R.id.nav_8, R.id.nav_9, R.id.nav_10, };

                    for (int i = 0; i < itemIdArr.length; i++) {
                        int itemId = itemIdArr[i];
                        if (id == itemId) {
                            CrossBorderNavItem navItem = navPane.crossBorderNavItemList.get(0);
                            SLog.info("navItem[%s]", navItem);
                            Util.handleClickLink(navItem.linkTypeApp, navItem.linkValueApp);
                            break;
                        }
                    }
                }
            });
            rvNavList.setAdapter(navAdapter);


            // 購物專場
            RecyclerView rvShoppingZoneList = helper.getView(R.id.rv_shopping_zone_list);
            rvShoppingZoneList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderShoppingZoneAdapter shoppingZoneAdapter =
                    new CrossBorderShoppingZoneAdapter(context, R.layout.cross_border_shopping_zone_item, item.shoppingZoneList);
            rvShoppingZoneList.setAdapter(shoppingZoneAdapter);

            RecyclerView rvBargainList = helper.getView(R.id.rv_bargain_list);
            rvBargainList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderActivityGoodsAdapter bargainGoodsAdapter =
                    new CrossBorderActivityGoodsAdapter(context, Constant.PROMOTION_TYPE_BARGAIN, R.layout.cross_border_activity_goods_item, item.bargainGoodsList);
            bargainGoodsAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CrossBorderActivityGoods activityGoods = item.bargainGoodsList.get(position);
                    Util.startFragment(GoodsDetailFragment.newInstance(activityGoods.commonId, activityGoods.goodsId));
                }
            });
            rvBargainList.setAdapter(bargainGoodsAdapter);


            RecyclerView rvGroupList = helper.getView(R.id.rv_group_list);
            rvGroupList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderActivityGoodsAdapter groupGoodsAdapter =
                    new CrossBorderActivityGoodsAdapter(context, Constant.PROMOTION_TYPE_GROUP, R.layout.cross_border_activity_goods_item, item.groupGoodsList);
            groupGoodsAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    CrossBorderActivityGoods activityGoods = item.bargainGoodsList.get(position);
                    Util.startFragment(GoodsDetailFragment.newInstance(activityGoods.commonId, activityGoods.goodsId));
                }
            });
            rvGroupList.setAdapter(groupGoodsAdapter);

            helper.setVisible(R.id.rl_store2_container, item.storeList.size() > 1);

            if (item.storeList.size() > 0) {
                Store store = item.storeList.get(0);
                ImageView imgStore1Figure = helper.getView(R.id.img_store1_figure);
                Glide.with(context).load(StringUtil.normalizeImageUrl(store.storeFigureImage)).centerCrop().into(imgStore1Figure);
                helper.setText(R.id.tv_store1_name, store.storeName);
                helper.getView(R.id.rl_store1_container).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.startFragment(ShopMainFragment.newInstance(store.storeId));
                    }
                });
            }
            if (item.storeList.size() > 1) {
                Store store = item.storeList.get(1);
                ImageView imgStore1Figure = helper.getView(R.id.img_store2_figure);
                Glide.with(context).load(StringUtil.normalizeImageUrl(store.storeFigureImage)).centerCrop().into(imgStore1Figure);
                helper.setText(R.id.tv_store_name, store.storeName);
                helper.getView(R.id.rl_store1_container).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.startFragment(ShopMainFragment.newInstance(store.storeId));
                    }
                });
            }
        } else if (itemType == Constant.ITEM_TYPE_NORMAL) {
            helper.addOnClickListener(R.id.cl_container_left, R.id.cl_container_right);

            if (item.goodsPair == null) {
                return;
            }
            GoodsSearchItemPair goodsPair = item.goodsPair;
            if (goodsPair.left != null) {
                ImageView goodsImage = helper.getView(R.id.goods_image_left);
                Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.left.imageSrc)).centerCrop().into(goodsImage);

                ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar_left);
                if (StringUtil.isEmpty(goodsPair.left.storeAvatarUrl)) {
                    Glide.with(context).load(R.drawable.grey_default_avatar).centerCrop().into(imgStoreAvatar);
                } else {
                    Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.left.storeAvatarUrl)).centerCrop().into(imgStoreAvatar);
                }


                if (!StringUtil.isEmpty(goodsPair.left.nationalFlag)) {
                    ImageView imgGoodsNationalFlag = helper.getView(R.id.img_goods_national_flag_left);
                    //fitcenter塞到国旗圆框里
                    Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.left.nationalFlag)).centerInside().into(imgGoodsNationalFlag);
                    imgGoodsNationalFlag.setVisibility(View.VISIBLE);
                }

                helper.setText(R.id.tv_store_name_left, goodsPair.left.storeName);
                helper.setText(R.id.tv_goods_name_left, goodsPair.left.goodsName);
                TextView tvGoodsJingleLeft = helper.getView(R.id.tv_goods_jingle_left);
                if (StringUtil.isEmpty(goodsPair.left.jingle)) {
                    tvGoodsJingleLeft.setVisibility(View.GONE);
                } else {
                    tvGoodsJingleLeft.setText(goodsPair.left.jingle);
                    tvGoodsJingleLeft.setVisibility(View.VISIBLE);
                }

                TextView leftTextView = helper.getView(R.id.tv_goods_price_left);
                if (Util.noPrice(goodsPair.left.goodsModel)) {
                    UiUtil.toConsultUI(leftTextView);
                } else {
                    leftTextView.setText(StringUtil.formatPrice(context, goodsPair.left.price, 1,false));
                    UiUtil.toPriceUI(leftTextView,0);
                }


                helper.setGone(R.id.tv_freight_free_left, goodsPair.left.isFreightFree)
                        .setGone(R.id.tv_gift_left, goodsPair.left.hasGift);
//                        .setGone(R.id.tv_discount_left, item.left.hasDiscount)

                SlantedWidget slantedWidget = helper.getView(R.id.slanted_widget_left);
                if (goodsPair.left.showDiscountLabel) {
                    slantedWidget.setVisibility(View.VISIBLE);
                    slantedWidget.setDiscountInfo(context,goodsPair.left.extendPrice0, goodsPair.left.batchPrice0);
                } else {
                    slantedWidget.setVisibility(View.GONE);
                }

                helper.addOnClickListener(R.id.btn_goto_store_left, R.id.cl_container_left);

                // 設置是否顯示【跨城購】標籤
                helper.setGone(R.id.tv_cross_border_indicator_left, goodsPair.left.goodsModel == Constant.GOODS_TYPE_CROSS_BORDER);
            }
            // 設置右邊item的可見性

            if (goodsPair.right != null) {
                ImageView goodsImage = helper.getView(R.id.goods_image_right);
                Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.right.imageSrc)).centerCrop().into(goodsImage);

                ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar_right);
                if (StringUtil.isEmpty(goodsPair.right.storeAvatarUrl)) {
                    Glide.with(context).load(R.drawable.grey_default_avatar).centerCrop().into(imgStoreAvatar);
                } else {
                    Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.right.storeAvatarUrl)).centerCrop().into(imgStoreAvatar);
                }


                if (!StringUtil.isEmpty(goodsPair.right.nationalFlag)) {
                    ImageView imgGoodsNationalFlag = helper.getView(R.id.img_goods_national_flag_right);
                    Glide.with(context).load(StringUtil.normalizeImageUrl(goodsPair.right.nationalFlag)).centerInside().into(imgGoodsNationalFlag);
                    imgGoodsNationalFlag.setVisibility(View.VISIBLE);
                }

                helper.setText(R.id.tv_store_name_right, goodsPair.right.storeName);
                helper.setText(R.id.tv_goods_name_right, goodsPair.right.goodsName);
                TextView tvGoodsJingleRight = helper.getView(R.id.tv_goods_jingle_right);
                if (StringUtil.isEmpty(goodsPair.right.jingle)) {
                    tvGoodsJingleRight.setVisibility(View.GONE);
                } else {
                    tvGoodsJingleRight.setText(goodsPair.right.jingle);
                    tvGoodsJingleRight.setVisibility(View.VISIBLE);
                }

                TextView priceRight = helper.getView(R.id.tv_goods_price_right);
                if (Util.noPrice(goodsPair.right.goodsModel)) {
                    UiUtil.toConsultUI(priceRight);
                } else {
                    priceRight.setText(StringUtil.formatPrice(context, goodsPair.right.price, 1, false));
                    UiUtil.toPriceUI(priceRight, 0);
                }

                helper.setGone(R.id.tv_freight_free_right, goodsPair.right.isFreightFree)
                        .setGone(R.id.tv_gift_right, goodsPair.right.hasGift);
//                        .setGone(R.id.tv_discount_right, item.right.hasDiscount);

                SlantedWidget slantedWidget = helper.getView(R.id.slanted_widget_right);
                if (goodsPair.right.showDiscountLabel) {
                    slantedWidget.setVisibility(View.VISIBLE);
                    slantedWidget.setDiscountInfo(context, goodsPair.right.extendPrice0, goodsPair.right.batchPrice0);
                } else {
                    slantedWidget.setVisibility(View.GONE);
                }

                helper.addOnClickListener(R.id.btn_goto_store_right, R.id.cl_container_right);

                // 設置是否顯示【跨城購】標籤
                helper.setGone(R.id.tv_cross_border_indicator_right, goodsPair.right.goodsModel == Constant.GOODS_TYPE_CROSS_BORDER);
            }
            boolean rightHandSideVisible = (goodsPair.right != null);
            helper.setGone(R.id.cl_container_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_name_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_jingle_right, rightHandSideVisible)
                    .setGone(R.id.vw_right_bottom_separator, rightHandSideVisible)
                    .setGone(R.id.btn_goto_store_right, rightHandSideVisible)
                    .setGone(R.id.tv_freight_free_right, rightHandSideVisible)
                    .setGone(R.id.tv_goods_price_right, rightHandSideVisible);
            if (item.goodsPair != null) {
                TextView tvGoodsJingleRight = helper.getView(R.id.tv_goods_jingle_right);
                if (StringUtil.isEmpty(goodsPair.right.jingle)) {
                    tvGoodsJingleRight.setVisibility(View.GONE);
                } else {
                    tvGoodsJingleRight.setText(goodsPair.right.jingle);
                    tvGoodsJingleRight.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
