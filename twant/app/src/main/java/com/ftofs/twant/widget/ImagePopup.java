package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.util.QRCode;
import com.ftofs.twant.util.StringUtil;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

public class ImagePopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    String url;


    public ImagePopup(@NonNull Context context, String url) {
        super(context);

        this.url = url;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.image_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        ImageView imageView = findViewById(R.id.image_view);
        Glide.with(imageView).load(StringUtil.normalizeImageUrl(url)).into(imageView);
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


