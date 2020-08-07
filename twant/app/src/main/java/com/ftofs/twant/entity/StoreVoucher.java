package com.ftofs.twant.entity;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;

public class StoreVoucher {
    public StoreVoucher(int storeId, int templateId, String storeName, String storeAvatar, int templatePrice, String limitAmountText,
                        String usableClientTypeText, String useStartTime, String useEndTime,
                        int state) {
        this.storeId = storeId;
        this.templateId = templateId;
        this.storeName = storeName;
        this.storeAvatar = storeAvatar;
        this.templatePrice = templatePrice;
        this.limitAmountText = limitAmountText;
        this.usableClientTypeText = usableClientTypeText;
        this.useStartTime = useStartTime;
        this.useEndTime = useEndTime;
        this.state = state;
    }

    public int storeId;  // 商店Id大于0表示商店券，否則表示平臺券
    public int templateId;
    public String storeName;
    public String storeAvatar;
    public int templatePrice;
    public String limitAmountText;
    public String usableClientTypeText;
    public String useStartTime;
    public String useEndTime;

    /**
     * 優惠券狀態
     */
    public int state;
    public String searchSn;

    /**
     * 優惠券是否可用
     * @return
     */
    public boolean isUsable() {
        return state == Constant.COUPON_STATE_UNRECEIVED || state == Constant.COUPON_STATE_RECEIVED;
    }

    /**
     * 獲取優惠券的狀態圖標資源
     * 如果不需要，則返回0
     * @return
     */
    public int getStateIconResId() {
        if (state == Constant.COUPON_STATE_RECEIVED) {
            return R.drawable.icon_voucher_state_received;
        } else if (state == Constant.COUPON_STATE_RUN_OUT_OF) {
            return R.drawable.icon_voucher_state_run_out_of;
        } else if (state == Constant.COUPON_STATE_USED) {
            return R.drawable.icon_voucher_state_used;
        } else if (state == Constant.COUPON_STATE_OUT_OF_DATE) {
            return R.drawable.icon_voucher_state_out_of_date;
        } else if (state == Constant.COUPON_STATE_DISCARDED) {
            return R.drawable.icon_voucher_state_discarded;
        }

        return 0;
    }
}
