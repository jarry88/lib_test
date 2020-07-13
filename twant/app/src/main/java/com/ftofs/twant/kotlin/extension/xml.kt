package com.ftofs.twant.kotlin.extension

import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ftofs.twant.util.StringUtil
import com.wzq.mvvmsmart.utils.KLog

@BindingAdapter("imgUrl")
fun setImgUrl(view: ImageView, url: String) {
    KLog.e(url)
    Glide.with(view).load(StringUtil.normalizeImageUrl(url)).centerCrop().into(view)
}