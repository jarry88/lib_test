package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.util.QRCode;
import com.ftofs.twant.util.StringUtil;

import java.io.File;

/**
 * 海報控件
 * @author zwm
 */
public class Poster extends FrameLayout {
    ImageView imgAvatar;
    TextView tvNickname;
    ImageView goodsImage;
    TextView tvGoodsName;
    ImageView imgQrCode;

    public Poster(@NonNull Context context) {
        this(context, null);
    }

    public Poster(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Poster(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.poster_layout, this, true);

        imgAvatar = findViewById(R.id.img_avatar);
        tvNickname = findViewById(R.id.tv_nickname);
        goodsImage = findViewById(R.id.img_goods);
        tvGoodsName = findViewById(R.id.tv_goods_name);
        imgQrCode = findViewById(R.id.img_qr_code);
    }

    public Poster setAvatar(File avatarFile) {
        Glide.with(this).load(avatarFile).centerCrop().into(imgAvatar);
        return this;
    }

    public Poster setGoodsImage(File goodsImageFile) {
        Glide.with(this).load(goodsImageFile).centerCrop().into(goodsImage);
        return this;
    }

    public Poster setGoodsName(String goodsName) {
        tvGoodsName.setText(goodsName);
        return this;
    }

    public Poster setQrCode(String text) {
        Bitmap qrCode = QRCode.createQRCode(text);
        Glide.with(imgQrCode).load(qrCode).into(imgQrCode);
        return this;
    }
}
