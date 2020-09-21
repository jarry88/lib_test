package com.ftofs.lib_common_ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

@SuppressLint("CustomViewStyleable")
class AAA @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        //1、获取配置的属性值
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AAA, defStyleAttr, 0)
        View.inflate(context,R.layout.layout_aaa,this)
        typedArray.getString(R.styleable.AAA_bbb)?.let { findViewById<TextView>(R.id.test)?.apply { this.text=it } }
        typedArray.recycle()

    }
}
