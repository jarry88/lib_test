package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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

public class CategoryBrandAdapter extends BaseQuickAdapter<CategoryBrand, BaseViewHolder> {
    Context context;
    public CategoryBrandAdapter(Context context, int layoutResId, @Nullable List<CategoryBrand> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryBrand categoryBrand) {
        int position = helper.getAdapterPosition();
        int remainder = position % 8;

        // 品牌LOGO显示在左右还是右边的规则
        boolean showLeft = true;
        if (remainder == 1 || remainder == 4 || remainder >= 6) {
            showLeft = false;
        }

        String imageUrl = Config.OSS_BASE_URL + "/" + categoryBrand.imageUrl;
        SLog.info("imageUrl[%s]", imageUrl);

        if (showLeft) {
            // 显示左边，隐藏右边的控件
            helper.setGone(R.id.ll_left_container, true);
            helper.setGone(R.id.ll_right_container, false);

            ImageView brandImageLeft = helper.getView(R.id.brand_image_left);
            Glide.with(context).load(imageUrl).into(brandImageLeft);

            helper.setText(R.id.tv_brand_name_chinese_left, categoryBrand.brandNameChinese);
            helper.setText(R.id.tv_brand_name_english_left, categoryBrand.brandNameEnglish);
        } else {
            // 显示左边，隐藏右边的控件
            helper.setGone(R.id.ll_right_container, true);
            helper.setGone(R.id.ll_left_container, false);

            ImageView brandImageRight = helper.getView(R.id.brand_image_right);
            Glide.with(context).load(imageUrl).into(brandImageRight);

            helper.setText(R.id.tv_brand_name_chinese_right, categoryBrand.brandNameChinese);
            helper.setText(R.id.tv_brand_name_english_right, categoryBrand.brandNameEnglish);
        }
    }
}

/*
public class CategoryBrandAdapter extends RecyclerView.Adapter<CategoryBrandAdapter.ViewHolder> {
    private Context context;
    private List<CategoryBrand> categoryBrandList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llContainerLeft;
        LinearLayout llContainerRight;

        ImageView brandImageLeft;
        TextView tvBrandNameChineseLeft;
        TextView tvBrandNameEnglishLeft;
        ImageView brandImageRight;
        TextView tvBrandNameChineseRight;
        TextView tvBrandNameEnglishRight;

        public ViewHolder(View view) {
            super(view);

            llContainerLeft = view.findViewById(R.id.ll_left_container);
            llContainerRight = view.findViewById(R.id.ll_right_container);

            brandImageLeft = view.findViewById(R.id.brand_image_left);
            tvBrandNameChineseLeft = view.findViewById(R.id.tv_brand_name_chinese_left);
            tvBrandNameEnglishLeft = view.findViewById(R.id.tv_brand_name_english_left);
            brandImageRight = view.findViewById(R.id.brand_image_right);
            tvBrandNameChineseRight = view.findViewById(R.id.tv_brand_name_chinese_right);
            tvBrandNameEnglishRight = view.findViewById(R.id.tv_brand_name_english_right);
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
        int remainder = position % 8;

        // 品牌LOGO显示在左右还是右边的规则
        boolean showLeft = true;
        if (remainder == 1 || remainder == 4 || remainder >= 6) {
            showLeft = false;
        }
        CategoryBrand categoryBrand = categoryBrandList.get(position);

        String imageUrl = Config.OSS_BASE_URL + "/" + categoryBrand.imageUrl;
        SLog.info("imageUrl[%s]", imageUrl);

        if (showLeft) {
            // 显示左边，隐藏右边的控件
            holder.llContainerLeft.setVisibility(View.VISIBLE);
            holder.llContainerRight.setVisibility(View.GONE);

            Glide.with(context).load(imageUrl).into(holder.brandImageLeft);

            holder.tvBrandNameChineseLeft.setText(categoryBrand.brandNameChinese);
            holder.tvBrandNameEnglishLeft.setText(categoryBrand.brandNameEnglish);
        } else {
            // 显示左边，隐藏右边的控件
            holder.llContainerRight.setVisibility(View.VISIBLE);
            holder.llContainerLeft.setVisibility(View.GONE);

            Glide.with(context).load(imageUrl).into(holder.brandImageRight);

            holder.tvBrandNameChineseRight.setText(categoryBrand.brandNameChinese);
            holder.tvBrandNameEnglishRight.setText(categoryBrand.brandNameEnglish);
        }


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
*/

