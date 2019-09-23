package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.entity.PayItem;
import com.ftofs.twant.fragment.OrderDetailFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;

import java.util.List;

/**
 * 訂單列表Adapter(顯示地方: 訂單列表的【全部】、【待付款】和查詢訂單Fragment中顯示)
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

        OrderItemListAdapter adapter = new OrderItemListAdapter(mContext, llOrderListContainer, R.layout.order_item, item.showPayButton);
        adapter.setChildClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                OrderItem orderItem = item.orderItemList.get(position);
                int id = view.getId();

                if (id == R.id.btn_cancel_order) {
                    SLog.info("btn_cancel_order");
                } else if (id == R.id.btn_buy_again) {
                    SLog.info("btn_buy_again");
                } else if (id == R.id.btn_view_logistics) {
                    SLog.info("btn_view_logistics");
                } else if (id == R.id.btn_order_comment) {
                    SLog.info("btn_order_comment");
                }
            }
        });
        adapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                OrderItem orderItem = item.orderItemList.get(position);
                Util.startFragment(OrderDetailFragment.newInstance(orderItem.orderId));
            }
        });
        adapter.setData(item.orderItemList);

        if (item.showPayButton) {
            // 子View點擊事件
            helper.addOnClickListener(R.id.btn_pay_order);
            helper.setText(R.id.btn_pay_order, "支付訂單 " + String.format("%.2f", item.payAmount));
            helper.setGone(R.id.btn_pay_order, true);
        } else {
            helper.setGone(R.id.btn_pay_order, false);
        }
    }
}
