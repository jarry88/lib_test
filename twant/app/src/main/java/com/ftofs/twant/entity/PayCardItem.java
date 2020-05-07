package com.ftofs.twant.entity;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PayCardItem {
    public static final int PAY_TYPE_MPAY = 0;
    public static final int PAY_TYPE_WALLET = 1;
    // public static final int PAY_TYPE_TAIFUNG = 2;
    public static final int PAY_TYPE_ALIHK = 3;
    public static final int PAY_TYPE_WEIXING = 4;
    public static final int PAY_TYPE_ALI = 5;
    public static final int SUPPORT_PAY_TYPE_COUNT = 6;
    public final int payType;
    public static int[] macOrder = new int[] {PAY_TYPE_MPAY,PAY_TYPE_ALIHK,PAY_TYPE_WEIXING,PAY_TYPE_ALI,PAY_TYPE_WALLET};
    public static int[] hkOrder = new int[] {PAY_TYPE_ALIHK,PAY_TYPE_MPAY,PAY_TYPE_WEIXING,PAY_TYPE_ALI,PAY_TYPE_WALLET};
    public static int[] mainlandOrder = new int[] {PAY_TYPE_WEIXING,PAY_TYPE_ALI,PAY_TYPE_MPAY,PAY_TYPE_ALIHK,PAY_TYPE_WALLET};

    public boolean showMask;
    public String textBalance;
    public String textSupport;
    public boolean showActivityDesc;
    public boolean showBalance;
    public boolean showSupport;
    public int status =Constant.STATUS_UNSELECTED;

    public static List<PayCardItem> toItemList(String user_zone) {
        int [] order;
        if (StringUtil.isEmpty(user_zone)) {
            order = macOrder;
        } else if(user_zone.equals(Constant.AREA_CODE_MACAO)){
            order = macOrder;
        } else if (user_zone.equals(Constant.AREA_CODE_HONGKONG)) {
            order = hkOrder;
        } else if (user_zone.equals(Constant.AREA_CODE_MAINLAND)) {
            order = mainlandOrder;
        } else {
            order = macOrder;
        }
        List<PayCardItem> payCardItems =new ArrayList<>();
        for (int i=0;i<order.length ; i++) {
            PayCardItem payCardItem = newInstance(order[i]);
            if (payCardItem != null) {
                payCardItems.add(payCardItem);
            }
        }

        return payCardItems;
    }

    private static PayCardItem newInstance(int payIndex) {
        switch (payIndex) {
            case PAY_TYPE_MPAY:
                String payActivityDesc="活动期间最高享99元优惠券";//目前活動關閉
                PayCardItem mpayCard=new PayCardItem("Mpay", R.drawable.pay_vendor_bg_orange, R.drawable.new_icon_pay_mpay,PayCardItem.PAY_TYPE_MPAY);
                mpayCard.payDesc = payActivityDesc;
                return mpayCard;
            case PAY_TYPE_WALLET:
                PayCardItem walletCard = new PayCardItem("想付錢包", R.drawable.pay_vendor_bg_blue, R.drawable.new_icon_pay_wallet, PayCardItem.PAY_TYPE_WALLET);
                walletCard.showBalance = true;
                walletCard.textBalance = "(未激活)";
                return walletCard;
//            case PAY_TYPE_TAIFUNG:
//                String textSupport = "支持銀聯信用卡和大陸提款卡支付";
//                PayCardItem taifungCard = new PayCardItem("大豐銀行電子支付", R.drawable.pay_vendor_bg_yellow, R.drawable.new_icon_pay_taifung, PayCardItem.PAY_TYPE_TAIFUNG);
//                taifungCard.textSupport = textSupport;
//                taifungCard.showSupport = true;
//                return taifungCard;
            case PAY_TYPE_WEIXING:
                PayCardItem weixinCard = new PayCardItem("微信支付（內地）", R.drawable.pay_vendor_bg_green, R.drawable.new_icon_pay_weixin, PayCardItem.PAY_TYPE_WEIXING);
                return weixinCard;
            case PAY_TYPE_ALI:
                PayCardItem alipayCard = new PayCardItem("支付寳（內地）", R.drawable.pay_vendor_bg_blue, R.drawable.new_icon_pay_alipay, PayCardItem.PAY_TYPE_ALI);
                return alipayCard;
            case PAY_TYPE_ALIHK:
                PayCardItem alipayhkCard = new PayCardItem("AlipayHK", R.drawable.pay_vendor_bg_blue, R.drawable.new_icon_pay_alipay_hk, PayCardItem.PAY_TYPE_ALIHK);
                return alipayhkCard;
            default:
                return null;

        }
    }

    @NonNull
    @Override
    public String toString() {
        String str = String.format("name %s,showMask %s,status %s,showBalance %s", this.payName, this.showMask, this.status, this.showBalance);
        return str;
    }

    public String getPayDesc() {
        return payDesc;
    }

    public String getPayName() {
        return payName;
    }

    public int getPayBgRid() {
        return payBgRid;
    }

    public int getPayIconRid() {
        return payIconRid;
    }

    private String payDesc;
    private String payName;
    private int payBgRid;
    private int payIconRid;

    public PayCardItem(String payName, int payBgRid, int payIconRid, int payType) {
        this.payName = payName;
        this.payBgRid = payBgRid;
        this.payIconRid = payIconRid;
        this.payType = payType;
    }

    public void setActivityDesc(String payActivityDesc) {
        this.payDesc = payActivityDesc;
    }
}
