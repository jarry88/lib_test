package com.ftofs.twant.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

public class TwLoadingPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    String content;


    public TwLoadingPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.tw_loading_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        ImageView imgLoading = findViewById(R.id.img_loading);
        Glide.with(imgLoading).load("file:///android_asset/loading.gif")
                .into(imgLoading);
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
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 1f);
    }

    @Override
    public void onClick(View v) {
    }
}

