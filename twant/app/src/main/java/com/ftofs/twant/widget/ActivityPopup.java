package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ftofs.twant.R;
import com.ftofs.twant.fragment.H5GameFragment;
import com.ftofs.twant.fragment.ShoppingSessionFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * 活動彈窗
 * @author zwm
 */
public class ActivityPopup extends CenterPopupView implements View.OnClickListener {
    ImageView imgContent;
    ImageView imgLoadingIndicator;
    ImageView btnClose;

    Context context;
    String appPopupAdImage;
    String appPopupAdLinkType;
    String appPopupAdLinkValue;


    public ActivityPopup(@NonNull Context context, String appPopupAdImage, String appPopupAdLinkType, String appPopupAdLinkValue) {
        super(context);

        this.context = context;
        this.appPopupAdImage = StringUtil.normalizeImageUrl(appPopupAdImage);
        this.appPopupAdLinkType = appPopupAdLinkType;
        this.appPopupAdLinkValue = StringUtil.normalizeImageUrl(appPopupAdLinkValue);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.activity_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        imgContent = findViewById(R.id.img_content);
        imgContent.setOnClickListener(this);
        imgLoadingIndicator = findViewById(R.id.img_loading_indicator);
        Glide.with(context).load("file:///android_asset/loading.gif").into(imgLoadingIndicator);

        btnClose = findViewById(R.id.btn_close);
        btnClose.setOnClickListener(this);

        SLog.info("appPopupAdImage[%s]", appPopupAdImage);
        Glide.with(context).load(appPopupAdImage).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                btnClose.setVisibility(VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                SLog.info("onResourceReady__");
                imgLoadingIndicator.setVisibility(GONE);
                btnClose.setVisibility(VISIBLE);
                return false;
            }
        }).into(imgContent);
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
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 0.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.img_content) {
            SLog.info("appPopupAdLinkType[%s]", appPopupAdLinkType);
            if ("activity".equals(appPopupAdLinkType)) { // 打開活動主頁
                Util.startFragment(H5GameFragment.newInstance(this.appPopupAdLinkValue, ""));
            } else if ("promotion".equals(appPopupAdLinkType)) { // 打開購物專場
                SLog.info("跳轉到購物專場");
                Util.startFragment(ShoppingSessionFragment.newInstance());
            }
            dismiss();
        } else if (id == R.id.btn_close) {
            dismiss();
        }
    }
}

