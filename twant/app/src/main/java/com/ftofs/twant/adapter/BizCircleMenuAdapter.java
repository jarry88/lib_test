package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.BizCircleItem;

import java.util.List;


/**
 * 商圈菜單
 * @author zwm
 */
public class BizCircleMenuAdapter extends BaseQuickAdapter<BizCircleItem, BaseViewHolder> {


    public BizCircleMenuAdapter(int layoutResId, @Nullable List<BizCircleItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BizCircleItem item) {
        helper.setText(R.id.tv_name, item.name);
    }
}
