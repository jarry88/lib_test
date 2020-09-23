package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;

import com.ftofs.twant.R;
import com.gzp.lib_common.constant.PopupType;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.lxj.xpopup.core.BottomPopupView;

public class BottomConfirmPopup extends BottomPopupView implements View.OnClickListener {
    OnSelectedListener onSelectedListener;
    public BottomConfirmPopup(@NonNull Context context, OnSelectedListener onSelectedListener) {
        super(context);

        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_change).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.bottom_confirm_popup;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_change) {
            onSelectedListener.onSelected(PopupType.SELECT_PERSONAL_BACKGROUND, 0, null);
            dismiss();
        } else if (id == R.id.btn_cancel) {
            dismiss();
        }
    }
}
