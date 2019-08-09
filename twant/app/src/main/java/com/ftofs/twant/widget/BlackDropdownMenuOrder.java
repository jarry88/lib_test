package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.fragment.CartFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.lxj.xpopup.core.AttachPopupView;

import me.yokeyword.fragmentation.SupportFragment;

public class BlackDropdownMenuOrder extends AttachPopupView implements View.OnClickListener {

    SupportFragment fragment;
    public BlackDropdownMenuOrder(@NonNull Context context, SupportFragment fragment) {
        super(context);
        this.fragment = fragment;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_black_menu_order;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        // 在這里可以做一些findViewById等查找控件，進行自定義操作
        findViewById(R.id.btn_item_1).setOnClickListener(this);
        findViewById(R.id.btn_item_2).setOnClickListener(this);
        findViewById(R.id.btn_item_3).setOnClickListener(this);
        findViewById(R.id.btn_item_4).setOnClickListener(this);
    }

    // 如果要自定义弹窗的背景，不要给布局设置背景图片，重写这个方法返回一个Drawable即可
    @Override
    protected Drawable getPopupBackground() {
        return getResources().getDrawable(R.drawable.black_menu_bg, null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_item_1) {
            // 商城首頁
            fragment.popTo(MainFragment.class, false);
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.HOME_FRAGMENT);
            dismiss();
        } else if (id == R.id.btn_item_2) {
            // 購物車
            fragment.start(CartFragment.newInstance(true));
            dismiss();
        } else if (id == R.id.btn_item_3) {
            // 個人專頁
            fragment.popTo(MainFragment.class, false);
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.MY_FRAGMENT);
            dismiss();
        } else if (id == R.id.btn_item_4) {
            // 消息
            fragment.popTo(MainFragment.class, false);
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.MESSAGE_FRAGMENT);
            dismiss();
        }

        dismiss();
    }
}
