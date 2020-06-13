package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.PaySuccessStoreInfoItem;
import com.ftofs.twant.entity.PaySuccessSummaryItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.orhanobut.hawk.Hawk;
import com.sxu.shadowdrawable.ShadowDrawable;

import java.util.List;

/**
 * 支付成功頁面Adapter
 * @author zwm
 */
public class PaySuccessItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    Context context;

    int twGrey;
    int twRed;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PaySuccessItemAdapter(Context context, List<MultiItemEntity> data) {
        super(data);

        this.context = context;

        addItemType(Constant.ITEM_VIEW_TYPE_SUMMARY, R.layout.pay_success_summary_item);
        addItemType(Constant.ITEM_VIEW_TYPE_COMMON, R.layout.pay_success_store_info_item);

        twGrey = Color.parseColor("#767777");
        twRed = context.getColor(R.color.tw_red);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        if (item instanceof PaySuccessSummaryItem) {
            helper.addOnClickListener(R.id.btn_view_order, R.id.btn_goto_home);

            PaySuccessSummaryItem paySuccessSummaryItem = (PaySuccessSummaryItem) item;
            SLog.info("paySuccessSummaryItem.isCashOnDelivery[%s]", paySuccessSummaryItem.isCashOnDelivery);
            if (paySuccessSummaryItem.isCashOnDelivery) {
                helper.setText(R.id.tv_pay_status_desc, mContext.getString(R.string.text_order_commit_success));
            } else {
                helper.setText(R.id.tv_pay_status_desc, mContext.getString(R.string.text_pay_success));
            }

            if (paySuccessSummaryItem.isGroupBuy) {
                helper.setText(R.id.btn_goto_home, "邀請好友");
            }

            TextView tvPaySuccessMPayActivityIndicator = helper.getView(R.id.tv_pay_success_mpay_activity_indicator);
            if (paySuccessSummaryItem.isMPayActivity) {
                tvPaySuccessMPayActivityIndicator.setText(((PaySuccessSummaryItem) item).mpayActivityDesc);
                tvPaySuccessMPayActivityIndicator.setVisibility(View.VISIBLE);
            } else {
                tvPaySuccessMPayActivityIndicator.setVisibility(View.GONE);
            }

            TextView tvPaySuccessStoreCouponIndicator = helper.getView(R.id.tv_pay_success_store_coupon_indicator);
            if (paySuccessSummaryItem.storeCouponCount > 0) {
                String desc = String.format("收到%d張店鋪券", paySuccessSummaryItem.storeCouponCount);
                tvPaySuccessStoreCouponIndicator.setText(desc);
                tvPaySuccessStoreCouponIndicator.setVisibility(View.VISIBLE);
            } else {
                tvPaySuccessStoreCouponIndicator.setVisibility(View.GONE);
            }

            // 顯示訂單金額
            double totalOrderAmount = Hawk.get(SPField.FIELD_TOTAL_ORDER_AMOUNT, 0d);
            helper.setText(R.id.tv_order_total_amount, "$" + StringUtil.formatFloat(totalOrderAmount));
        } else if (item instanceof PaySuccessStoreInfoItem) {
            helper.addOnClickListener(R.id.btn_shop_map);

            PaySuccessStoreInfoItem paySuccessStoreInfoItem = (PaySuccessStoreInfoItem) item;

            ImageView imgStoreAvatar = helper.getView(R.id.img_store_avatar);
            Glide.with(context).load(StringUtil.normalizeImageUrl(paySuccessStoreInfoItem.storeAvatar)).centerCrop().into(imgStoreAvatar);

            // 店鋪營業狀態
            TextView tvStoreBizStatus = helper.getView(R.id.tv_store_biz_status);
            if (paySuccessStoreInfoItem.storeBizStatus == Constant.TRUE_INT) {
                tvStoreBizStatus.setBackgroundResource(R.drawable.pay_success_store_status_bg_biz);
                tvStoreBizStatus.setTextColor(twRed);
                tvStoreBizStatus.setText("營業中");
            } else {
                tvStoreBizStatus.setBackgroundResource(R.drawable.pay_success_store_status_bg_rest);
                tvStoreBizStatus.setTextColor(twGrey);
                tvStoreBizStatus.setText("休息中");
            }

            helper.setText(R.id.tv_store_name, paySuccessStoreInfoItem.storeName)
                    .setText(R.id.tv_goods_name, paySuccessStoreInfoItem.goodsName)
                    .setText(R.id.tv_goods_spec, paySuccessStoreInfoItem.goodsSpec)
                    .setText(R.id.tv_store_phone, paySuccessStoreInfoItem.storePhone)
                    .setText(R.id.tv_store_address, paySuccessStoreInfoItem.storeAddress)
                    .setText(R.id.tv_business_time_working_day, paySuccessStoreInfoItem.businessTimeWorkingDay)
                    .setText(R.id.tv_business_time_weekend, paySuccessStoreInfoItem.businessTimeWeekend)
                    .setText(R.id.tv_transport_instruction, paySuccessStoreInfoItem.transportInstruction);


            if (StringUtil.isEmpty(paySuccessStoreInfoItem.selfTakeCode) || "0".equals(paySuccessStoreInfoItem.selfTakeCode)) {
                helper.setGone(R.id.ll_self_take_code_container, false);
            } else {
                TextView tvSelfTakeCode = helper.getView(R.id.tv_self_take_code);
                tvSelfTakeCode.setText(paySuccessStoreInfoItem.selfTakeCode);
                helper.setGone(R.id.ll_self_take_code_container, true);
            }

            ImageView goodsImage = helper.getView(R.id.goods_image);
            Glide.with(context).load(StringUtil.normalizeImageUrl(paySuccessStoreInfoItem.goodsImageUrl)).centerCrop().into(goodsImage);

            if (paySuccessStoreInfoItem.templatePrice > 0) {
                String conformTitle = "送 " + StringUtil.formatPrice(context, paySuccessStoreInfoItem.templatePrice, 0) + " 店舖券一張";
                helper.setText(R.id.tv_conform_title, conformTitle);
                helper.setGone(R.id.ll_conform_container, true);
            } else {
                helper.setGone(R.id.ll_conform_container, false);
            }

            LinearLayout llGoodsContainer = helper.getView(R.id.ll_goods_container);
            ShadowDrawable.setShadowDrawable(llGoodsContainer, Color.parseColor("#FFFFFF"), Util.dip2px(context, 5),
                    Color.parseColor("#19000000"), Util.dip2px(context, 5), 0, 0);
        }
    }
}

