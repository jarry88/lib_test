package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.lxj.xpopup.core.DrawerPopupView;


/**
 * 貼文列表的篩選彈窗
 * @author zwm
 */
public class PostFilterDrawerPopupView extends DrawerPopupView implements View.OnClickListener {
    Context context;
    OnSelectedListener onSelectedListener;


    int twRed;
    int twBlack;

    public PostFilterDrawerPopupView(@NonNull Context context, OnSelectedListener onSelectedListener) {
        super(context);
        this.context = context;
        this.onSelectedListener = onSelectedListener;

        twRed = getResources().getColor(R.color.tw_red, null);
        twBlack = getResources().getColor(R.color.tw_black, null);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.post_filter_drawer_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.ll_popup_content_view).setOnClickListener(this);


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



    @Override
    public void onClick(View v) {
        int id = v.getId();
    }
}

