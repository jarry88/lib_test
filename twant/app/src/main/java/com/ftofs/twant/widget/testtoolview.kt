package com.ftofs.twant.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.ftofs.twant.R

//3、虛擬運營商號段：
//
//電信：1700、1701、1702、162
//
//移動：1703、1705、1706 、165
//聯通：1704、1707、1708、1709、167、171
//衛星通訊：1349
/**
 * 1、在xml、fragment中声明并绑定控件
 * 2、调用接口获取 mobilelist 数据 赋值给控件
 * 3、onselect时传递序号进来
 * 4、调取checkerror检查状态
 * 5、返回true时 取值使用
 * 6、返回false时 查看msg
 */
@SuppressLint("CustomViewStyleable", "ResourceType")
class TestToolView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val mContext:Context=context
    private lateinit var title: TextView



    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Title)
        initView()
//        typedArray.getString(R.attr.title).let { title.text="11" }
        typedArray.recycle()
    }

    private fun initView() {
        View.inflate(mContext,R.layout.rl_title_wight,this)
        title=rootView.findViewById(R.id.tv_title)
        title.text="aa"
    }


}