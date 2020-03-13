package com.ftofs.twant.adapter;

import androidx.annotation.Nullable;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.StoreVoucherVo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.List;

public class OrderVoucherListAdapter extends BaseQuickAdapter<StoreVoucherVo, BaseViewHolder> {
    String storeName;
    int couponType;
    int platformCouponIndex;

    /**
     * Constructor
     * @param layoutResId
     * @param storeName
     * @param couponType data參數是哪種類型的列表
     * @param data 商店券列表 或 平台券列表
     * @param platformCouponIndex 當前正在使用的平台券列表Index(-1表示沒有使用），當couponType為平台券時才有意義
     */
    public OrderVoucherListAdapter(int layoutResId, String storeName, int couponType, @Nullable List<StoreVoucherVo> data, int platformCouponIndex) {
        super(layoutResId, data);
        this.storeName = storeName;
        this.couponType = couponType;
        this.platformCouponIndex = platformCouponIndex;
    }

    @Override
    protected void convert(BaseViewHolder helper, StoreVoucherVo itemData) {
        helper.setText(R.id.tv_template_price, StringUtil.formatPrice(mContext, itemData.price, 0));
        helper.setText(R.id.tv_limit_amount_text, itemData.limitText);
        if (couponType == Constant.COUPON_TYPE_STORE) {
            helper.setText(R.id.tv_store_name, storeName);
        } else {
            helper.setText(R.id.tv_store_name, itemData.voucherTitle);
        }

        String validTime = mContext.getString(R.string.text_valid_time) + ": " + itemData.startTime +
                "  -  " + itemData.endTime;
        helper.setText(R.id.tv_valid_time, validTime);



        RelativeLayout rlLeftContainer = helper.getView(R.id.rl_left_container);
        if (couponType == Constant.COUPON_TYPE_STORE) {
            // rlLeftContainer.setBackgroundResource(R.drawable.red_voucher);
            if (itemData.isInUse) {
                helper.setGone(R.id.vw_voucher_in_use_indicator, true);
            } else {
                helper.setGone(R.id.vw_voucher_in_use_indicator, false);
            }
        } else {
            // rlLeftContainer.setBackgroundResource(R.drawable.blue_voucher);
            int position = helper.getAdapterPosition();
            SLog.info("position[%d]", position);
            if (position == platformCouponIndex) {
                helper.setGone(R.id.vw_voucher_in_use_indicator, true);
            } else {
                helper.setGone(R.id.vw_voucher_in_use_indicator, false);
            }
        }

    }
    /*
    public void setPlatformCouponIndex(int platformCouponIndex) {
        this.platformCouponIndex = platformCouponIndex;
    }
    */
}
