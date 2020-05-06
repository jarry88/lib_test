package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.ChatFragment;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.seller.adapter.SellerOrderDetailGoodsAdapter;
import com.ftofs.twant.seller.adapter.SellerOrderStatusAdapter;
import com.ftofs.twant.seller.entity.SellerOrderDetailGoodsItem;
import com.ftofs.twant.seller.entity.SellerOrderItem;
import com.ftofs.twant.seller.entity.SellerOrderSkuItem;
import com.ftofs.twant.seller.entity.SellerOrderStatus;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.hyphenate.chat.EMConversation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商家訂單詳情頁面
 * @author zwm
 */
public class SellerOrderDetailFragment extends BaseFragment implements View.OnClickListener {
    int orderId;
    String orderSn;
    String buyerNickname;
    String buyerMemberName;

    List<SellerOrderDetailGoodsItem> sellerOrderDetailGoodsItemList = new ArrayList<>();

    public static SellerOrderDetailFragment newInstance(int orderId) {
        Bundle args = new Bundle();

        SellerOrderDetailFragment fragment = new SellerOrderDetailFragment();
        fragment.setArguments(args);
        fragment.setParams(orderId);

        return fragment;
    }

    public void setParams(int orderId) {
        this.orderId = orderId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_order_detail, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_copy_order_sn, this);
        Util.setOnClickListener(view, R.id.btn_chat_with_user, this);
        Util.setOnClickListener(view, R.id.btn_ship, this);

