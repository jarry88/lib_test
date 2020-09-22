package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.lib_net.model.Goods;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.entity.CrossBorderHomeItem;
import com.ftofs.twant.entity.CrossBorderNavItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.widget.GridLayout;
import com.gzp.lib_common.utils.SLog;

import java.util.ArrayList;
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

            RecyclerView rvBannerList = helper.getView(R.id.rv_banner_list);
            rvBannerList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderBannerAdapter bannerAdapter =
                    new CrossBorderBannerAdapter(context, R.layout.cross_border_banner_item, item.bannerItemList);
            rvBannerList.setAdapter(bannerAdapter);

            GridLayout glNavContainer = helper.getView(R.id.gl_nav_container);
            glNavContainer.removeAllViews();
            for (CrossBorderNavItem navItem : item.navItemList) {
                View navItemView = LayoutInflater.from(context).inflate(R.layout.cross_border_nav_item, glNavContainer, false);
                ImageView imgIcon = navItemView.findViewById(R.id.img_icon);
                SLog.info("img_icon[%s]", navItem.icon);
                Glide.with(context).load(StringUtil.normalizeImageUrl(navItem.icon)).centerCrop().into(imgIcon);
                ((TextView) navItemView.findViewById(R.id.tv_name)).setText(navItem.navName);

                glNavContainer.addView(navItemView);
            }

            RecyclerView rvShoppingZoneList = helper.getView(R.id.rv_shopping_zone_list);
            rvShoppingZoneList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderShoppingZoneAdapter shoppingZoneAdapter =
                    new CrossBorderShoppingZoneAdapter(context, R.layout.cross_border_shopping_zone_item, item.shoppingZoneList);
            rvShoppingZoneList.setAdapter(shoppingZoneAdapter);
            

            List<Goods> bargainGoodsList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                bargainGoodsList.add(new Goods());
            }
            RecyclerView rvBargainList = helper.getView(R.id.rv_bargain_list);
            rvBargainList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderActivityGoodsAdapter bargainGoodsAdapter =
                    new CrossBorderActivityGoodsAdapter(context, Constant.PROMOTION_TYPE_BARGAIN, R.layout.cross_border_activity_goods_item, bargainGoodsList);
            rvBargainList.setAdapter(bargainGoodsAdapter);

            List<Goods> groupGoodsList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                groupGoodsList.add(new Goods());
            }
            RecyclerView rvGroupList = helper.getView(R.id.rv_group_list);
            rvGroupList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CrossBorderActivityGoodsAdapter groupGoodsAdapter =
                    new CrossBorderActivityGoodsAdapter(context, Constant.PROMOTION_TYPE_GROUP, R.layout.cross_border_activity_goods_item, groupGoodsList);
            rvGroupList.setAdapter(groupGoodsAdapter);
        } else if (itemType == Constant.ITEM_TYPE_NORMAL) {

        }
    }
}
