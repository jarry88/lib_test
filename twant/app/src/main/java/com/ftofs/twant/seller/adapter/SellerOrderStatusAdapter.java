package com.ftofs.twant.seller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.seller.entity.SellerOrderStatus;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

public class SellerOrderStatusAdapter extends ViewGroupAdapter<SellerOrderStatus> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public SellerOrderStatusAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, SellerOrderStatus itemData) {
        View vwTopLine = itemView.findViewById(R.id.vw_top_line);
        View vwBottomLine = itemView.findViewById(R.id.vw_bottom_line);

        if (position == 0) {
            vwTopLine.setVisibility(View.INVISIBLE);
        }

        if (position == getItemCount() - 1) {
            vwBottomLine.setVisibility(View.INVISIBLE);
        }

        TextView tvOrderStatusDesc = itemView.findViewById(R.id.tv_order_status_desc);
        tvOrderStatusDesc.setText(itemData.statusDesc);
        if (itemData.isLatestStatus) {
            tvOrderStatusDesc.setTextColor(context.getColor(R.color.tw_black));
        } else {
            tvOrderStatusDesc.setTextColor(Color.parseColor("#999999"));
        }

        TextView tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
        ImageView imgIndicator = itemView.findViewById(R.id.img_status_indicator);
        ViewGroup.LayoutParams layoutParams = imgIndicator.getLayoutParams();
        if (StringUtil.isEmpty(itemData.timestamp)) {
            tvTimestamp.setVisibility(View.GONE);

            imgIndicator.setImageResource(R.drawable.grey_point_4dp);
            layoutParams.width = Util.dip2px(context, 8);
            layoutParams.height = Util.dip2px(context, 8);
            imgIndicator.setLayoutParams(layoutParams);

            if (position >= 1 && getDataList().get(position - 1).isLatestStatus) {
                vwTopLine.setBackgroundColor(context.getColor(R.color.tw_blue));
            } else {
                vwTopLine.setBackgroundColor(context.getColor(R.color.tw_bright_grey));
            }
            vwBottomLine.setBackgroundColor(context.getColor(R.color.tw_bright_grey));
        } else {
            tvTimestamp.setText(itemData.timestamp);

            imgIndicator.setImageResource(R.drawable.icon_checked);
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            imgIndicator.setLayoutParams(layoutParams);

            vwTopLine.setBackgroundColor(context.getColor(R.color.tw_blue));
            vwBottomLine.setBackgroundColor(context.getColor(R.color.tw_blue));
        }
    }
}
