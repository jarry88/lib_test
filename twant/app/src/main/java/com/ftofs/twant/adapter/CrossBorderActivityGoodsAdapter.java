package com.ftofs.twant.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.lib_net.model.Goods;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;

import java.util.List;

public class CrossBorderActivityGoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    Context context;
    int promotionType;  // 砍價還是拼團

    public CrossBorderActivityGoodsAdapter(Context context, int promotionType, int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);

        this.context = context;
        this.promotionType = promotionType;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Goods item) {
        helper.addOnClickListener(R.id.btn_action);

        if (promotionType == Constant.PROMOTION_TYPE_GROUP) {
            helper.setText(R.id.btn_action, "立即拼團")
                .setText(R.id.tv_price_type, "拼團價：");
        } else if (promotionType == Constant.PROMOTION_TYPE_BARGAIN) {
            helper.setText(R.id.btn_action, "立即砍價")
                    .setText(R.id.tv_price_type, "最低可砍至：");
        }
    }
}
