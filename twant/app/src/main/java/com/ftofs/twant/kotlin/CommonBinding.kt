package com.ftofs.twant.kotlin

import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import anet.channel.util.Utils
import com.bumptech.glide.Glide
import com.ftofs.twant.R
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.StringUtil
import com.ftofs.twant.util.Util


@BindingAdapter(value = ["imageUrl"])
fun loadImageUrl(v: ImageView, url: String?) {
    url?.run {
        Glide.with(v).load(StringUtil.normalizeImageUrl(url)).centerCrop().into(v)
    }
}
@BindingAdapter(value = ["price"])
fun setTextPrice(v: TextView, price: Double) {
    price.run {
        v.text= StringUtil.formatPrice( null,price, 0, 2)
    }
}
@BindingAdapter(value = ["showOringial"])
fun setTextOriginal(v: TextView, original: Double) {
    original.run {
        v.text = StringUtil.formatPrice(null,original, 0, true)
        v.typeface = Typeface.DEFAULT
        // 原價顯示刪除線
        // 原價顯示刪除線
        v.paintFlags = v.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}
@BindingAdapter(value = ["fir","len","pointSize","defaultSize","textInput"],requireAll = false)
fun setSpText(v: TextView, fir:Int,len:Int,pointSize:Int,defaultSize:Int,textInput:String) {
    val text=v.text
    SLog.info(textInput)
    textInput.run {
        val sp = SpannableString(text)
        sp.setSpan(AbsoluteSizeSpan(defaultSize,true),0,fir,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        sp.setSpan(AbsoluteSizeSpan(pointSize,true),fir,fir+len,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        sp.setSpan(AbsoluteSizeSpan(defaultSize,true),fir+len,text.length,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        v.text = sp
    }
}
@BindingAdapter("radius")
fun setBackRadius(v: View, radius: Float) {
    v.run {
        Utils.getAppContext().run out@{
            background=ColorDrawable(getColor(R.color.white))
            outlineProvider= object:ViewOutlineProvider(){
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.run {
                        setRoundRect(0,0,v.width,v.height,Util.dip2px(this@out,radius).toFloat())
                    }
                }
        }
        background=ColorDrawable(Utils.getAppContext().getColor(R.color.white))
        clipToOutline=true

        }
    }

}