package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreNavigationAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.entity.StoreNavigationItem;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.ShopCustomerServiceFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.AttachPopupView;

import java.util.List;

/**
 * 白色的彈出菜單
 * @author zwm
 */
public class WhiteDropdownMenu extends AttachPopupView {
    Context context;
    int storeId;
    String storeFigureUrl;

    SimpleCallback onDismissCallback;
    int btnId; // 點擊到的button的Id

    StoreNavigationAdapter storeNavigationAdapter;
    List<StoreNavigationItem> storeNavigationItemList;

    public WhiteDropdownMenu(@NonNull Context context, int storeId, String storeFigureUrl,
                             List<StoreNavigationItem> storeNavigationItemList, SimpleCallback onDismissCallback) {
        super(context);

        this.context = context;
        this.storeId = storeId;
        this.storeFigureUrl = storeFigureUrl;
        this.storeNavigationItemList = storeNavigationItemList;
        this.onDismissCallback = onDismissCallback;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_white_menu;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        LinearLayout llMenuList = findViewById(R.id.ll_menu_list);
        storeNavigationAdapter = new StoreNavigationAdapter(context, llMenuList, R.layout.store_navigation_item);
        storeNavigationAdapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
            @Override
            public void onClick(ViewGroupAdapter adapter, View view, int position) {
                StoreNavigationItem item = storeNavigationItemList.get(position);

                int id = item.id;
                btnId = id;
                if (id == -1) {
                    SLog.info("btn_item_1");
                } else if (id == -2) {
                    SLog.info("btn_item_2");
                    Util.startFragment(ShopCustomerServiceFragment.newInstance(storeId, storeFigureUrl));
                } else {
                    String url = item.url;
                    String title = item.title;
                    Util.startFragment(H5GameFragment.newInstance(url, title));
                }
                dismiss();
            }
        });
        storeNavigationAdapter.setData(storeNavigationItemList);
    }

    // 如果要自定义弹窗的背景，不要给布局设置背景图片，重写这个方法返回一个Drawable即可
    @Override
    protected Drawable getPopupBackground() {
        return getResources().getDrawable(R.drawable.white_menu_bg_small, null);
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();

        if (onDismissCallback != null) {
            onDismissCallback.onSimpleCall(btnId);
        }
    }
}

