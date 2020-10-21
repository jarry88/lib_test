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
import com.ftofs.twant.entity.DistributionOrderItem;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

/**
 * 分銷系統訂單Adapter
 * @author zwm
 */
public class DistributionOrderAdapter extends BaseMultiItemQuickAdapter<DistributionOrderItem, BaseViewHolder> {
    Context context;
    public DistributionOrderAdapter(Context context, @Nullable List<DistributionOrderItem> data) {
        super(data);

        this.context = context;
        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.distribution_order_item);
        addItemType(Constant.ITEM_TYPE_NO_DATA, R.layout.distribution_empty_view);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, DistributionOrderItem item) {
        if (item.itemType == Constant.ITEM_TYPE_NORMAL) {
            helper.setText(R.id.tv_order_sn, String.valueOf(item.ordersSn))
                    .setText(R.id.tv_order_status, item.getMarketingOrdersStateDesc())
                    .setText(R.id.tv_goods_name, item.goodsName)
                    .setText(R.id.tv_create_time, item.createTime)
                    .setText(R.id.tv_pay_amount, "實付款：¥" + StringUtil.formatFloat(item.goodsPayAmount))
                    .setText(R.id.tv_expect_commission_amount, "預計佣金：" + StringUtil.formatFloat(item.predictCommission) + "元");

            ImageView goodsImage = helper.getView(R.id.goods_image);
            Glide.with(context).load(StringUtil.normalizeImageUrl(item.goodsImage, "?x-oss-process=image/resize,w_160")).centerCrop().into(goodsImage);
        }
    }
}
