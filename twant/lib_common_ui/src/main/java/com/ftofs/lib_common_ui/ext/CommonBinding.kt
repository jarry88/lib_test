package com.ftofs.lib_common_ui.ext

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.gzp.lib_common.utils.normalizeImageUrl

@BindingAdapter(value = ["imageUrl","defaultDrawable"],requireAll = false)
fun loadImageUrl(v: ImageView, url: String?,defaultDrawable:Drawable?){
    url?.run {
//        SLog.info("url $url ${url.length},${"".length}")
        if (isEmpty()) {
            Glide.with(v).load(defaultDrawable).centerCrop().into(v)

        } else {
//            SLog.info("url $url ${url.length},${"".length}")
            Glide.with(v).load(normalizeImageUrl(url)).centerCrop().into(v)

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
//
//@BindingAdapter(value = ["timeStamp"])
//fun setTimeStamp(v: TextView, timeStamp: Long?) {
//    timeStamp?.let {
//        v.text= Time.fromMillisUnixtime(timeStamp,"Y-m-d H:i:s")
//    }
//}
//@BindingAdapter(value = ["price"])
//fun setTextPrice(v: TextView, price: Double) {
//    price.run {
//        v.text= StringUtil.formatPrice( null,price, 0, 2)
//    }
//}
//@BindingAdapter(value = ["priceModel"])
//fun setTextPriceWithModel(v: TextView, vo: Goods) {
//    SLog.info(vo.goodsModal.toString())
//    if (vo.goodsModal == Constant.GOODS_TYPE_CONSULT) {
//        v.text="詢價"
//    } else {
//        setTextPrice(v, vo.appPriceMin)
//    }
//}
//@BindingAdapter(value = ["showOringial"])
//fun setTextOriginal(v: TextView, original: Double) {
//    original.run {
//        v.text = StringUtil.formatPrice(null,original, 0, true)
//        v.typeface = Typeface.DEFAULT
//        // 原價顯示刪除線
//        // 原價顯示刪除線
//        v.paintFlags = v.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//    }
//}
//@BindingAdapter(value = ["fir","len","pointSize","defaultSize","textInput"],requireAll = false)
//fun setSpText(v: TextView, fir:Int,len:Int,pointSize:Int,defaultSize:Int,textInput:String) {
//    val text=v.text
//    SLog.info(textInput)
//    textInput.run {
//        val sp = SpannableString(text)
//        sp.setSpan(AbsoluteSizeSpan(defaultSize,true),0,fir,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//        sp.setSpan(AbsoluteSizeSpan(pointSize,true),fir,fir+len,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//        sp.setSpan(AbsoluteSizeSpan(defaultSize,true),fir+len,text.length,Spannable.SPAN_INCLUSIVE_INCLUSIVE)
//        v.text = sp
//    }
//}
//
///**
// * 注意不能剪裁带背景色的view
// */
//@BindingAdapter("radius")
//fun setBackRadius(v: View, radius: Float) {
//    v.run {
//        Utils.getAppContext().run out@{
//            background=ColorDrawable(getColor(R.color.white))
//            outlineProvider= object:ViewOutlineProvider(){
//                override fun getOutline(view: View?, outline: Outline?) {
//                    outline?.run {
//                        setRoundRect(0,0,v.width,v.height,Util.dip2px(this@out,radius).toFloat())
//                    }
//                }
//        }
//        background=ColorDrawable(Utils.getAppContext().getColor(R.color.white))
//        clipToOutline=true
//
//        }
//    }
//
//}