package com.ftofs.twant.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.ConfirmOrderSkuItem;
import com.ftofs.twant.entity.ConfirmOrderStoreItem;
import com.ftofs.twant.entity.ConfirmOrderSummaryItem;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import java.util.List;

/**
 * 確認訂單商店列表Adapter
 * @author zwm
 */
public class ConfirmOrderStoreAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    Context context;
    String timesSign;
    List<ListPopupItem> shippingTimeDescList;  // 配送時間描述列表
    int payWay = Constant.PAY_WAY_ONLINE;

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public ConfirmOrderStoreAdapter(Context context, List<ListPopupItem> shippingTimeDescList, @Nullable List<MultiItemEntity> data) {
        super(data);
        addItemType(Constant.ITEM_VIEW_TYPE_COMMON, R.layout.confirm_order_store_item);
        addItemType(Constant.ITEM_VIEW_TYPE_SUMMARY, R.layout.confirm_order_summary_item);

        this.context = context;
        this.shippingTimeDescList = shippingTimeDescList;
        timesSign = context.getResources().getString(R.string.times_sign);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity multiItemEntity) {
        int itemViewType = helper.getItemViewType();

        if (itemViewType == Constant.ITEM_VIEW_TYPE_COMMON) {
            final ConfirmOrderStoreItem item = (ConfirmOrderStoreItem) multiItemEntity;
            helper.addOnClickListener(R.id.btn_receipt)  // 變更單據信息
                    .addOnClickListener(R.id.btn_change_shipping_time)  // 修改配送時間
                    .addOnClickListener(R.id.ll_store_info_container)  // 點擊商店信息
                    .addOnClickListener(R.id.btn_use_voucher);  // 使用商店券


            String voucherStatus = "可用0張";
            if (item.voucherCount > 0) {
                if (item.voucherId > 0) { // 如果正在使用優惠券，則顯示正在使用的券的名稱
                    SLog.info("HERE");
                    voucherStatus = item.voucherName;
                } else { // 如果沒有使用優惠券，則顯示可用的券的數量
                    SLog.info("HERE");
                    voucherStatus = Util.getAvailableCouponCountDesc(item.voucherCount);
                }
            } else { // 如果沒有可用的優惠券，則隱藏
//                helper.setGone(R.id.rl_voucher_container, false);
            }


            String discountAmountText = StringUtil.formatPrice(context, item.discountAmount, 0,2);
            if (item.discountAmount > 0) { // 如果有優惠，在前面加上負號
                discountAmountText = "-" + discountAmountText;
            }

            //每家商店小計金額
            double realFreightAmount = item.freightAmount;
            //
            if (payWay == Constant.PAY_WAY_FETCH) {  // 如果是門店自提，則不算運費
                realFreightAmount = 0;
            }
            // float finalPayAmount = item.buyItemAmount + realFreightAmount - item.discountAmount;
            double taxAmount =item.taxAmount;
            SLog.info(item.toString());
            double finalPayAmount = item.buyItemAmount + realFreightAmount;
//            if (item.tariffEnable == Constant.TRUE_INT) {
//                helper.setVisible(R.id.rl_tax_container, true);
//            } else {
//                helper.setVisible(R.id.rl_tax_container, false);
//            }

            if (item.taxAmount < Constant.DOUBLE_ZERO_THRESHOLD) {
                helper.setVisible(R.id.rl_tax_container, false);
            } else {
                helper.setVisible(R.id.rl_tax_container, true);
            }

            SLog.info("realFreightAmount[%s], finalPayAmount[%s]", realFreightAmount, finalPayAmount);
            helper.setText(R.id.tv_store_name, item.storeName)
                .setText(R.id.tv_freight_amount, StringUtil.formatPrice(context, realFreightAmount, 0,2))
                .setText(R.id.tv_store_discount, discountAmountText)
                .setText(R.id.tv_store_voucher_count, voucherStatus)
                .setText(R.id.tv_store_item_count, String.format("共%d件，小計：", item.itemCount))
                .setText(R.id.tv_store_pay_amount, StringUtil.formatPrice(context, finalPayAmount, 0,2))
            .setText(R.id.tv_tax_number,StringUtil.formatPrice(context,taxAmount,0,2));


            EditText etLeaveMessage = helper.getView(R.id.et_leave_message);
            etLeaveMessage.setText(item.leaveMessage);
            EditTextUtil.cursorSeekToEnd(etLeaveMessage);
            etLeaveMessage.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    item.leaveMessage = s.toString();
                    // SLog.info("leaveMessage[%s]", item.leaveMessage);
                }
            });

            LinearLayout llSkuItemContainer = helper.getView(R.id.ll_sku_item_container);
            llSkuItemContainer.removeAllViews();
            for (ConfirmOrderSkuItem confirmOrderSkuItem : item.confirmOrderSkuItemList) {
                LinearLayout skuItemView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.confirm_order_sku_item, null, false);

                ImageView goodsImageView = skuItemView.findViewById(R.id.goods_image);
                Glide.with(goodsImageView).load(confirmOrderSkuItem.goodsImage).centerCrop().into(goodsImageView);
                TextView goodsName = skuItemView.findViewById(R.id.tv_goods_name);
                goodsName.setText(confirmOrderSkuItem.goodsName);
                TextView tvFullSpecs = skuItemView.findViewById(R.id.tv_goods_full_specs);
                tvFullSpecs.setText(confirmOrderSkuItem.goodsFullSpecs);
                TextView tvBuyNum = skuItemView.findViewById(R.id.tv_sku_count);
                TextView tvBuyNumError = skuItemView.findViewById(R.id.tv_error_status);
                String buyNum = timesSign + " " + confirmOrderSkuItem.buyNum;
                if (confirmOrderSkuItem.allowSend == 0) {
                    tvBuyNum.setVisibility(View.GONE);
                    tvBuyNumError.setVisibility(View.VISIBLE);
                    tvBuyNumError.setText(context.getString(R.string.text_not_allow_send));

                } else if (confirmOrderSkuItem.storageStatus == 0) {
                    tvBuyNum.setVisibility(View.GONE);
                    tvBuyNumError.setVisibility(View.VISIBLE);
                    tvBuyNumError.setText(context.getString(R.string.text_storage_out));


                } else {
                    tvBuyNum.setVisibility(View.VISIBLE);
                    tvBuyNumError.setVisibility(View.GONE);
                }
                tvBuyNum.setText(buyNum);
                TextView tvSkuPrice = skuItemView.findViewById(R.id.tv_sku_price);
                tvSkuPrice.setText(StringUtil.formatPrice(context, confirmOrderSkuItem.skuPrice, 0,2));

                TextView tvActivityLabel = skuItemView.findViewById(R.id.tv_activity_label);
                tvActivityLabel.setVisibility(confirmOrderSkuItem.joinBigSale==0?View.VISIBLE:View.GONE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, Util.dip2px(context, 15), 0, 0);

                LinearLayout llSkuGiftContainer = skuItemView.findViewById(R.id.ll_sku_gift_container);
                if (confirmOrderSkuItem.giftItemList != null && confirmOrderSkuItem.giftItemList.size() > 0) {
                    ConfirmOrderGiftListAdapter giftListAdapter = new ConfirmOrderGiftListAdapter(context, llSkuGiftContainer, R.layout.confirm_order_gift_item);
                    giftListAdapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(ViewGroupAdapter adapter, View view, int position) {
                            SLog.info("onClick, position[%d]", position);
                            GiftItem giftItem = confirmOrderSkuItem.giftItemList.get(position);
                            Util.startFragment(GoodsDetailFragment.newInstance(giftItem.commonId, giftItem.goodsId));
                        }
                    });
                    giftListAdapter.setData(confirmOrderSkuItem.giftItemList);
                } else {
                    llSkuGiftContainer.removeAllViews();
                    llSkuGiftContainer.setVisibility(View.GONE);
                }

                llSkuItemContainer.addView(skuItemView, layoutParams);
            }

            if (item.conformTemplatePrice > 0) {
                String conformTitle = "送 " + StringUtil.formatPrice(context, item.conformTemplatePrice, 0,2) + " 店舖券一張";
                helper.setText(R.id.tv_conform_title, conformTitle);
                helper.setGone(R.id.ll_conform_container, true);
            } else {
                helper.setGone(R.id.ll_conform_container, false);
            }
        } else {
            // 匯總數據
            ConfirmOrderSummaryItem item = (ConfirmOrderSummaryItem) multiItemEntity;

            // 如果沒設置單據，則顯示【不開單據】，否則顯示單據抬頭
            if (item.receipt == null) {
                helper.setText(R.id.tv_receipt, context.getResources().getString(R.string.text_does_not_need_receipt));
            } else {
                helper.setText(R.id.tv_receipt, item.receipt.header);
            }

            if (shippingTimeDescList != null && shippingTimeDescList.size() > 0) {
                helper.setText(R.id.tv_shipping_time, shippingTimeDescList.get(item.shipTimeType).title);
            }

            helper.addOnClickListener(R.id.btn_change_pay_way)
                .addOnClickListener(R.id.btn_receipt)
                .addOnClickListener(R.id.btn_change_shipping_time)
                .addOnClickListener(R.id.btn_select_platform_coupon);  // 使用平台券;
            SLog.info("item.platformCouponCount[%d]", item.platformCouponCount);

            SLog.info("item.paymentTypeCode[%s]", item.paymentTypeCode);
            helper.setText(R.id.tv_pay_way, paymentTypeCodeToPayWayDesc(item.paymentTypeCode));
            if (item.platformCouponCount > 0) {
                helper.setText(R.id.tv_platform_coupon, item.platformCouponStatus);
                helper.setGone(R.id.btn_select_platform_coupon, true);
            } else {
                helper.setGone(R.id.btn_select_platform_coupon, false);
            }
        }
    }

    private String paymentTypeCodeToPayWayDesc(String paymentTypeCode) {
        switch (paymentTypeCode) {
            case Constant.PAYMENT_TYPE_CODE_ONLINE:
                return context.getResources().getString(R.string.text_pay_online);
            case Constant.PAYMENT_TYPE_CODE_OFFLINE:
                return context.getResources().getString(R.string.text_pay_delivery);
            case Constant.PAYMENT_TYPE_CODE_CHAIN:
                return context.getResources().getString(R.string.text_pay_fetch);
            default:
                return null;
        }
    }
}
