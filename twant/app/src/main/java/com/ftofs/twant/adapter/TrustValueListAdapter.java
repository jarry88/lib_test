package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.TrustValueItem;

import java.util.List;

/**
 * 真實值明細Adapter
 * @author zwm
 */
public class TrustValueListAdapter extends BaseQuickAdapter<TrustValueItem, BaseViewHolder> {
    public TrustValueListAdapter(int layoutResId, @Nullable List<TrustValueItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TrustValueItem item) {
        helper.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_content, item.content)
                .setText(R.id.tv_value, "+" + item.value)
                .setText(R.id.tv_timestamp, item.timestamp);
    }
}
