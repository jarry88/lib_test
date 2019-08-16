package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.fragment.AddFriendFragment;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.ContactFragment;
import com.ftofs.twant.fragment.PersonalInfoFragment;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.AttachPopupView;

/**
 * 通用的黑色彈出菜單
 * @author zwm
 */
public class BlackDropdownMenu extends AttachPopupView implements View.OnClickListener {
    /**
     * 消息菜單
     */
    public static final int TYPE_MESSAGE = 1;

    /**
     * 店鋪菜單
     */
    public static final int TYPE_STORE = 2;

    /**
     * 訂單菜單
     */
    public static final int TYPE_ORDER = 3;


    BaseFragment baseFragment;
    int type;
    public BlackDropdownMenu(@NonNull Context context, BaseFragment baseFragment, int type) {
        super(context);
        this.baseFragment = baseFragment;
        this.type = type;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_black_menu;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        if (type == TYPE_MESSAGE) {
            ((ImageView) findViewById(R.id.icon_item_1)).setImageResource(R.drawable.icon_contact_white);
            ((TextView) findViewById(R.id.tv_item_1)).setText(R.string.text_contact);
            ((ImageView) findViewById(R.id.icon_item_2)).setImageResource(R.drawable.icon_scan_qr_code);
            ((TextView) findViewById(R.id.tv_item_2)).setText(R.string.text_scan_qr_code);
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_add_friend);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.text_add_friend);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_setting_white);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.text_setting);
        } else if (type == TYPE_STORE) {

        } else if (type == TYPE_ORDER) {

        }

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
            handleItem1Clicked();
        } else if (id == R.id.btn_item_2) {
            handleItem2Clicked();
        } else if (id == R.id.btn_item_3) {
            handleItem3Clicked();
        } else if (id == R.id.btn_item_4) {
            handleItem4Clicked();
        }

        dismiss();
    }

    private void handleItem1Clicked() {
        switch (type) {
            case TYPE_MESSAGE:
                Util.startFragment(ContactFragment.newInstance());
                break;
            case TYPE_STORE:
                break;
            case TYPE_ORDER:
                break;
            default:
                break;
        }
    }

    private void handleItem2Clicked() {
        switch (type) {
            case TYPE_MESSAGE:
                baseFragment.startCaptureActivity();
                break;
            case TYPE_STORE:
                break;
            case TYPE_ORDER:
                break;
            default:
                break;
        }
    }


    private void handleItem3Clicked() {
        switch (type) {
            case TYPE_MESSAGE:
                Util.startFragment(AddFriendFragment.newInstance());
                break;
            case TYPE_STORE:
                break;
            case TYPE_ORDER:
                break;
            default:
                break;
        }
    }


    private void handleItem4Clicked() {
        switch (type) {
            case TYPE_MESSAGE:
                Util.startFragment(PersonalInfoFragment.newInstance());
                break;
            case TYPE_STORE:
                break;
            case TYPE_ORDER:
                break;
            default:
                break;
        }
    }
}
