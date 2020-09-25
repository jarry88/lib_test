package com.ftofs.twant.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.ftofs.twant.R

class Title @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        initView(attrs)
    }
    private val titlebarLeft by lazy {findViewById<RelativeLayout>(R.id.titlebar_leftlayout)  }
    private val titlebarLeftImage by lazy {findViewById<ImageView>(R.id.titlebarLeftImage)  }
    private val titlebarRight by lazy {findViewById<RelativeLayout>(R.id.titlebar_rightlayout)  }
    private val titleText by lazy {findViewById<TextView>(R.id.titlebar_title)  }
    @SuppressLint("ResourceType")
    private fun initView(attrs: AttributeSet?) {
      View.inflate(context,R.layout.layout_login_tilte,this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Title)
        typedArray.getString(R.styleable.Title_text_title)?.let {
            text=it
        }
        //不設置的時候是顯示的
        typedArray.getBoolean(R.styleable.Title_login_info,false).takeIf { it }?.let {
            rootView.findViewById<View>(R.id.tv_info)?.visibility=View.VISIBLE
        }
//        typedArray.getBoolean(R.attr.login_info,false).let {
//            if(it) rootView.findViewById<TextView>(R.id.tv_info)?.visibility= VISIBLE
//        }
        typedArray.recycle()

    }
    var text:String?
        get() = ""
        set(value) {titleText?.text=value}
    //    左侧布局监听
    fun setLeftLayoutClickListener(listener: OnClickListener) {
        titlebarLeft!!.setOnClickListener(listener)
    }
    //   通过资源id设置左侧图片样式
    fun setLeftImageResource(resId: Int) {
        titlebarLeftImage!!.setImageResource(resId)
    }
    //   右侧布局监听
    fun setRightLayoutClickListener(listener: OnClickListener) {
        titlebarRight!!.setOnClickListener(listener)
    }
}