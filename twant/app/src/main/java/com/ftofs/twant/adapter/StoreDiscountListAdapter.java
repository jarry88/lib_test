package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreDiscount;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;

public class StoreDiscountListAdapter extends ViewGroupAdapter<StoreDiscount> {

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public StoreDiscountListAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);
        addClickableChildrenId(R.id.btn_participate_activity);
    }

    @Override
    public void bindView(int position, View itemView, StoreDiscount itemData) {
        setText(itemView, R.id.tv_discount_name, itemData.discountName);
        setText(itemView, R.id.tv_goods_count, String.valueOf(itemData.goodsCount));
        setText(itemView, R.id.tv_discount_rate, StringUtil.formatFloat(itemData.discountRate) + "折");


        int dayCount = itemData.countDownTime / 86400;  // 剩余多少天
        setText(itemView, R.id.tv_day_count, String.valueOf(dayCount));
        int remainDay =itemData.countDownTime%86400;
        SLog.info("dayCount %d",itemData.countDownTime%86400);
        int hourCount = remainDay / 3600;
        int remainHour = remainDay % 3600;
        int minCount = remainHour / 60;
        int remainMin = remainHour % 60;
        int remainSec = remainMin / 60;
        int secCount  = remainMin % 60;
        String hour = String.valueOf(hourCount);
        String minute = String.valueOf(minCount);
        String sec = String.valueOf(secCount);

        Jarbon jarbon = new Jarbon((itemData.countDownTime % 86400) * 1000);
        String timeRemain = hour+":"+minute+":"+sec;
        setText(itemView, R.id.tv_remain_time, timeRemain);
    }
}
