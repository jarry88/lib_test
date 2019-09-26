package com.ftofs.twant.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.OrderOperation;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.entity.PayItem;
import com.ftofs.twant.fragment.GoodsEvaluationFragment;
import com.ftofs.twant.fragment.OrderDetailFragment;
import com.ftofs.twant.fragment.OrderFragment;
import com.ftofs.twant.fragment.OrderLogisticsInfoFragment;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 訂單列表Adapter(顯示地方: 訂單列表的【全部】、【待付款】和查詢訂單Fragment中顯示)
 * 每個支付單號一個PayItem，一個PayItem包含一個或多個OrderItem(OrderItem與店鋪是一一對應的關系)，一個OrderItem包含若干個SkuItem(不直接包含SpuItem)
 * @author zwm
 */
public class PayItemListAdapter extends BaseMultiItemQuickAdapter<PayItem, BaseViewHolder> {
    Context context;
    String currencyTypeSign;
    String timesSign;
    OrderFragment orderFragment;

    public PayItemListAdapter(Context context, List data, OrderFragment orderFragment) {
        super(data);
        this.context = context;
        this.orderFragment = orderFragment;

        addItemType(Constant.ITEM_TYPE_NORMAL, R.layout.pay_item);
        addItemType(Constant.ITEM_TYPE_LOAD_END_HINT, R.layout.load_end_hint);

        currencyTypeSign = context.getResources().getString(R.string.currency_type_sign);
        timesSign = context.getResources().getString(R.string.times_sign);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayItem item) {
        int itemType = item.getItemType();
        if (itemType == Constant.ITEM_TYPE_NORMAL) {
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
                        String confirmText = "確定要取消訂單嗎?";
                        new XPopup.Builder(context)
//                         .dismissOnTouchOutside(false)
                                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                                .setPopupCallback(new XPopupCallback() {
                                    @Override
                                    public void onShow() {
                                    }
                                    @Override
                                    public void onDismiss() {
                                    }
                                }).asCustom(new TwConfirmPopup(context, confirmText, null, new OnConfirmCallback() {
                            @Override
                            public void onYes() {
                                SLog.info("onYes");
                                orderFragment.orderOperation(OrderOperation.ORDER_OPERATION_TYPE_CANCEL, orderItem.orderId);
                            }

                            @Override
                            public void onNo() {
                                SLog.info("onNo");
                            }
                        }))
                                .show();
                    } else if (id == R.id.btn_buy_again) {
                        SLog.info("btn_buy_again");
                        orderFragment.orderOperation(OrderOperation.ORDER_OPERATION_TYPE_BUY_AGAIN, orderItem.orderId);
                    } else if (id == R.id.btn_view_logistics) {
                        SLog.info("btn_view_logistics");
                        Util.startFragment(OrderLogisticsInfoFragment.newInstance(orderItem.orderId));
                    } else if (id == R.id.btn_order_comment) {
                        SLog.info("btn_order_comment");
                        Util.startFragment(GoodsEvaluationFragment.newInstance());
                    } else if (id == R.id.btn_have_received) {
                        SLog.info("btn_have_received");
                        new XPopup.Builder(context)
//                         .dismissOnTouchOutside(false)
                                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                                .setPopupCallback(new XPopupCallback() {
                                    @Override
                                    public void onShow() {
                                    }
                                    @Override
                                    public void onDismiss() {
                                    }
                                }).asCustom(new TwConfirmPopup(context, "確認收貨嗎?", null, new OnConfirmCallback() {
                            @Override
                            public void onYes() {
                                SLog.info("onYes");
                                orderFragment.confirmReceive(orderItem.orderId);
                            }

                            @Override
                            public void onNo() {
                                SLog.info("onNo");
                            }
                        }))
                                .show();
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
        } else {

        }
    }
}
