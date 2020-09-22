package com.ftofs.twant.adapter;

import android.content.Context;
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

import java.util.List;

public class CrossBorderActivityGoodsAdapter extends BaseQuickAdapter<CrossBorderActivityGoods, BaseViewHolder> {
    Context context;
    int promotionType;  // 砍價還是拼團

    public CrossBorderActivityGoodsAdapter(Context context, int promotionType, int layoutResId, @Nullable List<CrossBorderActivityGoods> data) {
        super(layoutResId, data);

        this.context = context;
        this.promotionType = promotionType;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CrossBorderActivityGoods item) {
        helper.addOnClickListener(R.id.btn_action);

        ImageView goodsImage = helper.getView(R.id.goods_image);
        Glide.with(context).load(StringUtil.normalizeImageUrl(item.goodsImage)).centerCrop().into(goodsImage);

        helper.setText(R.id.tv_goods_name, item.goodsName)
            .setText(R.id.tv_price, StringUtil.formatPrice(context, item.price, 0));

        if (promotionType == Constant.PROMOTION_TYPE_GROUP) {
            helper.setText(R.id.btn_action, "立即拼團")
                .setText(R.id.tv_price_type, "拼團價：");
        } else if (promotionType == Constant.PROMOTION_TYPE_BARGAIN) {
            helper.setText(R.id.btn_action, "立即砍價")
                    .setText(R.id.tv_price_type, "最低可砍至：");
        }
    }
}
