package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

/**
 * 打折標簽
 * @author zwm
 */
public class SlantedWidget extends RelativeLayout {
    TextView tvOriginalPrice;
    TextView tvDiscount;

    public SlantedWidget(Context context) {
        this(context, null);
    }

    public SlantedWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlantedWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LinearLayout llPriceInfo = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.discount_price, null, false);

        tvDiscount = llPriceInfo.findViewById(R.id.tv_discount);
        tvOriginalPrice = llPriceInfo.findViewById(R.id.tv_original_price);
        // 原價顯示刪除線
        tvOriginalPrice.setPaintFlags(tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        llPriceInfo.setRotation(-45);

        RelativeLayout.MarginLayoutParams layoutParams = new RelativeLayout.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = Util.dip2px(context, 5);
        layoutParams.topMargin = Util.dip2px(context, 1);
        addView(llPriceInfo, layoutParams);

        // setPadding(Util.dip2px(context, 5), Util.dip2px(context, 5), 0, 0);

        setBackgroundResource(R.drawable.discount_label);
    }


    public void setDiscountInfo(Context context, double discount, double originalPrice) {
        tvDiscount.setText(String.valueOf(discount));
        tvOriginalPrice.setText(StringUtil.formatPrice(context, originalPrice, 1,false));
    }
}



