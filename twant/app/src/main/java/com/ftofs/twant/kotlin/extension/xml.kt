package com.ftofs.twant.kotlin.extension

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ftofs.twant.util.StringUtil

@BindingAdapter("imgUrl")
fun setImgUrl(view: ImageView, url: String) {
    Glide.with(view).load(StringUtil.normalizeImageUrl(url)).centerCrop().into(view)
}