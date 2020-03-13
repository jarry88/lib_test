package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreNavigationItem;

public class StoreNavigationAdapter extends ViewGroupAdapter<StoreNavigationItem> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public StoreNavigationAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, StoreNavigationItem itemData) {
        setText(itemView, R.id.tv_title, itemData.title);


        if (position == getItemCount() - 1) { // 最後一項隱藏分隔線
            itemView.setBackground(null);
        }
    }
}
