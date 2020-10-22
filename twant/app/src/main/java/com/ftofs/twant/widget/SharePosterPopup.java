package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;


import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.util.WeixinUtil;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.File;

/**
 * 分享海報彈窗
 * @author zwm
 */
public class SharePosterPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    File posterFile;

    public SharePosterPopup(@NonNull Context context, File posterFile) {
        super(context);

        this.context = context;
        this.posterFile = posterFile;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.share_poster_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        LinearLayout llBottomContainer = findViewById(R.id.ll_bottom_container);
        float radius = Util.dip2px(context, 16);
        llBottomContainer.setBackground(BackgroundDrawable.create(Color.WHITE, new float[] {radius, radius, 0, 0}));

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        findViewById(R.id.ll_bottom_container).setOnClickListener(this);
        findViewById(R.id.img_poster).setOnClickListener(this);

        findViewById(R.id.btn_share_to_gallery).setOnClickListener(this);
        findViewById(R.id.btn_share_to_timeline).setOnClickListener(this);
        findViewById(R.id.btn_share_to_friend).setOnClickListener(this);

        ImageView imgPoster = findViewById(R.id.img_poster);
        Glide.with(context).load(posterFile).into(imgPoster);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 1f);
    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())* 1f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        } if (id == R.id.btn_share_to_gallery) {
            Util.addImageToGallery(context, posterFile);
            ToastUtil.success(context, "已保存到相冊");
            dismiss();
        } else if (id == R.id.btn_share_to_timeline || id == R.id.btn_share_to_friend) {
            // 檢測微信是否已經安裝
            if (TwantApplication.Companion.get().getWxApi()!=null&&!TwantApplication.Companion.get().getWxApi().isWXAppInstalled()) {
                ToastUtil.error(context, context.getString(R.string.weixin_not_installed_hint));
                return;
            }

            int scene;
            if (id == R.id.btn_share_to_friend) {
                scene = WeixinUtil.WX_SCENE_SESSION;
            } else {
                scene = WeixinUtil.WX_SCENE_TIMELINE;
            }

            WeixinUtil.WeixinShareInfo shareInfo = new WeixinUtil.WeixinShareInfo();
            shareInfo.absolutePath = posterFile.getAbsolutePath();
            WeixinUtil.share(context, scene, WeixinUtil.SHARE_MEDIA_PHOTO, shareInfo);

            dismiss();
        }
    }
}
