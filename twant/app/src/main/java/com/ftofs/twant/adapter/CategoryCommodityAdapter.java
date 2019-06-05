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
import com.ftofs.twant.entity.CategoryCommodity;
import com.ftofs.twant.entity.CategoryShop;
import com.ftofs.twant.entity.CategoryShop;
import com.ftofs.twant.log.SLog;

import java.util.List;

/**
 * 商品分類adapter
 * @author zwm
 */
public class CategoryCommodityAdapter extends BaseQuickAdapter<CategoryCommodity, BaseViewHolder> {
    Context context;
    public CategoryCommodityAdapter(Context context, int layoutResId, @Nullable List<CategoryCommodity> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryCommodity categoryCommodity) {
        String imageUrl = Config.OSS_BASE_URL + "/" + categoryCommodity.imageUrl;
        SLog.info("imageUrl[%s]", imageUrl);

        ImageView imgCommodity = helper.getView(R.id.img_commodity);
        Glide.with(context).load(imageUrl).centerCrop().into(imgCommodity);
        helper.setText(R.id.tv_commodity_name, categoryCommodity.categoryName);
    }
}
