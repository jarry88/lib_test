package com.ftofs.twant.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.DistributionPromotionGoods;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class DistributionPromotionGoodsAdapter extends BaseMultiItemQuickAdapter<DistributionPromotionGoods, BaseViewHolder> {
    Context context;
    public DistributionPromotionGoodsAdapter(Context context, @Nullable List<DistributionPromotionGoods> data) {
        super(data);

        this.context = context;
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.distribution_promotion_goods);
        addItemType(Constant.ITEM_TYPE_NO_DATA, R.layout.distribution_empty_view);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DistributionPromotionGoods item) {
        if (item.itemType == Constant.ITEM_TYPE_NORMAL) {
            helper.addOnClickListener(R.id.btn_share);

            helper.setImageResource(R.id.ic_selected_indicator,
                    item.selected ? R.drawable.ic_baseline_check_box_24 : R.drawable.ic_baseline_check_box_outline_blank_24);

            helper.setText(R.id.tv_goods_name, item.goodsName)
                    .setText(R.id.tv_goods_price, "¥" + StringUtil.formatFloat(item.batchPrice2));

            ImageView goodsImage = helper.getView(R.id.img_goods);
            Glide.with(context).load(StringUtil.normalizeImageUrl(item.imageName, "?x-oss-process=image/resize,w_160")).centerCrop().into(goodsImage);

            if (item.commissionType == Constant.COMMISSION_TYPE_RATIO) {
                helper.setText(R.id.tv_first_level, "一級：" + StringUtil.formatFloat(item.commissionLevel1) + "%")
                        .setText(R.id.tv_second_level, "二級：" + StringUtil.formatFloat(item.commissionLevel2) + "%");
            } else if (item.commissionType == Constant.COMMISSION_TYPE_FIXED) {
                helper.setText(R.id.tv_first_level, "一級：" + StringUtil.formatFloat(item.commissionAmount))
                        .setText(R.id.tv_second_level, "二級：" + StringUtil.formatFloat(item.commissionAmount2));
            }
        }
    }
}
