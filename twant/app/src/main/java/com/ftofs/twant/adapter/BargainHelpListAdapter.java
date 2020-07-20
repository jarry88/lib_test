package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.BargainHelpListItem;
import com.ftofs.twant.util.StringUtil;

public class BargainHelpListAdapter extends ViewGroupAdapter<BargainHelpListItem> {
    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public BargainHelpListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
    }

    @Override
    public void bindView(int position, View itemView, BargainHelpListItem itemData) {
        ImageView imgAvatar = itemView.findViewById(R.id.img_avatar);
        Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.avatar)).into(imgAvatar);
        ((TextView) itemView.findViewById(R.id.tv_nickname)).setText(itemData.nickname);
        ((TextView) itemView.findViewById(R.id.tv_help_time)).setText(itemData.createTime);
        ((TextView) itemView.findViewById(R.id.tv_price)).setText(StringUtil.formatPrice(context, itemData.bargainPrice, 0));

        if (position == getItemCount() - 1) { // 最後一項，不顯示分隔線
            itemView.setBackground(null);
        }
    }
}
