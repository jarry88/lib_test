package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.fragment.AddFriendFragment;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.CartFragment;
import com.ftofs.twant.fragment.ContactFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.MessageFragment;
import com.ftofs.twant.fragment.PersonalInfoFragment;
import com.ftofs.twant.fragment.SearchFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
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
     * 商品詳情菜單
     */
    public static final int TYPE_GOODS = 3;

    /**
     * 訂單菜單
     */
    public static final int TYPE_ORDER = 4;

    /**
     * 商城首頁+個人專頁
     */
    public static final int TYPE_HOME_AND_MY = 5;

    /**
     * 通訊錄菜單
     */
    public static final int TYPE_CONTACT = 6;


    Context context;
    BaseFragment baseFragment;
    int type;
    public BlackDropdownMenu(@NonNull Context context, BaseFragment baseFragment, int type) {
        super(context);

        this.context = context;
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
        } else if (type == TYPE_STORE || type == TYPE_GOODS) {
            ((ImageView) findViewById(R.id.icon_item_1)).setImageResource(R.drawable.icon_black_menu_home);
            ((TextView) findViewById(R.id.tv_item_1)).setText(R.string.menu_item_shop_home_home);
            ((ImageView) findViewById(R.id.icon_item_2)).setImageResource(R.drawable.icon_black_menu_search);
            ((TextView) findViewById(R.id.tv_item_2)).setText(R.string.menu_item_shop_home_search);
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_black_menu_customer_service);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.menu_item_shop_home_customer_service);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_black_menu_message);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.menu_item_shop_home_message);
        } else if (type == TYPE_ORDER) {
            ((ImageView) findViewById(R.id.icon_item_1)).setImageResource(R.drawable.icon_black_menu_home);
            ((TextView) findViewById(R.id.tv_item_1)).setText(R.string.menu_item_shop_home_home);
            ((ImageView) findViewById(R.id.icon_item_2)).setImageResource(R.drawable.icon_black_menu_cart);
            ((TextView) findViewById(R.id.tv_item_2)).setText(R.string.text_cart);
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_black_menu_my);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.text_my_page);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_black_menu_message);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.menu_item_shop_home_message);
        } else if (type == TYPE_HOME_AND_MY) { // 只有2項，商城首頁 + 個人專頁
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_black_menu_home);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.menu_item_shop_home_home);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_black_menu_my);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.text_my_page);
        } else if (type == TYPE_CONTACT) {
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_scan_qr_code);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.text_scan_qr_code);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_add_friend);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.text_add_friend);
        }

        // 在這里可以做一些findViewById等查找控件，進行自定義操作
        if (type == TYPE_HOME_AND_MY || type == TYPE_CONTACT) {
            // 只有2項，不需要對前2項設置事件處理
            findViewById(R.id.btn_item_1).setVisibility(GONE);
            findViewById(R.id.btn_item_2).setVisibility(GONE);

            View btnItem3 = findViewById(R.id.btn_item_3);
            ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) btnItem3.getLayoutParams();
            layoutParams.topMargin = Util.dip2px(context, 15);
        } else {
            findViewById(R.id.btn_item_1).setOnClickListener(this);
            findViewById(R.id.btn_item_2).setOnClickListener(this);
        }

        findViewById(R.id.btn_item_3).setOnClickListener(this);
        findViewById(R.id.btn_item_4).setOnClickListener(this);
    }

    // 如果要自定义弹窗的背景，不要给布局设置背景图片，重写这个方法返回一个Drawable即可
    @Override
    protected Drawable getPopupBackground() {
        if (type == TYPE_HOME_AND_MY || type == TYPE_CONTACT) {
            return getResources().getDrawable(R.drawable.black_menu_bg_small, null);
        } else {
            return getResources().getDrawable(R.drawable.black_menu_bg, null);
        }
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
                // 通訊錄
                Util.startFragment(ContactFragment.newInstance());
                break;
            case TYPE_STORE:
            case TYPE_GOODS:
                // 商城首頁
                baseFragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.HOME_FRAGMENT);
                break;
            case TYPE_ORDER:
                // 商城首頁
                baseFragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.HOME_FRAGMENT);
                break;
            default:
                break;
        }
    }

    private void handleItem2Clicked() {
        switch (type) {
            case TYPE_MESSAGE:
                // 掃一掃
                baseFragment.startCaptureActivity();
                break;
            case TYPE_STORE:
            case TYPE_GOODS:
                // 全站搜索
                baseFragment.start(SearchFragment.newInstance(SearchType.ALL));
                break;
            case TYPE_ORDER:
                // 購物車
                baseFragment.start(CartFragment.newInstance(true));
                break;
            default:
                break;
        }
    }


    private void handleItem3Clicked() {
        switch (type) {
            case TYPE_MESSAGE:
                // 添加朋友
                Util.startFragment(AddFriendFragment.newInstance());
                break;
            case TYPE_STORE:
            case TYPE_GOODS:
                // 咨詢客服
                ((ShopMainFragment) baseFragment).onBottomBarClick(ShopMainFragment.CUSTOMER_SERVICE_FRAGMENT);
                break;
            case TYPE_ORDER:
                // 個人專頁
                baseFragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.MY_FRAGMENT);
                break;
            case TYPE_HOME_AND_MY:
                // 商城首頁
                baseFragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.HOME_FRAGMENT);
                break;
            case TYPE_CONTACT:
                // 掃一掃
                baseFragment.startCaptureActivity();
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
            case TYPE_GOODS:
                // 消息
                Util.startFragment(MessageFragment.newInstance(true));
                break;
            case TYPE_ORDER:
                // 消息
                Util.startFragment(MessageFragment.newInstance(true));
                break;
            case TYPE_HOME_AND_MY:
                // 個人專頁
                baseFragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.MY_FRAGMENT);
                break;
            case TYPE_CONTACT:
                // 添加朋友
                Util.startFragment(AddFriendFragment.newInstance());
                break;
            default:
                break;
        }
    }
}
