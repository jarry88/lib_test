package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
public class CategoryShopAdapter extends RecyclerView.Adapter<CategoryShopAdapter.ViewHolder> {
    private Context context;
    private List<CategoryShop> categoryShopList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImage;
        TextView tvShopParentName;
        TextView tvShopCount;
        TextView tvCommodityCount;

        public ViewHolder(View view) {
            super(view);
            coverImage = view.findViewById(R.id.img_cover);
            tvShopParentName = view.findViewById(R.id.tv_shop_parent_name);
            tvShopCount = view.findViewById(R.id.tv_shop_count);
            tvCommodityCount = view.findViewById(R.id.tv_commodity_count);
        }
    }

    public CategoryShopAdapter(Context context, List<CategoryShop> CategoryShopList) {
        this.context = context;
        this.categoryShopList = CategoryShopList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_shop_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CategoryShop categoryShop = categoryShopList.get(position);

        String coverUrl = Config.OSS_BASE_URL + "/" + categoryShop.coverUrl;
        SLog.info("coverUrl[%s]", coverUrl);
        Glide.with(context).load(coverUrl).placeholder(R.drawable.icon__bottom_bar_want).into(holder.coverImage);


        String shopText = context.getResources().getString(R.string.text_shop);
        String commodityText = context.getResources().getString(R.string.text_commodity);

        holder.tvShopParentName.setText(categoryShop.shopParentName);

        String shopCountStr = String.format("%s %d", shopText, categoryShop.shopCount);
        holder.tvShopCount.setText(shopCountStr);

        String commodityCountStr = String.format("%s %d", commodityText, categoryShop.commodityCount);
        holder.tvCommodityCount.setText(commodityCountStr);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (categoryShopList != null) {
            count = categoryShopList.size();
        }
        SLog.info("count[%d]", count);
        return count;
    }
}
