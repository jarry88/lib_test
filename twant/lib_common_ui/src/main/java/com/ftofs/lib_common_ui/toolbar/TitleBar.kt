package com.ftofs.lib_common_ui.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.ftofs.lib_common_ui.R
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.ToastUtil

class TitleBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        RelativeLayout(context, attrs, defStyleAttr){
    private val  titlebarTitle by lazy { mView.findViewById<TextView>(R.id.titlebar_title) }
    private val titlebarLeft by lazy { mView.findViewById<RelativeLayout>(R.id.titlebar_leftlayout) }
    private val titlebarLeftImage by lazy { mView.findViewById<ImageView>(R.id.titlebarLeftImage) }
    private val titlebarRight by lazy { mView.findViewById<RelativeLayout>(R.id.titlebar_rightlayout) }
    private val titlebarRightText by lazy { mView.findViewById<TextView>(R.id.titlebar_righttext) }
    private val titlebarRightImage by lazy { mView.findViewById<ImageView>(R.id.titlebar_rightimage) }
    private val root by lazy { mView.findViewById<RelativeLayout>(R.id.titlebar_root) }
    private var mView:View = LayoutInflater.from(context).inflate(R.layout.rl_title_wight, null,false)

    init {
        //通过布局解释器获取布局
        //通过attrs设置相关属性
        if (mView.findViewById<TextView>(R.id.titlebar_title) == null) {
            SLog.info("沒找到")
        }
//        mView.findViewById<TextView>(R.id.titlebar_title)?.let { setStyle(attrs!!) }?:ToastUtil.error(context,"沒有拿到")
        //设置默认背景颜色
        setDefaultColor()
        addView(mView)
    }
    private fun setDefaultColor() {
        this.setBackgroundResource(R.color.colorPrimary)
        titlebarTitle!!.setTextColor(Color.WHITE)
        titlebarRightText!!.setTextColor(Color.WHITE)
    }
    private fun setStyle( attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        //            获取title
        array.getString(R.styleable.TitleBar_titleBar_title)?.let { titlebarTitle.text=it }
        //            获取左侧图片
        val lDrawable = array.getDrawable(R.styleable.TitleBar_titleBar_leftImage)
        if (lDrawable != null) {
            titlebarLeftImage!!.setImageDrawable(lDrawable)
        }
        //            获取右侧图片
        val rDrawable = array.getDrawable(R.styleable.TitleBar_titleBar_rightImage)
        if (rDrawable != null) {
            titlebarRightImage!!.setImageDrawable(rDrawable)
            titlebarRightText!!.visibility = GONE//有图片则不显示文字
        }
        //            获取右侧文字
        //            当图片为空才显示文字
        val right_text = array.getString(R.styleable.TitleBar_titleBar_rightText)
        if (rDrawable == null && right_text != null) {
            titlebarRightText!!.text = right_text
        }
        //            获取背景图片
        val bgDrawable = array.getDrawable(R.styleable.TitleBar_titleBar_background)
        if (bgDrawable != null) {
            root.background=bgDrawable
        }
        array.recycle()
        titlebarLeft!!.setOnClickListener {
            val activity = context as Activity
            activity.onBackPressed()
        }
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
    //   通过资源id设置右侧图片样式
    fun setRightImageResource(resId: Int) {
        titlebarRightImage!!.setImageResource(resId)
    }
    //    设置title
    fun setTitle(title: String) {
        titlebarTitle!!.text = title
    }
    //    设置跟布局背景颜色
    override fun setBackgroundColor(color: Int) {
        root.setBackgroundColor(color)
    }

}