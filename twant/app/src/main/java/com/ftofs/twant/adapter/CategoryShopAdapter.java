package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.entity.CategoryShop;
import com.ftofs.twant.entity.CategoryShop;
import com.ftofs.twant.log.SLog;

import java.util.List;

/**
 * 店鋪分類adapter
 * @author zwm
 */

public class CategoryShopAdapter extends BaseQuickAdapter<CategoryShop, BaseViewHolder> {
    Context context;
    public CategoryShopAdapter(Context context, int layoutResId, @Nullable List<CategoryShop> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryShop categoryShop) {
        String coverUrl = Config.OSS_BASE_URL + "/" + categoryShop.coverUrl;
        SLog.info("coverUrl[%s]", coverUrl);
        ImageView coverImage = helper.getView(R.id.img_cover);
        Glide.with(context).load(coverUrl).into(coverImage);

        String shopText = context.getString(R.string.text_shop);
        String commodityText = context.getString(R.string.text_commodity);
        helper.setText(R.id.tv_shop_parent_name, categoryShop.shopParentName);

        String shopCountStr = String.format("%s %d", shopText, categoryShop.shopCount);
        helper.setText(R.id.tv_shop_count, shopCountStr);

        String commodityCountStr = String.format("%s %d", commodityText, categoryShop.commodityCount);
        helper.setText(R.id.tv_commodity_count, commodityCountStr);
    }
}
