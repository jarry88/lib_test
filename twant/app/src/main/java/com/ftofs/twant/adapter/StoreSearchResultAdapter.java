package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.StoreSearchItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

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
        Glide.with(mContext).load(Config.OSS_BASE_URL + "/" + item.storeFigureImage).into(imgStoreFigure);
    }
}
