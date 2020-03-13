package com.ftofs.twant.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.MyFollowStoreItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class MyFollowStoreAdapter extends MyFollowAdapter<MyFollowStoreItem, BaseViewHolder> {
    public MyFollowStoreAdapter(int layoutResId, @Nullable List<MyFollowStoreItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyFollowStoreItem item) {
        SLog.info("COUNT[%d]", getItemCount());

        ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);

        if(StringUtil.isEmpty(item.storeAvatarUrl)){
            Glide.with(mContext).load(R.drawable.default_store_avatar).centerCrop().into(imgStoreAvatar);

        }else{
            Glide.with(mContext).load(item.storeAvatarUrl).centerCrop().into(imgStoreAvatar);

        }
        Glide.with(mContext).load(item.storeFigureImageUrl).centerCrop().into((ImageView)helper.getView(R.id.img_store_figure));
        helper.setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_store_address, item.chainAreaInfo)
                .setText(R.id.tv_like_count, String.valueOf(item.collectCount))
        .setText(R.id.tv_store_class,item.className);

        if (item.distance.equals("0")) {
            helper.setGone(R.id.tv_store_distance, false);
        } else {
            helper.setText(R.id.tv_store_distance, item.distance + "km");
        }
        super.switchMode(helper, item);
    }
}

