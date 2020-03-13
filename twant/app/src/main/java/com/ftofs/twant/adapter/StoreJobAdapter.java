package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.WantedPostItem;

import java.util.List;

public class StoreJobAdapter extends BaseQuickAdapter<WantedPostItem, BaseViewHolder> {
    public StoreJobAdapter(int layoutResId, @Nullable List<WantedPostItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WantedPostItem item) {
        helper.setText(R.id.tv_position_title, item.postTitle)
                .setText(R.id.tv_position_type, item.postType)
                .setText(R.id.tv_salary, item.salaryRange + "/" + item.salaryType);
    }
}
