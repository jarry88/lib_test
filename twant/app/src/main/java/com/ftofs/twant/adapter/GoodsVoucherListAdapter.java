package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class GoodsVoucherListAdapter extends BaseQuickAdapter<StoreVoucher, BaseViewHolder> {
    public GoodsVoucherListAdapter(int layoutResId, @Nullable List<StoreVoucher> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreVoucher itemData) {
        helper.setText(R.id.tv_template_price, String.valueOf(itemData.templatePrice));
        helper.setText(R.id.tv_limit_amount_text, itemData.limitAmountText);
        // helper.setText(R.id.tv_store_name, itemData.storeName);
        helper.setText(R.id.tv_usable_client_type_text, itemData.storeName);
        String validTime = mContext.getString(R.string.text_valid_time) + ": " + itemData.useStartTime +
                "  -  " + itemData.useEndTime;
        helper.setText(R.id.tv_valid_time, validTime);

        ImageView imgVoucherState = helper.getView(R.id.img_voucher_state);
        int couponStateIconResId = itemData.getStateIconResId();
        if (couponStateIconResId > 0) {
            imgVoucherState.setImageResource(couponStateIconResId);
            imgVoucherState.setVisibility(View.VISIBLE);
        } else {
            imgVoucherState.setVisibility(View.GONE);
        }

        int itemCount = getItemCount();
        int position = helper.getAdapterPosition();
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) helper.itemView.getLayoutParams();
        if (position == itemCount - 1) {
            // 最后一項，設置大一點的bottomMargin
            layoutParams.bottomMargin = (int) mContext.getResources().getDimension(R.dimen.bottom_toolbar_max_height);
        } else {
            layoutParams.bottomMargin = 0;
        }

        ImageView imgCouponIcon = helper.getView(R.id.img_coupon_icon);

        if (itemData.storeId > 0) {
            helper.setText(R.id.tv_usage_desc, "商店專用");
            if (StringUtil.useDefaultAvatar(itemData.storeAvatar)) {
                Glide.with(mContext).load(R.drawable.default_store_avatar).centerCrop().into(imgCouponIcon);
            } else {
                Glide.with(mContext).load(StringUtil.normalizeImageUrl(itemData.storeAvatar)).centerCrop().into(imgCouponIcon);
            }
        } else {
            helper.setText(R.id.tv_usage_desc, "平臺專用");
            Glide.with(mContext).load(R.drawable.app_logo).centerCrop().into(imgCouponIcon);
        }
    }
}
