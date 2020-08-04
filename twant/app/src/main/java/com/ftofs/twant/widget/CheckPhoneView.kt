package com.ftofs.twant.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.ftofs.twant.R
const val text_invalid_mobile="你輸入的%s手機號碼有誤，請重新輸入"
@SuppressLint("CustomViewStyleable")
class CheckPhoneView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val mContext:Context=context

    private var zoneIndex:Int=0
    private var areaArray= arrayListOf<String>()
    private val regex= listOf(
        "",
        "^[569][0-9]{0,7}$", // 香港
        "^1[0-9]{0,10}$",    // 大陸
        "^6[0-9]{0,7}$"   // 澳門
    )

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingCheckPhoneView)
        initView()
        typedArray.recycle()
    }

    private fun initView() {
        View.inflate(context,R.layout.wdget_check_phone,this)
    }
    fun setZoneIndex(index:Int){
        zoneIndex=index
    }
    fun checkError():Pair<Boolean,String>{
        var isError = false
        var msg = ""
        msg = String.format(text_invalid_mobile, areaArray[zoneIndex]);
        return Pair(isError,msg)
    }

}