package com.ftofs.ft_login.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.ftofs.lib_common_ui.R

@SuppressLint("ResourceType")
class Title @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        initView(attrs)
    }
    private val titlebarLeft by lazy {findViewById<RelativeLayout>(R.id.titlebar_leftlayout)  }
    private val titlebarLeftImage by lazy {findViewById<ImageView>(R.id.titlebarLeftImage)  }
    private val titlebarRight by lazy {findViewById<RelativeLayout>(R.id.titlebar_rightlayout)  }
    private fun initView(attrs: AttributeSet?) {
//        val rootView=View.inflate(context,R.layout.rl_title_wight,this)
        val rootView=(context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.rl_title_wight, this, true)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Title)

//        typedArray.getString(R.attr.title)?.let { rootView.findViewById<TextView>(R.id.titlebar_title)?.text=it }
        typedArray.getBoolean(R.attr.login_info,false).let {
            if(it) rootView.findViewById<TextView>(R.id.tv_info)?.visibility= VISIBLE
        }
        typedArray.recycle()

    }

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