package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CrossBorderActivityGoods;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import java.util.List;

public class CrossBorderActivityGoodsAdapter extends BaseQuickAdapter<CrossBorderActivityGoods, BaseViewHolder> {
    Context context;
    int promotionType;  // 砍價還是拼團

    int screenWidth;

    public CrossBorderActivityGoodsAdapter(Context context, int promotionType, int layoutResId, @Nullable List<CrossBorderActivityGoods> data) {
        super(layoutResId, data);

        this.context = context;
        this.promotionType = promotionType;

        screenWidth = Util.getScreenDimension(context).first;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CrossBorderActivityGoods item) {
        helper.addOnClickListener(R.id.btn_action);

        ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
        layoutParams.width = (screenWidth - Util.dip2px(context, 64)) / 3;
        helper.itemView.setLayoutParams(layoutParams);

        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.goodsImage)).centerCrop().into(goodsImage);

        helper.setText(R.id.tv_goods_name, item.goodsName)
            .setText(R.id.tv_price, StringUtil.formatMopPrice(item.price, 1));

        if (promotionType == Constant.PROMOTION_TYPE_GROUP) {
            helper.setText(R.id.btn_action, "立即拼團");
        } else if (promotionType == Constant.PROMOTION_TYPE_BARGAIN) {
            helper.setText(R.id.btn_action, "立即砍價");
        }
    }
}
