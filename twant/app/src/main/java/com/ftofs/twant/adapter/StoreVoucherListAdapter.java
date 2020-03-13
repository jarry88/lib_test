package com.ftofs.twant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.util.StringUtil;

/**
 * 商店優惠券列表Adapter
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
    }

    @Override
    public void bindView(int position, View itemView, StoreVoucher itemData) {
        setText(itemView, R.id.tv_template_price, String.valueOf(itemData.templatePrice));
        setText(itemView, R.id.tv_limit_amount_text, itemData.limitAmountText);
        setText(itemView, R.id.tv_store_name, itemData.storeName);
        String validTime = itemData.useStartTime + "  -  " + itemData.useEndTime;
        setText(itemView, R.id.tv_valid_time, validTime);

        TextView tvUsableClientTypeText = itemView.findViewById(R.id.tv_usable_client_type_text);
        ImageView imgCouponIcon = itemView.findViewById(R.id.img_coupon_icon);
        TextView tvUsageDesc = itemView.findViewById(R.id.tv_usage_desc);

        if (itemData.storeId > 0) {
            tvUsageDesc.setText("店舖專用");

            if (StringUtil.useDefaultAvatar(itemData.storeAvatar)) {
                Glide.with(context).load(R.drawable.default_store_avatar).centerCrop().into(imgCouponIcon);
            } else {
                Glide.with(context).load(StringUtil.normalizeImageUrl(itemData.storeAvatar)).centerCrop().into(imgCouponIcon);
            }
            tvUsableClientTypeText.setText(itemData.storeName);
        } else {
            tvUsageDesc.setText("平台專用");

            tvUsableClientTypeText.setText(itemData.usableClientTypeText);
        }


        ImageView imgVoucherState = itemView.findViewById(R.id.img_voucher_state);
        int stateIconResId = itemData.getStateIconResId();
        if (stateIconResId != 0) {
            imgVoucherState.setImageResource(stateIconResId);
            imgVoucherState.setVisibility(View.VISIBLE);
        } else {
            imgVoucherState.setVisibility(View.GONE);
        }


        if (!itemData.isUsable()) { // 不可用
            tvUsageDesc.setTextColor(Color.parseColor("#FFD4D4D4"));
            setBackgroundResource(itemView, R.id.rl_left_container, R.drawable.grey_voucher);
        }
    }
}
