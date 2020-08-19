package com.ftofs.twant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.OrderOperation;
import com.ftofs.twant.entity.EvaluationGoodsItem;
import com.ftofs.twant.entity.OrderItem;
import com.ftofs.twant.entity.OrderSkuItem;
import com.ftofs.twant.entity.PayItem;
import com.ftofs.twant.fragment.GoodsEvaluationFragment;
import com.ftofs.twant.fragment.OrderDetailFragment;
import com.ftofs.twant.fragment.OrderFragment;
import com.ftofs.twant.fragment.OrderLogisticsInfoFragment;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.orders.OrdersGoodsVo;
import com.ftofs.twant.widget.CancelAfterVerificationListPopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.ftofs.twant.widget.VerificationPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;

import org.litepal.util.Const;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 訂單列表Adapter(顯示地方: 訂單列表的【全部】、【待付款】和查詢訂單Fragment中顯示)
 * 每個支付單號一個PayItem，一個PayItem包含一個或多個OrderItem(OrderItem與商店是一一對應的關系)，一個OrderItem包含若干個SkuItem(不直接包含SpuItem)
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
                        // newInstance(storeId, storeName, orderDetailGoodsItemList)
                        List<EvaluationGoodsItem> evaluationGoodsItemList = new ArrayList<>();
                        for (OrderSkuItem orderSkuItem : orderItem.orderSkuItemList) {
                            EvaluationGoodsItem evaluationGoodsItem = new EvaluationGoodsItem(orderSkuItem.commonId, orderSkuItem.imageSrc,
                                    orderSkuItem.goodsName, orderSkuItem.goodsFullSpecs);
                            evaluationGoodsItemList.add(evaluationGoodsItem);
                        }
                        Util.startFragment(GoodsEvaluationFragment.newInstance(orderItem.orderId, 0, orderItem.storeName, evaluationGoodsItemList));
                    } else if (id == R.id.btn_have_received) {
                        SLog.info("btn_have_received");
                        if (Config.USE_DEVELOPER_TEST_DATA) {
//                                    cancelAfterVerification(orderItem);
                            loadGoodsList(orderItem);
                        } else if (Constant.WANT_EAT.equals(orderItem.storeName)) {
                            loadGoodsList(orderItem);
                        } else {
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

                                }
                            }))
                                    .show();
                        }

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
                TextView payOrder=helper.getView(R.id.btn_pay_order);
                payOrder.setText("支付訂單 $ " + StringUtil.formatFloat(item.payAmount));
                UiUtil.toPriceUI(payOrder, 0,5,6);
                helper.setGone(R.id.btn_pay_order, true);
            } else {
                helper.setGone(R.id.btn_pay_order, false);
            }
        } else {

        }
    }

    private void cancelAfterVerification(OrderItem item) {

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
                }).asCustom(CancelAfterVerificationListPopup.Companion.newInstance(context, null)).show();
//        ToastUtil.success(context,"進入新彈窗");
    }
    private void loadGoodsList(OrderItem item) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token,"ordersId",item.orderId);

        SLog.info("params[%s]", params);
        final BasePopupView loadingPopup = Util.createLoadingPopup(context).show();

        Api.postUI(Api.PATH_IFOODMACAU_GOODS_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);
                loadingPopup.dismiss();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(context, responseObj)) {
                    return;
                }

                /*
                all: 全部
                new: 待付款
                pay: 待發貨
                finish: 已完成
                send: 待收貨
                noeval: 待評論
                 */
               try {
                   EasyJSONArray ordersGoodsVoList = responseObj.getSafeArray("datas.ordersGoodsVoList");
                   List<OrdersGoodsVo> list = new ArrayList<>();
                   for (Object object : ordersGoodsVoList) {
                       list.add(OrdersGoodsVo.parse((EasyJSONObject) object));
                   }
                   if (list.isEmpty()) {
                       //todo異常情況處理
//                       ToastUtil.error(context,);
                       return;
                   }
                   boolean directVerification=false;
                   if (list.size() == 1 && list.get(0).getIfoodmacauCount() == 1) {
                       directVerification = true;
                   }
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
                                   orderFragment.outReloadData();
                               }
                           }).asCustom(directVerification? new VerificationPopup(context,list.get(0),1):new CancelAfterVerificationListPopup(context, list,item))
                           .show();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
}
