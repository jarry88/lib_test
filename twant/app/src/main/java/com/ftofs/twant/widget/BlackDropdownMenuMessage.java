package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.fragment.AddFriendFragment;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.ContactFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.QrCodeCardFragment;
import com.lxj.xpopup.core.AttachPopupView;

/**
 * 黑色下拉菜单
 * @author zwm
 */
public class BlackDropdownMenuMessage extends AttachPopupView implements View.OnClickListener {
    BaseFragment baseFragment;
    public BlackDropdownMenuMessage(@NonNull Context context, BaseFragment baseFragment) {
        super(context);
        this.baseFragment = baseFragment;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_black_menu_message;
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
            MainFragment mainFragment = MainFragment.getInstance();
            mainFragment.start(ContactFragment.newInstance());
        } else if (id == R.id.btn_item_2) {
            baseFragment.startCaptureActivity();
        } else if (id == R.id.btn_item_3) {
            MainFragment mainFragment = MainFragment.getInstance();
            mainFragment.start(AddFriendFragment.newInstance());
        }

        dismiss();
    }
}
