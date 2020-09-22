package com.ftofs.twant.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class CrossBorderBannerAdapter extends BaseQuickAdapter<CrossBorderBannerItem, BaseViewHolder> {
    Context context;

    public CrossBorderBannerAdapter(Context context, int layoutResId, @Nullable List<CrossBorderBannerItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CrossBorderBannerItem item) {
        ImageView imgBanner = helper.getView(R.id.img_banner);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.image)).centerCrop().into(imgBanner);
    }
}
