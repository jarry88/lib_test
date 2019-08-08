package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.SearchFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.lxj.xpopup.core.AttachPopupView;

import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 店鋪的黑色下拉菜单
 * @author zwm
 */
public class BlackDropdownMenuStore extends AttachPopupView implements View.OnClickListener {
    SupportFragment fragment;

    public BlackDropdownMenuStore(@NonNull Context context, SupportFragment fragment) {
        super(context);

        this.fragment = fragment;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_black_menu_store;
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

        switch (id) {
            case R.id.btn_item_1:
                // 商城首頁
                fragment.popTo(MainFragment.class, false);
                dismiss();
                break;
            case R.id.btn_item_2:
                // 全站搜索
                fragment.start(SearchFragment.newInstance());
                dismiss();
                break;
            case R.id.btn_item_3:
                // 咨詢客服
                ((ShopMainFragment) fragment).onBottomBarClick(ShopMainFragment.CUSTOMER_SERVICE_FRAGMENT);
                dismiss();
                break;
            case R.id.btn_item_4:
                // 消息
                fragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.MESSAGE_FRAGMENT);
                dismiss();
                break;
            default:
                break;
        }
    }
}
