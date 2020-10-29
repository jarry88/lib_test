package com.ftofs.twant.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class BannerRoundedImageHolder extends RecyclerView.ViewHolder {
    public RoundedImageView imageView;

    public BannerRoundedImageHolder(@NonNull View view) {
        super(view);
        this.imageView = (RoundedImageView) view;
        this.imageView.setCornerRadiusDimen(R.dimen.dp_4);
    }
}
