package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ftofs.twant.R;
import com.lxj.xpopup.core.BottomPopupView;


public class InviteAddFriendPopup extends BottomPopupView implements View.OnClickListener {
    EditText etRemark;


    public InviteAddFriendPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.invite_add_friend_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dismiss) {
            dismiss();
        } else if (id == R.id.btn_send_invitation) {
            dismiss();

            String remark = etRemark.getText().toString();

        }
    }

//    @Override
//    protected int getMaxHeight() {
//        return (int) (XPopupUtils.getWindowHeight(getContext())*0.75);
//    }
}
