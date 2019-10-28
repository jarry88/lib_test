package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreSortCriteriaItem;

public class StoreSortCriteriaAdapter extends ViewGroupAdapter<StoreSortCriteriaItem> {
    int twBlack;

    int twBlue;

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public StoreSortCriteriaAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);

        twBlack = context.getColor(R.color.tw_black);
        twBlue = context.getColor(R.color.tw_blue);
    }

    @Override
    public void bindView(int position, View itemView, StoreSortCriteriaItem itemData) {
        TextView tvName = itemView.findViewById(R.id.tv_name);
        ImageView imgStatusIndicator = itemView.findViewById(R.id.img_status_indicator);

        tvName.setText(itemData.name);
        if (itemData.selected) {
            tvName.setTextColor(twBlue);
            imgStatusIndicator.setImageResource(R.drawable.icon_checked);
        } else {
            tvName.setTextColor(twBlack);
            imgStatusIndicator.setImageResource(R.drawable.icon_cart_item_unchecked);
        }
    }
}
