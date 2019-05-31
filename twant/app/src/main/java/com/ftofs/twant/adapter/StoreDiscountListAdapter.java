package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.entity.StoreDiscount;
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
        String discountTitle = String.format(context.getString(R.string.text_discount_title_template),
                StringUtil.formatFloat(itemData.discountRate), itemData.goodsCount);
        setText(itemView, R.id.tv_discount_title, discountTitle);
    }
}
