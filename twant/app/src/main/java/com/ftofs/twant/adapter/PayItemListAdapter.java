package com.ftofs.twant.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.PayItem;

import java.util.List;

/**
 * 訂單列表Adapter
 * 每個支付單號一個PayItem，一個PayItem包含一個或多個OrderItem(OrderItem與店鋪是一一對應的關系)，一個OrderItem包含若干個SkuItem(不直接包含SpuItem)
 * @author zwm
 */
public class PayItemListAdapter extends BaseQuickAdapter<PayItem, BaseViewHolder> {
    Context context;
    String currencyTypeSign;
    String timesSign;

    public PayItemListAdapter(Context context, int layoutResId, List data) {
        super(layoutResId, data);
        this.context = context;
        currencyTypeSign = context.getResources().getString(R.string.currency_type_sign);
        timesSign = context.getResources().getString(R.string.times_sign);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayItem item) {
        LinearLayout llOrderListContainer = helper.getView(R.id.ll_order_list_container);
        llOrderListContainer.removeAllViews();

        OrderItemListAdapter adapter = new OrderItemListAdapter(mContext, llOrderListContainer, R.layout.order_item);
        adapter.setData(item.orderItemList);

        // 暫時屏蔽支付按鈕
        if (item.showPayButton) {
            // 子View點擊事件
            helper.addOnClickListener(R.id.btn_pay_order);
        } else {
            helper.setGone(R.id.btn_pay_order, false);
        }
    }
}
