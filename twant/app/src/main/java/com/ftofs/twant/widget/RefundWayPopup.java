package com.ftofs.twant.widget;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * 退款方式選擇彈窗
 * @author zwm
 */
public class RefundWayPopup  extends BottomPopupView implements View.OnClickListener {
    public static final int REFUND_WAY_WALLET = 1;   // 退至預存款
    public static final int REFUND_WAY_ORIGINAL = 2; // 原路退回

    Context context;
    OnSelectedListener onSelectedListener;
    int refundWay;

    public RefundWayPopup(@NonNull Context context, OnSelectedListener onSelectedListener, int refundWay) {
        super(context);

        this.context = context;
        this.onSelectedListener = onSelectedListener;
        this.refundWay = refundWay;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.refund_way_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        findViewById(R.id.btn_refund_to_wallet).setOnClickListener(this);
        findViewById(R.id.btn_refund_to_original).setOnClickListener(this);
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
        if (id == R.id.btn_dismiss) {
            dismiss();
        } else if (id == R.id.btn_refund_to_wallet) {
            onSelectedListener.onSelected(PopupType.SELECT_REFUND_WAY, REFUND_WAY_WALLET, null);
            dismiss();
        } else if (id == R.id.btn_refund_to_original) {
            onSelectedListener.onSelected(PopupType.SELECT_REFUND_WAY, REFUND_WAY_ORIGINAL, null);
            dismiss();
        }
    }
}

