package com.ftofs.twant.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;

import cn.snailpad.easyjson.EasyJSONObject;

public class StoreVoucher implements Parcelable {
    public int memberIsReceive;//會員又沒有領取 0是未領取,中秋活動
    public String templateTitle;

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

    protected StoreVoucher(Parcel in) {
        memberIsReceive = in.readInt();
        storeId = in.readInt();
        templateId = in.readInt();
        storeName = in.readString();
        storeAvatar = in.readString();
        templatePrice = in.readInt();
        limitAmountText = in.readString();
        usableClientTypeText = in.readString();
        useStartTime = in.readString();
        useEndTime = in.readString();
        state = in.readInt();
        searchSn = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(memberIsReceive);
        dest.writeInt(storeId);
        dest.writeInt(templateId);
        dest.writeString(storeName);
        dest.writeString(storeAvatar);
        dest.writeInt(templatePrice);
        dest.writeString(limitAmountText);
        dest.writeString(usableClientTypeText);
        dest.writeString(useStartTime);
        dest.writeString(useEndTime);
        dest.writeInt(state);
        dest.writeString(searchSn);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StoreVoucher> CREATOR = new Creator<StoreVoucher>() {
        @Override
        public StoreVoucher createFromParcel(Parcel in) {
            return new StoreVoucher(in);
        }

        @Override
        public StoreVoucher[] newArray(int size) {
            return new StoreVoucher[size];
        }
    };

    public static StoreVoucher parse(EasyJSONObject voucher) throws Exception{
        StoreVoucher voucher1=new StoreVoucher(
                voucher.getInt("storeId"),
                voucher.getInt("templateId"),
                voucher.getSafeString("storeName"),
                voucher.getSafeString("storeAvatar"),
                voucher.getInt("templatePrice"),
                voucher.getSafeString("limitAmountText"),
                voucher.getSafeString("usableClientTypeText"),
                voucher.getSafeString("useStartTime"),
                voucher.getSafeString("useEndTime"),
                voucher.getInt("withoutState"));//領完未領完
        voucher1.memberIsReceive = voucher.getInt("memberIsReceive");
        voucher1.templateTitle = voucher.getSafeString("templateTitle");
        return voucher1;
    }

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
