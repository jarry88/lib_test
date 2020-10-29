package com.ftofs.twant.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.util.List;

/**
 * 默认实现的图片适配器，图片加载需要自己实现
 */
public abstract class BannerRoundedImageAdapter<T> extends BannerAdapter<T, BannerRoundedImageHolder> {

    public BannerRoundedImageAdapter(List<T> mData) {
        super(mData);
    }

    @Override
    public BannerRoundedImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        RoundedImageView imageView = new RoundedImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerRoundedImageHolder(imageView);
    }

}

