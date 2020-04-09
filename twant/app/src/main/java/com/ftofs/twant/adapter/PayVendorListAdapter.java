package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.PayCardItem;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.libra.Utils;

import java.util.List;

import javax.annotation.Nullable;

public class PayVendorListAdapter extends BaseQuickAdapter<PayCardItem, BaseViewHolder> {

    public PayVendorListAdapter(int layout, @Nullable List<PayCardItem> data) {
        super(layout,data);
    }
    @Override
    protected void convert(BaseViewHolder helper, PayCardItem item) {
        helper.getView(R.id.rv_pay_card_item).setBackgroundResource(item.getPayBgRid());
        ImageView imageView = helper.getView(R.id.img_pay_icon);
        Glide.with(mContext).load(item.getPayIconRid()).centerInside().into(imageView);
        helper.setText(R.id.tv_balance_name, item.getPayName());
        helper.setGone(R.id.ll_pay_balance, false);
        helper.setGone(R.id.ll_cashier_alipay_pay,false);
        helper.setGone(R.id.ll_cashier_alipayHK_pay,false);
        helper.setGone(R.id.ll_cashier_wechat_pay,false);
        helper.setGone(R.id.ll_cashier_taifung_pay,false);
        helper.setGone(R.id.ll_cashier_mpay_pay,false);
        if (item.payType == PayCardItem.PAY_TYPE_TAIFUNG) {
            helper.setVisible(R.id.ll_cashier_taifung_pay, true);
        } else if (item.payType == PayCardItem.PAY_TYPE_MPAY) {
            helper.setVisible(R.id.ll_cashier_mpay_pay, true);
            if (item.getPayDesc() != null &&item.showActivityDesc) {
                helper.setText(R.id.tv_pay_activity_indicator, item.getPayDesc());
                helper.setVisible(R.id.icon_pay_activity_label, true);
            }
        }
        else if (item.payType == PayCardItem.PAY_TYPE_ALIHK) {
            helper.setVisible(R.id.ll_cashier_alipayHK_pay, true);
        }else if (item.payType == PayCardItem.PAY_TYPE_ALI) {
            helper.setVisible(R.id.ll_cashier_alipay_pay, true);

        }else if (item.payType == PayCardItem.PAY_TYPE_WEIXING) {
            helper.setVisible(R.id.ll_cashier_wechat_pay, true);

        }else if (item.payType == PayCardItem.PAY_TYPE_WALLET) {
            helper.setVisible(R.id.ll_pay_balance, true);
            if (StringUtil.isEmpty(item.textBalance)) {
                helper.setVisible(R.id.tv_balance, false);
            } else if (item.showBalance) {
                helper.setVisible(R.id.tv_balance, true);
                helper.setText(R.id.tv_balance, item.textBalance);
            }

        }


        RelativeLayout view=helper.getView(R.id.rv_pay_card_item);
        helper.setVisible(R.id.mask_pay, item.showMask);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (item.status == Constant.STATUS_SELECTED) {
            if (helper.getAdapterPosition() == 0) {
//                layoutParams.topMargin = Util.dip2px(mContext,80);
            }
            view.setLayoutParams(layoutParams);
            view.setRotation(6);
        } else {
            view.setLayoutParams(layoutParams);
            if (view.getRotation() > 0) {
                view.setRotation(0);
            }
        }

    }
}
