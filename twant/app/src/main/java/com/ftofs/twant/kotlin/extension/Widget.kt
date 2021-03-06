package com.ftofs.twant.kotlin.extension

import android.content.Context

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ftofs.twant.kotlin.util.EmptyCornerDrawable
fun View.removeParent(): View {
    return (this.parent as ViewGroup).let { it.removeView(this)
    it}
}
fun ImageView.displayWithUrl(url: String?) {
    Glide.with(this).load(url)
            .apply(
                    RequestOptions().transform(CenterCrop())
            )
            .into(this)
}

fun ImageView.displayWithUrl(url: String?, radius: Float) {
    val radiusPx = radius.dp2IntPx(context)
    val empty = EmptyCornerDrawable(0xff969696.toInt(), radiusPx.toFloat())
    Glide.with(this).load(url)
            .apply(
                    RequestOptions().transform(
                            CenterCrop(),
                            RoundedCorners(radiusPx)
                    )
                            .placeholder(empty).error(
                                    empty
                            )
            )
            .into(this)
}


/**
 * 隐藏软键盘
 */
fun View.hideKeyboard() {
    val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)

}
/**
 * 設置隱藏
 */