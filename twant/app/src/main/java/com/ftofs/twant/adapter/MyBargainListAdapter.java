package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.MyBargainListItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import java.util.List;

public class MyBargainListAdapter extends BaseQuickAdapter<MyBargainListItem, BaseViewHolder> {
    Context context;

    public MyBargainListAdapter(Context context, int layoutResId, @Nullable List<MyBargainListItem> data) {
        super(layoutResId, data);

        this.context = context;
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyBargainListItem item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageSrc)).centerCrop().into(goodsImage);
        helper.setText(R.id.tv_goods_name, item.goodsName)
                .setText(R.id.tv_goods_spec, item.goodsFullSpecs)
                .setText(R.id.tv_activity_time, item.startTime.substring(0, 10) + " - " + item.endTime.substring(0, 10))
                .setText(R.id.tv_open_price, StringUtil.formatPrice(context, item.openPrice, 0))
                .setText(R.id.tv_help_count, item.bargainTimes + "人幫砍")
                .setText(R.id.tv_bargain_down_price, "已砍掉" + StringUtil.formatPrice(context, item.bargainPrice, 0))
                .setText(R.id.tv_bargain_bottom_price, "底价：" + StringUtil.formatPrice(context, item.bottomPrice, 0));

        int position = helper.getAdapterPosition();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == 0) {
            layoutParams.topMargin = Util.dip2px(context, 8);
        } else {
            layoutParams.topMargin = 0;
        }
        helper.itemView.setLayoutParams(layoutParams);
    }
}
