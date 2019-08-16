package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.WantedPostItem;

import java.util.List;

public class WantedJobListAdapter extends BaseQuickAdapter<WantedPostItem, BaseViewHolder> {


    public WantedJobListAdapter(int layoutResId, @Nullable List<WantedPostItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WantedPostItem item) {
        helper.setText(R.id.tv_title, String.format("%s | %s", item.postType, item.postTitle));
        helper.setText(R.id.tv_salary, String.format("%s%s/%s", item.salaryRange, item.currency, item.salaryType));
    }
}
