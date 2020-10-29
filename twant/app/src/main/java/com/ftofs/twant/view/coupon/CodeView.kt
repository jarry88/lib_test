package com.ftofs.twant.view.coupon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ftofs.twant.R
import com.ftofs.twant.dsl.LinearLayout
import com.ftofs.twant.dsl.bg_color_id
import com.ftofs.twant.dsl.layout_height
import com.ftofs.twant.dsl.layout_width

class CodeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    :FrameLayout(context, attrs, defStyleAttr) {
    private val mPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color=Color.BLUE
    } }

    private val mBounds by lazy { Rect() }

    private var mCount = 0

    val contentView by lazy {
        LinearLayout {
            layout_width =40
            layout_height=40
            bg_color_id = R.color.blue
        }
    }

    init {
        La
    }
//    override fun onDraw(canvas: Canvas?) {
//        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mPaint)
//        mPaint.color =Color.YELLOW
//        mPaint.textSize = 30f
//        val text = mCount.toString()
//        mPaint.getTextBounds(text, 0, text.length, mBounds)
//        val textWidth = mBounds.width().toFloat()
//        val textHeight = mBounds.height().toFloat()
//        canvas!!.drawText(text, width / 2 - textWidth / 2, height / 2
//                + textHeight / 2, mPaint)
//
//    }
}