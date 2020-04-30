package com.ftofs.twant.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.OrderState;
import com.ftofs.twant.entity.order.OrderDetailGoodsItem;
import com.ftofs.twant.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Comparator.*;

/**
 * 訂單詳情頁面的產品列表Adapter
 * @author zwm
 */
public class OrderDetailGoodsAdapter extends ViewGroupAdapter<OrderDetailGoodsItem> {
    private Stream<View> btnViews;
    Context context;
    String timesSign;

    /**
     * 構造方法
     *
     * @param context
     * @param container    容器
     * @param itemLayoutId itemView的布局Id
     */
    public OrderDetailGoodsAdapter(Context context, ViewGroup container, int itemLayoutId) {
        super(context, container, itemLayoutId);

        this.context = context;
        timesSign = context.getString(R.string.times_sign);
        addClickableChildrenId(R.id.btn_goto_goods, R.id.btn_refund, R.id.btn_refund_all, R.id.btn_refund_waiting, R.id.btn_return, R.id.btn_view_complaint, R.id.btn_complain);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void bindView(int position, View itemView, OrderDetailGoodsItem itemData) {
        ImageView goodsImage = itemView.findViewById(R.id.goods_image);
        Glide.with(context).load(itemData.imageSrc).centerCrop().into(goodsImage);
        setText(itemView, R.id.tv_goods_name, itemData.goodsName);
        setText(itemView, R.id.tv_goods_full_specs, itemData.goodsFullSpecs);
        setText(itemView, R.id.tv_goods_price_left, StringUtil.formatPrice(context, itemData.goodsPrice, 0,2));
        setText(itemView, R.id.tv_buy_item_amount, timesSign + " " + itemData.buyNum);
        setText(itemView,R.id.tv_goods_amount,StringUtil.formatPrice(context,itemData.goodsPrice,0,2));
        View btnRefund = itemView.findViewById(R.id.btn_refund);
        View btnRefundAll = itemView.findViewById(R.id.btn_refund_all);
        View btnRefundWaiting = itemView.findViewById(R.id.btn_refund_waiting);
        View btnReturn = itemView.findViewById(R.id.btn_return);
        View btnComplain = itemView.findViewById(R.id.btn_complain);
        View btnViewComplaint = itemView.findViewById(R.id.btn_view_complaint);

        if (itemData.refundType == 0) {

            if (itemData.showRefundWaiting == Constant.TRUE_INT) {
                btnRefundWaiting.setVisibility(View.VISIBLE);
            } else {
                if (itemData.showRefund == 1) {
                    btnRefund.setVisibility(View.VISIBLE);
                    btnReturn.setVisibility(View.VISIBLE);
                } else {
                    btnRefund.setVisibility(View.GONE);
                    btnReturn.setVisibility(View.GONE);
                }

                if (itemData.orderState == OrderState.TO_BE_SEND) {
                    btnRefund.setVisibility(View.GONE);
                    btnRefundAll.setVisibility(View.VISIBLE);
                    btnReturn.setVisibility(View.GONE);
                } else if (itemData.orderState == OrderState.TO_BE_RECEIVE) {
                    btnRefund.setVisibility(View.VISIBLE);
                    btnRefundAll.setVisibility(View.GONE);
                    btnReturn.setVisibility(View.VISIBLE);
                }
            }

        } else if (itemData.refundType == 1) { // 查看退款

        } else if (itemData.refundType == 2) { // 查看退貨

        }

        if (itemData.showMemberComplain == 1) {
            if (itemData.complainId == 0) {
                btnComplain.setVisibility(View.VISIBLE);
                btnViewComplaint.setVisibility(View.GONE);
            } else {
                btnComplain.setVisibility(View.GONE);
                btnViewComplaint.setVisibility(View.VISIBLE);
            }
        }
        btnViews = Stream.of(btnViewComplaint,btnComplain,btnReturn,btnRefundWaiting,btnRefundAll,btnRefund);
        AtomicInteger index=new AtomicInteger(0);
        btnViews.filter(view -> view.getVisibility()==View.VISIBLE).forEach(view->{
            if (index.getAndIncrement() % 2 == 1) {
                view.setBackgroundResource(R.drawable.smaller_outline_button);
            } else {
                view.setBackgroundResource(R.drawable.smaller_outline_button_revert);
            }
        });

    }
}


