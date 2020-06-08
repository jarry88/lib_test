package com.ftofs.twant.seller.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.seller.entity.SellerGoodsPicVo;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class SellerSkuImageListAdapter extends BaseQuickAdapter<SellerGoodsPicVo, BaseViewHolder> {
    Context context;
    int adapterIndex;


    public SellerSkuImageListAdapter(Context context, int layoutResId, int adapterIndex, @Nullable List<SellerGoodsPicVo> data) {
        super(layoutResId, data);

        this.context = context;
        this.adapterIndex = adapterIndex;
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerGoodsPicVo item) {
        helper.addOnClickListener(R.id.btn_remove_image);

        ImageView imageView = helper.getView(R.id.image_view);
        if (!StringUtil.isEmpty(item.absolutePath)) {
            Glide.with(context).load(item.absolutePath).centerCrop().into(imageView);
        } else {
            Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageName)).centerCrop().into(imageView);
        }
    }

    public int getAdapterIndex() {
        return adapterIndex;
    }
}
