package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.util.StringUtil;
import com.lxj.xpopup.core.CenterPopupView;


/**
 * 已讀消息提示框
 * @author zwm
 */
public class ReadMessagePopup extends CenterPopupView implements View.OnClickListener {
    String title;
    String createTime;
    Object iconRes;
    String content;

    /**
     * 已讀消息提示框
     * @param context
     * @param title
     * @param createTime
     * @param iconRes
     * @param content
     */
    public ReadMessagePopup(@NonNull Context context, String title, String createTime, Object iconRes, String content) {
        super(context);

        this.title = title;
        this.createTime = createTime;
        this.iconRes = iconRes;
        this.content = content;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.btn_ok).setOnClickListener(this);

        ((TextView) findViewById(R.id.tv_title)).setText(title);
        ((TextView) findViewById(R.id.tv_content)).setText(content);
        ((TextView) findViewById(R.id.tv_create_time)).setText(createTime);
        ImageView goodsImage = findViewById(R.id.goods_image);
        if (iconRes instanceof String) {
            String imgUrl = StringUtil.normalizeImageUrl((String) iconRes);
            Glide.with(getContext()).load(imgUrl).centerCrop().into(goodsImage);
        } else {
            int resId = (int) iconRes;
            Glide.with(getContext()).load(resId).centerCrop().into(goodsImage);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            dismiss();
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.read_message_popup;
    }
}
