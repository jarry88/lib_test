package com.ftofs.twant.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CareerItem;

import java.util.List;

import androidx.annotation.Nullable;

public class CareerAdapter extends BaseQuickAdapter <CareerItem, BaseViewHolder> {
    public boolean editable =true;

    public CareerAdapter(int layoutResId, @Nullable List<CareerItem> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CareerItem item) {
        if (!editable) {
            helper.getView(R.id.tv_edit).setVisibility(View.GONE);
            helper.getView(R.id.icon_edit).setVisibility(View.GONE);
        }
        if (item.itemType < CareerItem.TYPE_CERTIFICATE) {
            helper.setText(R.id.tv_platform_name, item.platformName)
                    .setText(R.id.tv_start_time, String.format("%s-%s",item.toMonth(item.StartDateFormat),item.toMonth(item.EndDateFormat)))
                    .setText(R.id.tv_major, item.major)
                    .setText(R.id.tv_explain, item.Explain);
        } else {
            helper.getView(R.id.tv_platform_name).setVisibility(View.GONE);
            helper.getView(R.id.rv_career_head).setVisibility(View.GONE);
            helper.setText(R.id.tv_explain, item.Explain);
        }

    }
}
