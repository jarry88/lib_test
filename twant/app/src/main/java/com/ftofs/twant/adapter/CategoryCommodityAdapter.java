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
import com.ftofs.twant.entity.CategoryCommodity;
import com.ftofs.twant.entity.CategoryShop;
import com.ftofs.twant.entity.CategoryShop;
import com.ftofs.twant.log.SLog;

import java.util.List;

/**
 * 商品分類adapter
 * @author zwm
 */
public class CategoryCommodityAdapter extends RecyclerView.Adapter<CategoryCommodityAdapter.ViewHolder> {
    private Context context;
    private List<CategoryCommodity> categoryCommodityList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCommodity;
        TextView tvCommodityName;


        public ViewHolder(View view) {
            super(view);
            imgCommodity = view.findViewById(R.id.img_commodity);
            tvCommodityName = view.findViewById(R.id.tv_commodity_name);
        }
    }

    public CategoryCommodityAdapter(Context context, List<CategoryCommodity> categoryCommodityList) {
        this.context = context;
        this.categoryCommodityList = categoryCommodityList;
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
        CategoryCommodity categoryCommodity = categoryCommodityList.get(position);

        String imageUrl = Config.OSS_BASE_URL + "/" + categoryCommodity.imageUrl;
        SLog.info("imageUrl[%s]", imageUrl);
        Glide.with(context).load(imageUrl).placeholder(R.drawable.icon__bottom_bar_want).into(holder.imgCommodity);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (categoryCommodityList != null) {
            count = categoryCommodityList.size();
        }
        SLog.info("count[%d]", count);
        return count;
    }
}
