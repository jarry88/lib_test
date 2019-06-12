package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CategoryMenu;

import java.util.List;

public class CategoryCommodityMenuAdapter extends BaseQuickAdapter<CategoryMenu, BaseViewHolder> {
    Context context;
    boolean isExpanded;
    int twBlack;
    int twRed;
    public CategoryCommodityMenuAdapter(Context context, int layoutResId, @Nullable List<CategoryMenu> data) {
        super(layoutResId, data);

        this.context = context;
        isExpanded = false;

        twBlack = context.getColor(R.color.tw_black);
        twRed = context.getColor(R.color.tw_red);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryMenu item) {
        if (item.selected) {
            helper.setGone(R.id.vw_indicator, true);
            helper.setTextColor(R.id.tv_category_name_chinese, twRed);
        } else {
            helper.setGone(R.id.vw_indicator, false);
            helper.setTextColor(R.id.tv_category_name_chinese, twBlack);
        }

        helper.setText(R.id.tv_category_name_chinese, item.categoryNameChinese);
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}