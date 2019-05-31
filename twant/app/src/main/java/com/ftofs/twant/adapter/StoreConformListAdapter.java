package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreConform;

public class StoreConformListAdapter extends ViewGroupAdapter<StoreConform> {

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public StoreConformListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, StoreConform itemData) {
        String title = String.format(context.getString(R.string.text_conform_title_template), itemData.limitAmount, itemData.conformPrice);
        String content = String.format(context.getString(R.string.text_conform_content_template), itemData.limitAmount, itemData.conformPrice);
        String validTime = context.getString(R.string.text_valid_time) + ": " + itemData.startTime + "  -  " + itemData.endTime;

        setText(itemView, R.id.tv_conform_title, title);
        setText(itemView, R.id.tv_conform_content, content);
        setText(itemView, R.id.tv_conform_valid_time, validTime);
    }
}
