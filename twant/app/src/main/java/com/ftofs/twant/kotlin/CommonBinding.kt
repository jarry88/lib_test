package com.ftofs.twant.kotlin

import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import android.view.View.*
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ftofs.lib_net.model.CouponItemVo
import com.ftofs.lib_net.model.Goods
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.lib_net.model.Store
import com.ftofs.twant.R
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.databinding.ItemHouseVoBinding
import com.ftofs.twant.dsl.colorId
import com.ftofs.twant.login.Title
import com.ftofs.twant.util.StringUtil
import com.ftofs.twant.util.Time
import com.ftofs.twant.util.Util
import com.ftofs.twant.view.CouponListItemView
import com.ftofs.twant.view.StoreInfoView
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
            SLog.info("url $url ")
            if (url.contains("none.gif")) {//go853房产占位图
                Glide.with(v).load(R.drawable.go_item_no_data).centerCrop().into(v)
            } else {
                Glide.with(v).load(StringUtil.normalizeImageUrl(url)).centerCrop().into(v)
            }

        }
    }
}

@BindingAdapter("tt")
fun setTitleText(view: Title, title: String?) {
    title?.let { view.text=it }
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
@BindingAdapter("visibleOrGone")
fun View.setVisibleOrGone(show: Boolean) {
    visibility = if (show) VISIBLE else GONE
}

@BindingAdapter("visible")
fun View.setVisible(show: Boolean) {
    visibility = if (show) VISIBLE else INVISIBLE
}
@BindingAdapter("pre_text")
fun TextView.setPretext(str: String) {
    text= String.format("%s%s",str,text)
}
@BindingAdapter("store_vo")
fun StoreInfoView.bindVo(vo: Store?) {
    vo?.let { this.updateVo(it) }
}
@BindingAdapter("coupon_item_vo")
fun CouponListItemView.bindVo(vo: CouponItemVo?) {
    vo?.let { this.updateVo(it) }
}
@BindingAdapter(value = ["price","tv_red","first_small"], requireAll = false)
fun setTextPrice(v: TextView, price: Double,tv_red:Boolean=true ,first:Int=11) {
    price.run {
        var size=first
        if(size<10) size=11
        v.text = SpannableStringBuilder(StringUtil.formatPrice(null, price, 0, 2)).also { s->
            s.setSpan(AbsoluteSizeSpan(size,true),0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        if(tv_red) v.colorId=R.color.red
//        v.apply { textSize=firstSize.toFloat() }
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
/**
 * 注意不能剪裁带背景色的view
 */
@BindingAdapter("tv_size")
fun setTextViewSize(v: TextView, size: Float) {
    v.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}
/**
 * 注意不能剪裁带背景色的view
 */
@BindingAdapter("go_sell")
fun setGoSell(v: TextView, item: PropertyVo) {
    v.apply {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0f)
        item.sellingPrice?.let {
            if (it > 0) {
                val start = item.saleType != 2
                text = SpannableStringBuilder("${if (start) "售" else ""}$" + it.toInt().toString() + "萬").also { s ->
                    s.setSpan(AbsoluteSizeSpan(13, true), 0, if (start) 2 else 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                    s.setSpan(AbsoluteSizeSpan(13, true), s.length - 1, s.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            } else GONE.let { if(item.saleType == 2) text ="面議" else {text ="" } }
        }?:GONE.let {
            if(item.saleType == 2){ text ="面議"
                visibility = VISIBLE
            }else text ="" }
    }
}
/**
 * 注意不能剪裁带背景色的view
 */
@BindingAdapter("go_rent")
fun setGoRent(v: TextView, item: PropertyVo) {
    v.apply {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0f)
        item.rentalPrice?.let {

            if (it > 0) {
                val start =item.saleType!=1
                text=SpannableStringBuilder(
                        "${if(start)"租" else ""}$"
                                +it.toInt().toString().let{if(it.length>3) it.substring(0,it.length-3)+","+it.substring(it.length-3,it.length) else it}+"元/月").also { s->
                    s.setSpan(AbsoluteSizeSpan(13,true),0,if(start) 2 else 1 , Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                    s.setSpan(AbsoluteSizeSpan(13,true),s.length-3,s.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                }
            }else GONE.let { if(item.saleType == 1){ text ="面議"
                visibility = VISIBLE
            }else text ="" }
        }?:GONE.let {if(item.saleType == 1) text ="面議" else text ="" }
    }
}