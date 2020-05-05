package com.ftofs.twant.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.SkuGalleryItem;

import java.util.List;

public class SkuImageListAdapter extends BaseQuickAdapter<SkuGalleryItem, BaseViewHolder> {
    Context context;

    public SkuImageListAdapter(Context context, int layoutResId, @Nullable List<SkuGalleryItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SkuGalleryItem item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(item.imageSrc).centerCrop().into(goodsImage);
    }
}
