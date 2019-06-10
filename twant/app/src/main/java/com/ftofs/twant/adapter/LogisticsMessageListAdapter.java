package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.LogisticsMessage;

import java.util.List;

public class LogisticsMessageListAdapter extends BaseQuickAdapter<LogisticsMessage, BaseViewHolder> {
    Context context;

    public LogisticsMessageListAdapter(Context context, int layoutResId, @Nullable List<LogisticsMessage> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LogisticsMessage item) {
        helper.setText(R.id.tv_time, item.time);
        helper.setText(R.id.tv_content, item.content);
    }
}
