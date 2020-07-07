package com.ftofs.twant.kotlin

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ftofs.twant.util.StringUtil


@BindingAdapter(value = ["imageUrl"])
fun loadImageUrl(v: ImageView, url: String?) {
    url?.run {
        Glide.with(v).load(StringUtil.normalizeImageUrl(url)).centerCrop().into(v)

    }
}