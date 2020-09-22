package com.ftofs.twant.login

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.ftofs.twant.R

class ThirdView @JvmOverloads constructor(context: Context,attr:AttributeSet?=null,defStyle:Int=0):
        FrameLayout(context,attr,defStyle) {
        init {
            View.inflate(context, R.layout.third_login_wighet,this)
        }
}