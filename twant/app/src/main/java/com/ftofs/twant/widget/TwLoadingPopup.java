package com.ftofs.twant.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

public class TwLoadingPopup extends CenterPopupView implements View.OnClickListener {
    Context context;

    public TwLoadingPopup(@NonNull Context context) {
        super(context);

        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.tw_loading_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        // 判斷Activity状态，防止误报bugly #41305 java.lang.IllegalArgumentException
        Activity activity = Util.findActivity(context);
        if (activity == null) {
            SLog.info("Error!findActivity failed");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            return;
        }

        ImageView imgLoading = findViewById(R.id.img_loading);
        Glide.with(context).load("file:///android_asset/loading.gif")
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

