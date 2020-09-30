package com.ftofs.twant.kotlin.extension

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ftofs.twant.util.StringUtil
import com.gzp.lib_common.smart.utils.KLog

@BindingAdapter("imgUrl")
fun setImgUrl(view: ImageView, url: String) {
    KLog.e(url)
    Glide.with(view).load(StringUtil.normalizeImageUrl(url)).centerCrop().into(view)
}
@BindingAdapter("visible")
fun setViewVisible(view: View, visible: Int?) {
    visible?.let {
        if(visible==1) view.visibility=View.VISIBLE
        else view.visibility=View.GONE
    }
}
@BindingAdapter("invisible")
fun setInViewVisible(view: View, visible: Int) {
    if(visible==1) view.visibility=View.INVISIBLE
    else view.visibility=View.VISIBLE
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