package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreVoucher;

/**
 * 店鋪優惠券列表Adapter
 * @author zwm
 */
public class StoreVoucherListAdapter extends ViewGroupAdapter<StoreVoucher> {
    String currencyTypeSign;

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public StoreVoucherListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);

        currencyTypeSign = context.getString(R.string.currency_type_sign);
        addClickableChildrenId(R.id.btn_receive_voucher_now);
    }

    @Override
    public void bindView(int position, View itemView, StoreVoucher itemData) {

        setText(itemView, R.id.tv_template_price, currencyTypeSign + itemData.templatePrice);
        setText(itemView, R.id.tv_limit_amount_text, itemData.limitAmountText);
        setText(itemView, R.id.tv_store_name, itemData.storeName);
        setText(itemView, R.id.tv_usable_client_type_text, "(" + itemData.usableClientTypeText + ")");
        String validTime = context.getString(R.string.text_valid_time) + ": " + itemData.useStartTime +
                "  -  " + itemData.useEndTime;
        setText(itemView, R.id.tv_valid_time, validTime);

        if (itemData.memberIsReceive == Constant.ONE) {
            setBackgroundResource(itemView, R.id.rl_left_container, R.drawable.grey_voucher);
        } else {
            setBackgroundResource(itemView, R.id.rl_left_container, R.drawable.pink_voucher);
        }
    }
}
