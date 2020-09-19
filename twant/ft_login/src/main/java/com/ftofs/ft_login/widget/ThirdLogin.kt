package com.ftofs.ft_login.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.ftofs.ft_login.R

class ThirdLogin @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThirdLogin)
        initView()
        typedArray.recycle()
    }
    private fun initView() {
        View.inflate(context,R.layout.third_login_layout,this)
//        findViewById<>()
    }

}