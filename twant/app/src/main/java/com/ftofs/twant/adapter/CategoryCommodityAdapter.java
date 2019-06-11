package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.entity.CategoryCommodity;
import com.ftofs.twant.entity.CategoryCommodityRow;
import com.ftofs.twant.log.SLog;

import java.util.List;

/**
 * 商品分類adapter
 * @author zwm
 */
public class CategoryCommodityAdapter extends BaseQuickAdapter<CategoryCommodityRow, BaseViewHolder> {
    Context context;
    public CategoryCommodityAdapter(Context context, int layoutResId, @Nullable List<CategoryCommodityRow> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryCommodityRow categoryCommodityRow) {
        // 先全部隱藏
        helper.setGone(R.id.category_image1, false);
        helper.setGone(R.id.tv_category_name1, false);
        helper.setGone(R.id.category_image2, false);
        helper.setGone(R.id.tv_category_name2, false);
        helper.setGone(R.id.category_image3, false);
        helper.setGone(R.id.tv_category_name3, false);

        List<CategoryCommodity> categoryCommodityList = categoryCommodityRow.categoryCommodityList;
        int index = 0;
        for (CategoryCommodity categoryCommodity : categoryCommodityList) {
            String imageUrl = Config.OSS_BASE_URL + "/" + categoryCommodity.imageUrl;

            if (index == 0) {
                ImageView goodsImage1 = helper.getView(R.id.category_image1);
                Glide.with(context).load(imageUrl).centerCrop().into(goodsImage1);
                helper.setText(R.id.tv_category_name1, categoryCommodity.categoryName);
                helper.setGone(R.id.category_image1, true);
                helper.setGone(R.id.tv_category_name1, true);

                helper.addOnClickListener(R.id.category_image1)
                        .addOnClickListener(R.id.tv_category_name1);
            } else if (index == 1) {
                ImageView goodsImage2 = helper.getView(R.id.category_image2);
                Glide.with(context).load(imageUrl).centerCrop().into(goodsImage2);
                helper.setText(R.id.tv_category_name2, categoryCommodity.categoryName);
                helper.setGone(R.id.category_image2, true);
                helper.setGone(R.id.tv_category_name2, true);

                helper.addOnClickListener(R.id.category_image2)
                        .addOnClickListener(R.id.tv_category_name2);
            } else if (index == 2) {
                ImageView goodsImage3 = helper.getView(R.id.category_image3);
                Glide.with(context).load(imageUrl).centerCrop().into(goodsImage3);
                helper.setText(R.id.tv_category_name3, categoryCommodity.categoryName);
                helper.setGone(R.id.category_image3, true);
                helper.setGone(R.id.tv_category_name3, true);

                helper.addOnClickListener(R.id.category_image3)
                        .addOnClickListener(R.id.tv_category_name3);
            }
            ++index;
        }
    }
}
