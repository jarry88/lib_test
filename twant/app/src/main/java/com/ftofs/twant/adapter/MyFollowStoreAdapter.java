package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.MyFollowStoreItem;
import com.ftofs.twant.log.SLog;

import java.util.List;

public class MyFollowStoreAdapter extends BaseQuickAdapter<MyFollowStoreItem, BaseViewHolder> {
    public MyFollowStoreAdapter(int layoutResId, @Nullable List<MyFollowStoreItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFollowStoreItem item) {
        SLog.info("COUNT[%d]", getItemCount());

        ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);
        Glide.with(mContext).load(item.storeAvatarUrl).centerCrop().into(imgStoreAvatar);

        helper.setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_store_address, item.chainAreaInfo)
                .setText(R.id.tv_like_count, String.valueOf(item.collectCount));

        if (item.distance.equals("0")) {
            helper.setGone(R.id.tv_store_distance, false);
        } else {
            helper.setText(R.id.tv_store_distance, item.distance + "km");
        }
    }
}

