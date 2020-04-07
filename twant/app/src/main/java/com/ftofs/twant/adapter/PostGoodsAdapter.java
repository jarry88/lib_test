package com.ftofs.twant.adapter;

import android.graphics.Color;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.sxu.shadowdrawable.ShadowDrawable;

import java.util.List;

/**
 * 發表想要帖時，想要帖產品Adapter
 * @author zwm
 */
public class PostGoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    public PostGoodsAdapter(int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(mContext).load(StringUtil.normalizeImageUrl(item.imageUrl)).centerCrop().into(goodsImage);

        helper.setText(R.id.tv_goods_name, item.name)
            .setText(R.id.tv_goods_price, StringUtil.formatPrice(mContext,item.price, 0,false));

        ShadowDrawable.setShadowDrawable(helper.itemView, Color.parseColor("#FFFFFF"), Util.dip2px(mContext, 5),
                Color.parseColor("#19000000"), Util.dip2px(mContext, 5), 0, 0);
    }
}
