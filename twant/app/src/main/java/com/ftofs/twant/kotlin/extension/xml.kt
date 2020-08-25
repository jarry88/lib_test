package com.ftofs.twant.kotlin.extension

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ftofs.twant.util.StringUtil
import com.google.android.material.tabs.TabLayout
import com.wzq.mvvmsmart.utils.KLog

@BindingAdapter("imgUrl")
fun setImgUrl(view: ImageView, url: String) {
    KLog.e(url)
    Glide.with(view).load(StringUtil.normalizeImageUrl(url)).centerCrop().into(view)
}
@BindingAdapter("visible")
fun setViewVisible(view: View, visible: Int) {
    if(visible==1) view.visibility=View.VISIBLE
    else view.visibility=View.GONE
}

//@BindingAdapter("android:paddingLeft")
//fun setPaddingLeft(view: View, oldPadding: Int, newPadding: Int) {
//    if (oldPadding != newPadding) {
//        view.setPadding(padding,
//                view.getPaddingTop(),
//                view.getPaddingRight(),
//                view.getPaddingBottom())
//    }
//}