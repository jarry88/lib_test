package com.ftofs.twant.seller.widget;


import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.fragment.ChatFragment;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * 商家版買家信息彈窗
 * @author zwm
 */
public class BuyerInfoPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    Activity activity;

    String userPhoneNumber;
    String receiverPhoneNumber;

    String memberName;
    String nickname;

    public BuyerInfoPopup(@NonNull Context context, Activity activity) {
        super(context);

        this.context = context;
        this.activity = activity;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.buyer_info_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_chat_with_user).setOnClickListener(this);
        findViewById(R.id.btn_dial_user_phone).setOnClickListener(this);
        findViewById(R.id.btn_dial_receiver_phone).setOnClickListener(this);
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
        } else if (id == R.id.btn_dial_user_phone || id == R.id.btn_dial_receiver_phone) {
            String phoneNumber;
            if (id == R.id.btn_dial_user_phone) {
                phoneNumber = userPhoneNumber;
            } else {
                phoneNumber = receiverPhoneNumber;
            }

            Util.dialPhone(activity, phoneNumber);
        } else if (id == R.id.btn_chat_with_user) {
            String avatarUrl = "";

            FriendInfo friendInfo = new FriendInfo();
            friendInfo.memberName = memberName;
            friendInfo.nickname = nickname;
            friendInfo.avatarUrl = avatarUrl;
            friendInfo.role = ChatUtil.ROLE_MEMBER;
            Util.startFragment(ChatFragment.newInstance(ChatUtil.getConversation(memberName, nickname, avatarUrl, ChatUtil.ROLE_MEMBER), friendInfo));
        }
    }
}
