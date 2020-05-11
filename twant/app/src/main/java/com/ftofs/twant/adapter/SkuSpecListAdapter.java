package com.ftofs.twant.adapter;

import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.SkuSpecViewItem;
import com.ftofs.twant.log.SLog;

import java.util.List;

public class SkuSpecListAdapter extends BaseQuickAdapter<SkuSpecViewItem, BaseViewHolder> {
    public SkuSpecListAdapter(int layoutResId, @Nullable List<SkuSpecViewItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SkuSpecViewItem item) {
        SLog.info("ITEM[%s]", item);
        TextView tvSpecs = helper.getView(R.id.tv_specs);

        if (item.status == SkuSpecViewItem.STATUS_LEFT) {
            tvSpecs.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            // tvSpecs.setTextSize(Constant.SKU_UNSELECTED_FONT_SIZE);
        } else if (item.status == SkuSpecViewItem.STATUS_CENTER) {
            tvSpecs.setGravity(Gravity.CENTER);
            // tvSpecs.setTextSize(Constant.SKU_SELECTED_FONT_SIZE);
        } else {
            tvSpecs.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // tvSpecs.setTextSize(Constant.SKU_UNSELECTED_FONT_SIZE);
        }
        tvSpecs.setText(item.goodsFullSpecs);
    }
}