        loadData();
    }


    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_ORDER_DETAIL + "/" + orderId;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        SLog.info("url[%s], params[%s]", url, params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }

                    EasyJSONObject ordersVo = responseObj.getSafeObject("datas.ordersVo");

                    ((TextView) contentView.findViewById(R.id.tv_order_status_desc)).setText(ordersVo.getSafeString("ordersStateName"));

                    String paymentInfoStr = String.format("買家已使用「%s」方式成功對訂單進行支付，支付單號 「%s」。",
                            ordersVo.getSafeString("paymentName"), ordersVo.getSafeString("paySnText"));
                    ((TextView) contentView.findViewById(R.id.tv_payment_info)).setText(paymentInfoStr);

                    orderSn = ordersVo.getSafeString("ordersSnText");
                    ((TextView) contentView.findViewById(R.id.tv_order_sn)).setText(orderSn);

                    buyerNickname = ordersVo.getSafeString("nickName");
                    buyerMemberName = ordersVo.getSafeString("memberName");

                    String buyerInfo = String.format("%s (%s)", buyerNickname, buyerMemberName);
                    ((TextView) contentView.findViewById(R.id.btn_buyer_info)).setText(buyerInfo);

                    String receiverInfo = ordersVo.getSafeString("receiverName") + " " + ordersVo.getSafeString("receiverPhone")
                            + " " + ordersVo.getSafeString("receiverAreaInfo") + " " + ordersVo.getSafeString("receiverAddress");
                    ((TextView) contentView.findViewById(R.id.tv_receiver)).setText(receiverInfo);

                    ((TextView) contentView.findViewById(R.id.tv_ship_time)).setText(ordersVo.getSafeString("shipTime"));

                    ((TextView) contentView.findViewById(R.id.tv_receiver_message)).setText(ordersVo.getSafeString("receiverMessage"));

                    String payWay = String.format("%s（付款單號：%s）",
                            ordersVo.getSafeString("paymentName"), ordersVo.getSafeString("paySnText"));
                    ((TextView) contentView.findViewById(R.id.tv_pay_way)).setText(payWay);

                    LinearLayout llOrderStatusContainer = contentView.findViewById(R.id.ll_order_status_container);
                    SellerOrderStatusAdapter adapter = new SellerOrderStatusAdapter(_mActivity, llOrderStatusContainer, R.layout.seller_order_status_item);

                    List<SellerOrderStatus> sellerOrderStatusList = new ArrayList<>();
                    String createTime = ordersVo.getSafeString("createTime");
                    sellerOrderStatusList.add(new SellerOrderStatus("提交訂單", createTime));
                    String payTime = ordersVo.getSafeString("paymentTime");
                    sellerOrderStatusList.add(new SellerOrderStatus("完成付款", payTime));
                    String sendTime = ordersVo.getSafeString("sendTime");
                    sellerOrderStatusList.add(new SellerOrderStatus("商家發貨", sendTime));
                    String receiveTime = ordersVo.getSafeString("finishTime");
                    sellerOrderStatusList.add(new SellerOrderStatus("確認收貨", receiveTime));
                    String evaluationTime = ordersVo.getSafeString("evaluationTime");
                    sellerOrderStatusList.add(new SellerOrderStatus("評價", evaluationTime));

                    for (int i = sellerOrderStatusList.size() - 1; i >= 0; i--) {
                        SellerOrderStatus item = sellerOrderStatusList.get(i);

                        if (!StringUtil.isEmpty(item.timestamp)) {
                            item.isLatestStatus = true;
                            break;
                        }
                    }
                    adapter.setData(sellerOrderStatusList);

                    LinearLayout llSellerOrderDetailGoodsContainer = contentView.findViewById(R.id.ll_seller_order_detail_goods_container);
                    SellerOrderDetailGoodsAdapter goodsAdapter = new SellerOrderDetailGoodsAdapter(_mActivity, llSellerOrderDetailGoodsContainer, R.layout.seller_order_detail_goods_item);
                    goodsAdapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
                        @Override
                        public void onClick(ViewGroupAdapter adapter, View view, int position) {
                            SellerOrderDetailGoodsItem item = sellerOrderDetailGoodsItemList.get(position);
                            Util.startFragment(GoodsDetailFragment.newInstance(item.commonId, 0));
                        }
                    });


                    EasyJSONArray ordersGoodsList = ordersVo.getArray("ordersGoodsList");
                    for (Object object : ordersGoodsList) {
                        SellerOrderDetailGoodsItem item = (SellerOrderDetailGoodsItem) EasyJSONBase.jsonDecode(SellerOrderDetailGoodsItem.class, object.toString());
                        sellerOrderDetailGoodsItemList.add(item);
                    }

                    goodsAdapter.setData(sellerOrderDetailGoodsItemList);

                    double ordersAmount = ordersVo.getDouble("ordersAmount");
                    ((TextView) contentView.findViewById(R.id.tv_orders_amount)).setText(StringUtil.formatPrice(_mActivity, ordersAmount, 0));

                    ((TextView) contentView.findViewById(R.id.tv_ship_name)).setText(ordersVo.getSafeString("shipName"));
                    ((TextView) contentView.findViewById(R.id.tv_ship_sn)).setText(ordersVo.getSafeString("shipSn"));
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_copy_order_sn) {
            SLog.info("复制订单号[%s]", orderSn);
            ClipboardUtils.copyText(_mActivity, orderSn);
            ToastUtil.success(_mActivity, "訂單編號已複製");
        } else if (id == R.id.btn_chat_with_user) {
            String avatarUrl = "";

            FriendInfo friendInfo = new FriendInfo();
            friendInfo.memberName = buyerMemberName;
            friendInfo.nickname = buyerNickname;
            friendInfo.avatarUrl = avatarUrl;
            friendInfo.role = ChatUtil.ROLE_MEMBER;
            EMConversation conversation = ChatUtil.getConversation(buyerMemberName, buyerNickname, avatarUrl, ChatUtil.ROLE_CS_AVAILABLE);

            Util.startFragment(ChatFragment.newInstance(conversation, friendInfo));
        } else if (id == R.id.btn_ship) {
            Util.startFragment(SellerOrderShipFragment.newInstance(orderId, orderSn));
        }
    }
}

