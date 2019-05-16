package com.ftofs.twant.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.domain.Area;

import java.util.List;

public class AreaPopupAdapter extends BaseQuickAdapter<Area, BaseViewHolder> {
    public AreaPopupAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Area item) {
        helper.setText(R.id.tv_area, item.getAreaName());
    }
}
