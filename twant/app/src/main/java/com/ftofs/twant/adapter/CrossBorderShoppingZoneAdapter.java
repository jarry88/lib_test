package com.ftofs.twant.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.CrossBorderShoppingZoneItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class CrossBorderShoppingZoneAdapter extends BaseQuickAdapter<CrossBorderShoppingZoneItem, BaseViewHolder> {
    Context context;
    public CrossBorderShoppingZoneAdapter(Context context, int layoutResId, @Nullable List<CrossBorderShoppingZoneItem> data) {
        super(layoutResId, data);

        this.context = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CrossBorderShoppingZoneItem item) {
        ImageView imgShoppingZone = helper.getView(R.id.img_shopping_zone);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.appLogo)).centerCrop().into(imgShoppingZone);
    }
}
