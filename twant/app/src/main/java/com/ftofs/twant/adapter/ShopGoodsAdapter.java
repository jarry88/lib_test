package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;


/**
 * 店鋪商品Adapter
 * @author zwm
 */
public class ShopGoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    Context context;

    public ShopGoodsAdapter(Context context, int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods goods) {
        ImageView goodsImage = helper.getView(R.id.img_goods);
        Glide.with(context).load(goods.imageUrl).centerCrop().into(goodsImage);
        helper.setText(R.id.tv_goods_name, goods.name);
        helper.setText(R.id.tv_goods_jingle, goods.jingle);
        helper.setText(R.id.tv_goods_price, StringUtil.formatPrice(context, (float) goods.price, 1));
    }
}
