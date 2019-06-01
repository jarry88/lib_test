package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.Footprint;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class FootprintListAdapter extends BaseQuickAdapter<Footprint, BaseViewHolder> {
    Context context;
    public FootprintListAdapter(Context context, int layoutResId, @Nullable List<Footprint> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Footprint item) {
        int position = helper.getAdapterPosition();
        SLog.info("position[%d]", position);
        List<Footprint> footprintList = getData();
        if (position > 0) {
            // 查看前一項，日期或店鋪是否相同
            Footprint prevFootprint = footprintList.get(position - 1);

            boolean differentDate = false;
            // 如果日期與前一項相同，隱藏日期
            if (prevFootprint.date.equals(item.date)) {
                helper.setGone(R.id.tv_date, false);
            } else {
                helper.setGone(R.id.tv_date, true);
                differentDate = true;
            }

            // 如果店鋪與前一項相冊，隱藏店鋪名信息
            if (prevFootprint.storeId == item.storeId) {
                helper.setGone(R.id.ll_store_name_container, false);
            } else {
                helper.setGone(R.id.ll_store_name_container, true);
            }

            // 如果與前一項不同一天，強制顯示店鋪名信息
            if (differentDate) {
                helper.setGone(R.id.ll_store_name_container, true);
            }
        } else {
            // 如果是開始那一項，需要手動設置為VISIBLE，預防之前重用時設置為GONE
            helper.setGone(R.id.tv_date, true);
            helper.setGone(R.id.ll_store_name_container, true);
        }

        helper.setText(R.id.tv_date, item.date)
                .setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_goods_name, item.goodsName)
                .setText(R.id.tv_goods_jingle, item.jingle)
                .setText(R.id.tv_goods_price, StringUtil.formatPrice(context, item.price, 1));

        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(item.imageSrc).centerCrop().into(goodsImage);
    }
}
