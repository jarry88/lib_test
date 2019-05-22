package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreSearchItem;

import java.util.List;

public class StoreSearchResultAdapter extends BaseQuickAdapter<StoreSearchItem, BaseViewHolder> {


    public StoreSearchResultAdapter(int layoutResId, @Nullable List<StoreSearchItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreSearchItem item) {
        ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);
        Glide.with(mContext).load(item.storeAvatarUrl).into(imgStoreAvatar);

        helper.setText(R.id.tv_store_name, item.storeName);

        ImageView imgStoreFigure = helper.getView(R.id.img_store_figure);
        // SLog.info("uuuurl[%s]", Config.OSS_BASE_URL + item.storeFigureImage);
        Glide.with(mContext).load(Config.OSS_BASE_URL + "/" + item.storeFigureImage).centerCrop().into(imgStoreFigure);

        TextView tvDistance = helper.getView(R.id.tv_distance);
        if (item.distance < Constant.STORE_DISTANCE_THRESHOLD) {
            // 如果distance為0，則隱藏距離信息
            tvDistance.setVisibility(View.GONE);
        } else {
            String distanceText = item.distance + "km";
            tvDistance.setText(distanceText);
        }

        helper.setText(R.id.tv_shop_open_day, String.valueOf(item.shopDay));
        helper.setText(R.id.tv_goods_common_count, String.valueOf(item.goodsCommonCount));
        helper.setText(R.id.tv_like_count, String.valueOf(item.likeCount));
    }
}
