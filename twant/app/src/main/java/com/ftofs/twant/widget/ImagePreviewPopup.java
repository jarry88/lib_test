package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.util.StringUtil;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * IM聊天時，發送圖片的預覽彈窗
 * @author zwm
 */
public class ImagePreviewPopup extends CenterPopupView implements View.OnClickListener {
    ImageView imgContent;

    Context context;
    SimpleCallback simpleCallback;
    String absolutePath;
    private CropImageView mCropView;

    public ImagePreviewPopup(@NonNull Context context, SimpleCallback simpleCallback, String absolutePath) {
        super(context);

        this.context = context;
        this.simpleCallback = simpleCallback;
        this.absolutePath = absolutePath;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.image_preview_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        imgContent = findViewById(R.id.img_content);
        Glide.with(context).load(StringUtil.getImageContentUri(context,absolutePath)).into(imgContent);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

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
        return XPopupUtils.getWindowWidth(getContext());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_cancel) {
            dismiss();
        } else if (id == R.id.btn_ok) {
            simpleCallback.onSimpleCall(EasyJSONObject.generate(
                    "action", SimpleCallback.ACTION_SELECT_IMAGE,
                    "absolute_path", absolutePath
            ));
            dismiss();
        }
    }
}
