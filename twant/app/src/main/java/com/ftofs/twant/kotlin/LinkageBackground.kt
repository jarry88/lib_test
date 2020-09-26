package com.ftofs.twant.kotlin

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.ftofs.twant.R
//新的banner頁儲備
class LinkageBackground @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

//    private val backgroundCur: ImageView? by bindOptionalView(R.id.backgroundCur)
//    private val backgroundPre: ImageView? by bindOptionalView(R.id.backgroundPre)
//    private val backgroundNext: ImageView? by bindOptionalView(R.id.backgroundNext)
//    private lateinit var bgRes: MutableList<Any>
//
//    init {
//        initView()
//    }
//
//    fun initView() {
////        View.inflate(context, R.layout.layout_linkage_bg, this)
//    }
//
//    /**
//     * 初始化背景
//     */
//    fun setBgRes(res: List<Any>) {
//        bgRes = res.toMutableList()
//        resetImg(0)
//    }
//
//    /**
//     * 向左滑动，不需要操作pre
//     */
//    fun scroll2Left(offset: Float) {
////        Log.d("LinkageBanner :", "<<<<<<<<<<$offset")
//        backgroundNext?.visibility = View.VISIBLE
//        backgroundNext?.alpha = offset
//        backgroundCur?.alpha = 1 - offset
//    }
//
//    /**
//     * 向右滑，让next隐藏，对pre和cur操作透明度
//     */
//    fun scroll2Right(offset: Float) {
////        Log.d("LinkageBanner :", ">>>>>>>>>>$offset")
//        backgroundNext?.visibility = View.GONE
//        backgroundNext?.alpha = 0f
//        backgroundPre?.visibility = View.VISIBLE
//        backgroundPre?.alpha = 1 - offset
//        backgroundCur?.alpha = offset
//    }
//
//    /**
//     * 重置所有背景，pre和next隐藏
//     */
//    fun resetImg(cur: Int) {
//        backgroundPre?.visibility = View.GONE
//        backgroundNext?.visibility = View.GONE
//
//        val pre = if (cur - 1 < 0) bgRes.size - 1 else cur - 1
//        backgroundPre?.loadImg(bgRes[pre])
//        backgroundPre?.alpha = 0f
//
//        backgroundCur?.loadImg(bgRes[cur])
//        backgroundCur?.alpha = 1f
//
//        val next = if (cur + 1 > bgRes.size - 1) 0 else cur + 1
//        backgroundNext?.loadImg(bgRes[next])
//        backgroundNext?.alpha = 0f
//    }

}