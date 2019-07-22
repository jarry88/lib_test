package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.ftofs.twant.R;
import com.lxj.xpopup.core.AttachPopupView;

/**
 * 黑色下拉菜单
 * @author zwm
 */
public class BlackDropdownMenuStore extends AttachPopupView {
    public BlackDropdownMenuStore(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_black_menu_store;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        // 在這里可以做一些findViewById等查找控件，進行自定義操作
    }

    // 如果要自定义弹窗的背景，不要给布局设置背景图片，重写这个方法返回一个Drawable即可
    @Override
    protected Drawable getPopupBackground() {
        return getResources().getDrawable(R.drawable.black_menu_bg, null);
    }
}
