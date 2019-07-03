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
import com.ftofs.twant.util.Util;

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
            helper.setGone(R.id.rl_left_container, true);
            helper.setGone(R.id.rl_right_container, false);

            ImageView brandImageLeft = helper.getView(R.id.brand_image_left);
            Glide.with(context).load(imageUrl).centerCrop().into(brandImageLeft);

            helper.setText(R.id.tv_brand_name_chinese_left, categoryBrand.brandNameChinese);
            helper.setText(R.id.tv_brand_name_english_left, categoryBrand.brandNameEnglish);
        } else {
            // 显示左边，隐藏右边的控件
            helper.setGone(R.id.rl_right_container, true);
            helper.setGone(R.id.rl_left_container, false);

            ImageView brandImageRight = helper.getView(R.id.brand_image_right);
            Glide.with(context).load(imageUrl).centerCrop().into(brandImageRight);

            helper.setText(R.id.tv_brand_name_chinese_right, categoryBrand.brandNameChinese);
            helper.setText(R.id.tv_brand_name_english_right, categoryBrand.brandNameEnglish);
        }

        int itemCount = getItemCount();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        }
    }
}
