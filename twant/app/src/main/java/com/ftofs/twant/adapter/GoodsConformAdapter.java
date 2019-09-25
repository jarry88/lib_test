package com.ftofs.twant.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.GiftVo;
import com.ftofs.twant.entity.GoodsConformItem;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.util.Util;

import java.util.List;

/**
 * 商品詳情 => 滿優惠 Adapter
 * @author zwm
 */
public class GoodsConformAdapter extends BaseQuickAdapter<GoodsConformItem, BaseViewHolder> {
    Context context;
    OnSelectedListener onSelectedListener;
    public GoodsConformAdapter(Context context, int layoutResId, @Nullable List<GoodsConformItem> data, OnSelectedListener onSelectedListener) {
        super(layoutResId, data);

        this.context = context;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsConformItem item) {
        String conformDesc = context.getString(R.string.text_conform_desc_template);
        conformDesc = String.format(conformDesc, item.limitAmount);
        helper.setText(R.id.tv_conform_desc, conformDesc);
        String validTime = context.getString(R.string.text_valid_time) + ": " + item.startTime.substring(0, 10) + " - " + item.endTime.substring(0, 10);
        helper.setText(R.id.tv_valid_time, validTime);

        // 包郵
        if (item.isFreeFreight == 0) {
            helper.setGone(R.id.ll_free_freight_ind_container, false);
        } else {
            String freeFreightDesc = String.format("满%d元，包邮", item.limitAmount);
            helper.setText(R.id.tv_free_freight_desc, freeFreightDesc);
        }

        // 立減
        if (item.conformPrice > 0) {
            String instantDiscountDesc = context.getString(R.string.text_instant_discount_desc);
            instantDiscountDesc = String.format(instantDiscountDesc, item.limitAmount, item.conformPrice);
            helper.setText(R.id.tv_instant_discount_desc, instantDiscountDesc);
        } else {
            helper.setGone(R.id.ll_instant_discount_ind_container, false);
        }

        // 送券
        if (item.templateId > 0) {
            String presentVoucherDesc = context.getString(R.string.text_present_voucher_desc);
            presentVoucherDesc = String.format(presentVoucherDesc, item.limitAmount, item.templatePrice);
            helper.setText(R.id.tv_present_voucher_desc, presentVoucherDesc);
        } else {
            helper.setGone(R.id.ll_present_voucher_ind_container, false);
        }

        // 贈品
        if (item.giftVoList != null && item.giftVoList.size() > 0) {
            LinearLayout container = helper.getView(R.id.ll_conform_gift_list);
            ConformGiftAdapter conformGiftAdapter = new ConformGiftAdapter(context, container,R.layout.goods_conform_gift_list_item, item.limitAmount);
            conformGiftAdapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
                @Override
                public void onClick(ViewGroupAdapter adapter, View view, int position) {
                    GiftVo giftVo = item.giftVoList.get(position);
                    onSelectedListener.onSelected(PopupType.DEFAULT, 0, null); // 主要用于通知隱藏彈窗
                    Util.startFragment(GoodsDetailFragment.newInstance(giftVo.commonId, giftVo.goodsId));
                }
            });
            conformGiftAdapter.setData(item.giftVoList);
        } else {
            helper.setGone(R.id.ll_gift_ind_container, false);
        }
    }
}

