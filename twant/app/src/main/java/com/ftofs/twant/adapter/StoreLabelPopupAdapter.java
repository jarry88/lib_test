package com.ftofs.twant.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.domain.goods.Category;

import java.util.List;

public class StoreLabelPopupAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {
    public StoreLabelPopupAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Category item) {
        helper.setText(R.id.tv_area, item.getCategoryName());
    }
}
