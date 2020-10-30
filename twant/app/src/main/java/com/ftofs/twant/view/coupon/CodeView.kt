package com.ftofs.twant.view.coupon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.ftofs.twant.R
import com.ftofs.twant.dsl.LinearLayout
import com.ftofs.twant.dsl.bg_color_id
import com.ftofs.twant.dsl.layout_height
import com.ftofs.twant.dsl.layout_width

class CodeView @JvmOverloads constructor( context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    :FrameLayout(context, attrs, defStyleAttr) {
    private val mPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color=Color.BLUE
    } }

    private val mBounds by lazy { Rect() }

    private var mCount = 0

//    val contentView by lazy {
//        LinearLayout {
//            layout_width =40
//            layout_height=40
//            bg_color_id = R.color.blue
//        }
//    }

    init {
        addView(TextView(context).apply { text ="sss" })

//        val layout = LinearLayout(context)
//        layout.orientation = LinearLayout.HORIZONTAL
//        layout.gravity = Gravity.CENTER_VERTICAL
//        addView(layout, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
//
//        iconView = ImageView(context)
//        layout.addView(iconView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
//
//        labelView = TextView(context)
//        labelView.leftPadding = dip(12)
//        labelView.rightPadding = dip(12)
//        labelView.maxLines = 1
//        layout.addView(labelView, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
//
//        arrowView = ImageView(context)
//        layout.addView(arrowView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT))
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