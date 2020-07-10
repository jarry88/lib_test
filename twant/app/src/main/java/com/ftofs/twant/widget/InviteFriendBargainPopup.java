package com.ftofs.twant.widget;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

public class InviteFriendBargainPopup extends CenterPopupView implements View.OnClickListener {
    Context context;

    public InviteFriendBargainPopup(@NonNull Context context) {
        super(context);

        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.invite_friend_bargain_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
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
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * .8f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok || id == R.id.btn_close) {
            dismiss();
        }
    }
}

