package com.ftofs.twant.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.MyLikeStoreItem;

import java.util.List;

/**
 * 【我的點贊】店鋪
 * @author zwm
 */
public class MyLikeStoreAdapter extends BaseQuickAdapter<MyLikeStoreItem, BaseViewHolder> {
    public MyLikeStoreAdapter(int layoutResId, @Nullable List<MyLikeStoreItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyLikeStoreItem item) {
        ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);
        Glide.with(mContext).load(item.storeAvatar).centerCrop().into(imgStoreAvatar);

        helper.setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_store_address, item.storeAddress)
                .setText(R.id.tv_like_count, String.valueOf(item.likeCount));

        if (item.storeDistanceStr.equals("0")) {
            helper.setGone(R.id.tv_store_distance, false);
        } else {
            helper.setText(R.id.tv_store_distance, item.storeDistanceStr + "km");
        }
    }
}
