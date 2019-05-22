package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ftofs.twant.R;
import com.lxj.xpopup.core.DrawerPopupView;

/**
 * 商品搜索結果列表的篩選彈窗
 * @author zwm
 */
public class GoodsFilterDrawerPopupView extends DrawerPopupView {
    public GoodsFilterDrawerPopupView(@NonNull Context context) {
        super(context);
    }
    @Override
    protected int getImplLayoutId() {
        return R.layout.goods_filter_drawer_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();


        //通过设置topMargin，可以让Drawer弹窗进行局部阴影展示
//        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getPopupContentView().getLayoutParams();
//        params.topMargin = 450;
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }
}
