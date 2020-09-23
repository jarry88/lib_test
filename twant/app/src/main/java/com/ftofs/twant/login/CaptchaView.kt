package com.ftofs.twant.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.roundToInt
import com.ftofs.lib_common_ui.R
@SuppressLint("UseCompatLoadingForDrawables")
class CaptchaView @JvmOverloads constructor(context: Context, attr:AttributeSet?=null, defStyleAttr:Int=0):AppCompatTextView(context,attr,defStyleAttr){
    var countTime:Long=60//倒計時幾秒鐘
    var mText ="s後重試"
    var canClickable=true
    var mInitText="獲取驗證碼"
    var mListener:()->Boolean= {true}

    private val countDownTimer by lazy {object : CountDownTimer(countTime*1000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            text= (millisUntilFinished / 1000).toDouble().roundToInt().toString().plus(mText)
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onFinish() {
                clickableSet()
                text="重新獲取"
                canClickable=true
        }
    } }
    init {
        gravity=Gravity.CENTER
        clickableSet()

        setOnClickListener {
            if(canClickable&&mListener()){
                canClickable=false
                countDownTimer.start()
                setTextColor(resources.getColor(R.color.tw_grey))
                background=resources.getDrawable(R.drawable.bg_grey_captcha,null)
            }
        }
    }
    private fun clickableSet() {
        background=resources.getDrawable(R.drawable.bg_refresh_captcha,null)
        setTextColor(resources.getColor(R.color.tw_blue))
    }
}