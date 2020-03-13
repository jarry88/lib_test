package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.sxu.shadowdrawable.ShadowDrawable;

import java.util.List;


/**
 * 商店產品Adapter
 * @author zwm
 * 
 */
public class ShopGoodsListAdapter extends BaseMultiItemQuickAdapter<Goods, BaseViewHolder> {
    Context context;

    public ShopGoodsListAdapter(Context context, @Nullable List<Goods> data) {
        super(data);
        this.context = context;

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.shop_goods_list_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods goods) {
        int itemViewType = helper.getItemViewType();
        if (itemViewType == Constant.ITEM_TYPE_NORMAL) {
            helper.addOnClickListener(R.id.btn_add_to_cart);
            ShadowDrawable.setShadowDrawable(helper.itemView, Color.parseColor("#FFFFFF"), Util.dip2px(mContext, 3),
                    Color.parseColor("#19000000"), Util.dip2px(mContext, 3), 0, 0);
            ImageView goodsImage = helper.getView(R.id.img_goods);
            Glide.with(context).load(goods.imageUrl).centerCrop().into(goodsImage);
            helper.setText(R.id.tv_goods_name, goods.name);
            helper.setText(R.id.tv_goods_price, StringUtil.formatPrice(context, (float) goods.price, 1,false));
        } else if (itemViewType == Constant.ITEM_TYPE_LOAD_END_HINT){
            // 顯示即可，不用特別處理
        }
    }
}

