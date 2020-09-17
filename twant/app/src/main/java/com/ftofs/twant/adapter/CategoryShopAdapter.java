package com.ftofs.twant.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CategoryShop;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 商店分類adapter
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
        String coverUrl = StringUtil.normalizeImageUrl(categoryShop.coverUrl);
        SLog.info("coverUrl[%s]", coverUrl);
        ImageView coverImage = helper.getView(R.id.img_cover);
        Glide.with(context).load(coverUrl).centerCrop().into(coverImage);

        String shopText = "間";
        String commodityText = context.getString(R.string.text_goods);
        helper.setText(R.id.tv_shop_parent_name, categoryShop.shopParentName);

        String shopCountStr = String.format("%d%s", categoryShop.shopCount, shopText);
        helper.setText(R.id.tv_shop_count, shopCountStr);

        String commodityCountStr = String.format("%s %d", commodityText, categoryShop.commodityCount);
        helper.setText(R.id.tv_commodity_count, commodityCountStr);

        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        } else {
            layoutParams.bottomMargin = 0;
        }
    }
}
