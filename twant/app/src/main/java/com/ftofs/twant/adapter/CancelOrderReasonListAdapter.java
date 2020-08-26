package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.CancelOrderReason;

public class CancelOrderReasonListAdapter extends ViewGroupAdapter<CancelOrderReason> {
    int twBlue;
    int twBlack;

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public CancelOrderReasonListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);

        twBlue = context.getColor(R.color.tw_blue);
        twBlack = context.getColor(R.color.tw_black);
    }

    @Override
    public void bindView(int position, View itemView, CancelOrderReason itemData) {
        TextView tvReasonDesc = itemView.findViewById(R.id.tv_reason_desc);
        ImageView imgIndicator = itemView.findViewById(R.id.img_indicator);
        tvReasonDesc.setText(itemData.text);

        if (itemData.selected) {
            tvReasonDesc.setTextColor(twBlue);
            imgIndicator.setVisibility(View.VISIBLE);
        } else {
            tvReasonDesc.setTextColor(twBlack);
            imgIndicator.setVisibility(View.GONE);
        }
    }
}

