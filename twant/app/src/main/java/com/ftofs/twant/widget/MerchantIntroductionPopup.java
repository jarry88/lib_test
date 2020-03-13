package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

public class MerchantIntroductionPopup extends BottomPopupView implements View.OnClickListener {
    Context context;

    String introduction;

    public MerchantIntroductionPopup(@NonNull Context context, String introduction) {
        super(context);

        this.context = context;
        this.introduction = introduction;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.merchant_introduction_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        TextView tvIntroduction = findViewById(R.id.tv_introduction);
        tvIntroduction.setText(introduction);
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
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        }
    }
}

