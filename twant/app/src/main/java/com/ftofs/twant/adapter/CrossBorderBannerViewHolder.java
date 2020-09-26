package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CrossBorderBannerItem;
import com.ftofs.twant.util.StringUtil;
import com.zhouwei.mzbanner.holder.MZViewHolder;

public class CrossBorderBannerViewHolder implements MZViewHolder<CrossBorderBannerItem> {
    private ImageView mImageView;
    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.cross_border_banner_item, null);
        mImageView = (ImageView) view.findViewById(R.id.img_banner);
        return view;
    }

    @Override
    public void onBind(Context context, int position, CrossBorderBannerItem data) {
        // 数据绑定
        Glide.with(context).load(StringUtil.normalizeImageUrl(data.image)).centerCrop().into(mImageView);
    }
}

