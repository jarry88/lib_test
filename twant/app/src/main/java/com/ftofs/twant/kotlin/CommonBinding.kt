package com.ftofs.twant.kotlin

import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ftofs.lib_net.model.Goods
import com.ftofs.twant.R
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.util.StringUtil
import com.ftofs.twant.util.Time
import com.ftofs.twant.util.Util
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog


@BindingAdapter(value = ["imageUrl", "defaultDrawable"], requireAll = false)
fun loadImageUrl(v: ImageView, url: String?, defaultDrawable: Drawable?){
    url?.run {
//        SLog.info("url $url ${url.length},${"".length}")
        if (isEmpty()) {
            SLog.info("url $url ${length},${"".length}")
            Glide.with(v).load(defaultDrawable).centerCrop().into(v)

        } else {
//            SLog.info("url $url ${url.length},${"".length}")
            if (url.contains("none.gif")) {//go853房产占位图
                Glide.with(v).load(R.drawable.go_no_data).centerCrop().into(v)
            } else {
                Glide.with(v).load(StringUtil.normalizeImageUrl(url)).centerCrop().into(v)
            }

        }
    }
}
@BindingAdapter(value = ["delayBackgroud"])
fun setDelayBackground(v: View, bg: Drawable?) {
    bg?.run {
        v.apply {
            layoutParams.apply {
                width=width
                height=height
            }
            background=bg
        }
    }
}

@BindingAdapter(value = ["timeStamp"])
fun setTimeStamp(v: TextView, timeStamp: Long?) {
    timeStamp?.let {
        v.text= Time.fromMillisUnixtime(timeStamp, "Y-m-d H:i:s")
    }
}
@BindingAdapter(value = ["price"])
fun setTextPrice(v: TextView, price: Double) {
    price.run {
        v.text= StringUtil.formatPrice(null, price, 0, 2)
    }
}
@BindingAdapter(value = ["priceModel"])
fun setTextPriceWithModel(v: TextView, vo: Goods) {
    SLog.info(vo.goodsModal.toString())
    if (vo.goodsModal == Constant.GOODS_TYPE_CONSULT) {
        v.text="問價"
    } else {
        setTextPrice(v, vo.appPriceMin)
    }
}
@BindingAdapter(value = ["mobileList"])
fun setTextMobileListSpan(v: TextView, mobileList: List<String>?) {
    //设置部分文字点击事件
    //设置部分文字点击事件
    mobileList?.takeIf { it.isNotEmpty() }?.let {
        val style=SpannableStringBuilder()
        it.forEach {
            s->
            style.apply {
                append(s)
                setSpan({Util.callPhone(Util.findActivity(v.context),s)},this.length-s.length,this.length,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                append("， ")
            }
        }
        style.delete(style.length-2,style.length)
        v.text=style
    }
}
@BindingAdapter(value = ["showOringial"])
fun setTextOriginal(v: TextView, original: Double) {
    original.run {
        v.text = StringUtil.formatPrice(null, original, 0, true)
        v.typeface = Typeface.DEFAULT
        // 原價顯示刪除線
        // 原價顯示刪除線
        v.paintFlags = v.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}
@BindingAdapter(value = ["fir", "len", "pointSize", "defaultSize", "textInput"], requireAll = false)
fun setSpText(v: TextView, fir: Int, len: Int, pointSize: Int, defaultSize: Int, textInput: String) {
    val text=v.text
    SLog.info(textInput)
    textInput.run {
        val sp = SpannableString(text)
        sp.setSpan(AbsoluteSizeSpan(defaultSize, true), 0, fir, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        sp.setSpan(AbsoluteSizeSpan(pointSize, true), fir, fir + len, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        sp.setSpan(AbsoluteSizeSpan(defaultSize, true), fir + len, text.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        v.text = sp
    }
}

/**
 * 注意不能剪裁带背景色的view
 */
@BindingAdapter("radius")
fun setBackRadius(v: View, radius: Float) {
    v.run {
        BaseContext.instance.getContext().run out@{
            background=ColorDrawable(getColor(R.color.white))
            outlineProvider= object:ViewOutlineProvider(){
                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.run {
                        setRoundRect(0, 0, v.width, v.height, Util.dip2px(this@out, radius).toFloat())
                    }
                }
        }
        background=ColorDrawable(BaseContext.instance.getContext().getColor(R.color.white))
        clipToOutline=true

        }
    }

}