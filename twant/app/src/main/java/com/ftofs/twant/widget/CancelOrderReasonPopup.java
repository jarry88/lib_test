package com.ftofs.twant.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CancelOrderReasonListAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.entity.CancelOrderReason;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONObject;

public class CancelOrderReasonPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    int orderId;
    String orderSN;
    EasyJSONObject reasonMap;
    SimpleCallback simpleCallback;

    CancelOrderReasonListAdapter adapter;
    int currSelectedPosition = -1;  // 最近一次選中的位置，-1表示未選中任何項

    List<CancelOrderReason> cancelOrderReasonList = new ArrayList<>();

    public CancelOrderReasonPopup(@NonNull Context context, int orderId, String orderSN, EasyJSONObject reasonMap, SimpleCallback simpleCallback) {
        super(context);

        this.orderId = orderId;
        this.orderSN = orderSN;
        this.context = context;
        this.reasonMap = reasonMap;
        this.simpleCallback = simpleCallback;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.cancel_order_reason_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        for (Map.Entry<String, Object> entry : reasonMap.entrySet()) {
            CancelOrderReason reason = new CancelOrderReason(entry.getKey(), (String) entry.getValue());
            cancelOrderReasonList.add(reason);
        }

        TextView tvOrderSN = findViewById(R.id.tv_order_sn);
        tvOrderSN.setText(String.format("(訂單號：%s)", orderSN));

        findViewById(R.id.btn_ok).setOnClickListener(this);

        LinearLayout llContainer = findViewById(R.id.ll_container);

        adapter = new CancelOrderReasonListAdapter(context, llContainer, R.layout.cancel_order_reason_item);
        adapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                if (position == currSelectedPosition) {
                    return;
                }

                CancelOrderReason reason;
                if (currSelectedPosition > 0) {
                    reason = cancelOrderReasonList.get(currSelectedPosition);
                    reason.selected = false;
                    adapter.notifyItemChanged(currSelectedPosition);
                }
                

                reason = cancelOrderReasonList.get(position);
                reason.selected = true;
                adapter.notifyItemChanged(position);

                currSelectedPosition = position;
            }
        });
        adapter.setData(cancelOrderReasonList);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_ok) {
            SLog.info("currSelectedPosition[%d]", currSelectedPosition);
            if (currSelectedPosition < 0) {
                ToastUtil.error(context, "請選擇取消原因");
                return;
            }
            
            if (simpleCallback != null) {
                simpleCallback.onSimpleCall(EasyJSONObject.generate(
                        "action", SimpleCallback.ACTION_SELECT_CANCEL_ORDER_REASON,
                        "reason_id", cancelOrderReasonList.get(currSelectedPosition).id,
                        "order_id", orderId
                ));
            }

            dismiss();
        }
    }
}

