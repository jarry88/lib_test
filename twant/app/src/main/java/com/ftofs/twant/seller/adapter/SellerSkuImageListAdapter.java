package com.ftofs.twant.seller.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;

import java.util.List;

public class SellerSkuImageListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Context context;
    public SellerSkuImageListAdapter(Context context, int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.btn_remove_image);

        ImageView imageView = helper.getView(R.id.image_view);
        Glide.with(context).load(item).centerCrop().into(imageView);
    }
}
