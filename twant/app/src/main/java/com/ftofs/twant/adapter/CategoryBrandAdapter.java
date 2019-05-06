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
import com.ftofs.twant.entity.CategoryBrand;
import com.ftofs.twant.entity.CategoryShop;
import com.ftofs.twant.entity.CategoryShop;
import com.ftofs.twant.log.SLog;

import java.util.List;

/**
 * 品類分類adapter
 * @author zwm
 */
public class CategoryBrandAdapter extends RecyclerView.Adapter<CategoryBrandAdapter.ViewHolder> {
    private Context context;
    private List<CategoryBrand> categoryBrandList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView brandImage;
        TextView tvBrandNameChinese;
        TextView tvBrandNameEnglish;

        public ViewHolder(View view) {
            super(view);
            brandImage = view.findViewById(R.id.brand_image);
            tvBrandNameChinese = view.findViewById(R.id.tv_brand_name_chinese);
            tvBrandNameEnglish = view.findViewById(R.id.tv_brand_name_english);
        }
    }

    public CategoryBrandAdapter(Context context, List<CategoryBrand> categoryBrandList) {
        this.context = context;
        this.categoryBrandList = categoryBrandList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_brand_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CategoryBrand categoryBrand = categoryBrandList.get(position);

        String imageUrl = Config.OSS_BASE_URL + "/" + categoryBrand.imageUrl;
        SLog.info("imageUrl[%s]", imageUrl);
        Glide.with(context).load(imageUrl).placeholder(R.drawable.icon__bottom_bar_want).into(holder.brandImage);

        holder.tvBrandNameChinese.setText(categoryBrand.brandNameChinese);
        holder.tvBrandNameEnglish.setText(categoryBrand.brandNameEnglish);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (categoryBrandList != null) {
            count = categoryBrandList.size();
        }
        SLog.info("count[%d]", count);
        return count;
    }
}
