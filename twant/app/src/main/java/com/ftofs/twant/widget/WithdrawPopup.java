package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

public class WithdrawPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    double withdrawAmount;

    public WithdrawPopup(@NonNull Context context, double withdrawAmount) {
        super(context);

        this.context = context;
        this.withdrawAmount = withdrawAmount;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_withdraw).setOnClickListener(this);

        TextView tvWithdrawAmount = findViewById(R.id.tv_withdraw_amount);
        tvWithdrawAmount.setText(StringUtil.formatFloat(withdrawAmount));

        View btnWithdraw = findViewById(R.id.btn_withdraw);
        float radius = Util.dip2px(context, 8);
        btnWithdraw.setBackground(BackgroundDrawable.create(Color.WHITE, new float[] {0, 0, radius, radius}));
    }



    @Override
    protected int getImplLayoutId() {
        return R.layout.withdraw_popup;
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 0.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_withdraw) {
            SLog.info("全部提現");
        }
    }
}
