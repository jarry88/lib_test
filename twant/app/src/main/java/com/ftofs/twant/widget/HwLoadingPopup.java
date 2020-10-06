package com.ftofs.twant.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

public class HwLoadingPopup extends CenterPopupView implements View.OnClickListener {
    Context context;

    TextView tvInfo;
    String info;

    public HwLoadingPopup(@NonNull Context context, String info) {
        super(context);
        this.info = info;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.hw_loading_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        tvInfo = findViewById(R.id.tv_info);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();

        tvInfo.setText(info);
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 1f);
    }

    @Override
    public void onClick(View v) {
    }

    public void setInfo(String info) {
        this.info = info;
    }
}



