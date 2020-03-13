package com.ftofs.twant.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.MyLikeStoreItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 【我的讚想】商店
 * @author zwm
 */
public class MyLikeStoreAdapter extends BaseQuickAdapter<MyLikeStoreItem, BaseViewHolder> {
    public MyLikeStoreAdapter(int layoutResId, @Nullable List<MyLikeStoreItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyLikeStoreItem item) {
        ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);
        SLog.info("item.storeAvatar[%s]", item.storeAvatar);
        if(!StringUtil.isUrlString(item.storeAvatar) || item.storeAvatar.equals("https://192.168.5.29/public/img/default_store_avatar.png")){
            Glide.with(mContext).load(R.drawable.default_store_avatar).centerCrop().into(imgStoreAvatar);
        }else{
            Glide.with(mContext).load(item.storeAvatar).centerCrop().into(imgStoreAvatar);
        }
        Glide.with(mContext).load(item.storeFigureImageUrl).centerCrop().into((ImageView) helper.getView(R.id.img_store_figure));
        helper.setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_store_address, item.storeAddress)
                .setText(R.id.tv_like_count, String.valueOf(item.likeCount))
                .setText(R.id.tv_store_class, item.className);

        if (item.storeDistanceStr.equals("0")) {
            helper.setGone(R.id.tv_store_distance, false);
        } else {
            helper.setText(R.id.tv_store_distance, item.storeDistanceStr + "km");
        }
    }
}
