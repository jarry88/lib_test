package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.interfaces.MobileZoneSelectedListener;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.MobileZoneAdapter;
import com.ftofs.twant.entity.MobileZone;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.util.List;


/**
 * 支付方式選擇
 * @author zwm
 */
public class PayWayPopup extends BottomPopupView implements View.OnClickListener {
    Context context;

    private int[] buttonIds = new int[] {R.id.btn_pay_online, R.id.btn_pay_delivery, R.id.btn_pay_fetch};
    private int[] textIds = new int[] {R.id.text_pay_online, R.id.text_pay_delivery, R.id.text_pay_fetch};
    private int[] indcatorIds = new int[] {R.id.ind_pay_online, R.id.ind_pay_delivery, R.id.ind_pay_fetch};

    int payWay;
    OnSelectedListener onSelectedListener;
    
    public PayWayPopup(@NonNull Context context, int payWay, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.payWay = payWay;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pay_way_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        for (int buttonId : buttonIds) {
            findViewById(buttonId).setOnClickListener(this);
        }

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        // 高亮文本
        TextView tvTextPay = findViewById(textIds[payWay]);
        tvTextPay.setTextColor(getResources().getColor(R.color.tw_red, null));
        findViewById(indcatorIds[payWay]).setVisibility(VISIBLE);
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
        SLog.info("id[%d]", id);
        int index = 0;
        for (int buttonId : buttonIds) {
            SLog.info("id[%d], buttonId[%d]", id, buttonId);
            if (id == buttonId) {
                onSelectedListener.onSelected(index);
                dismiss();
                return;
            }
            ++index;
        }

        if (id == R.id.btn_dismiss) {
            dismiss();
        }
    }
}
