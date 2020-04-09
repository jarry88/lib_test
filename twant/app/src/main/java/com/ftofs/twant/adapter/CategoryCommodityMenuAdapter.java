package com.ftofs.twant.adapter;

import android.content.Context;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CategoryMenu;

import java.util.List;

public class CategoryCommodityMenuAdapter extends BaseQuickAdapter<CategoryMenu, BaseViewHolder> {
    Context context;
    boolean isExpanded;
    int twBlack;
    int twBlue;
    public CategoryCommodityMenuAdapter(Context context, int layoutResId, @Nullable List<CategoryMenu> data) {
        super(layoutResId, data);

        this.context = context;
        isExpanded = false;

        twBlack = context.getColor(R.color.tw_black);
        twBlue = context.getColor(R.color.tw_blue);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryMenu item) {
        if (item.selected) {
            helper.setGone(R.id.vw_indicator, true);
            helper.setTextColor(R.id.tv_category_name_chinese, twBlue);
        } else {
            helper.setGone(R.id.vw_indicator, false);
            helper.setTextColor(R.id.tv_category_name_chinese, twBlack);
        }
        helper.setText(R.id.tv_category_name_chinese, item.categoryNameChinese);
    }
}
