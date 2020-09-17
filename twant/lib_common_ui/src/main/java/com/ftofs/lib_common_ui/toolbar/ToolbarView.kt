package com.ftofs.lib_common_ui.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.text.InputFilter
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.Barrier
import androidx.constraintlayout.widget.ConstraintLayout
import com.ftofs.lib_common_ui.R
import com.ftofs.lib_common_ui.ext.color
import com.ftofs.lib_common_ui.ext.dimen
import com.ftofs.lib_common_ui.ext.dp2px
import com.ftofs.lib_common_ui.ext.drawable
import com.gzp.lib_common.utils.ScreenUtils

/**
 * toolbar 添加组件存在重建情况下不使用自带的注入
 *  Created by 86152 on 2019-09-22
 **/
class ToolbarView(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    private val toolbarHeight = 45

    private val idx = 0x119911
    private val idParent = 0x119912
    private val idLeft = 0x119913
    private val idCenter = 0x119914
    private val idRight = 0x119915
    private val idBarrier = 0x119916
    private val idDivider = 0x119917
    private val idExtend = 0x119918

    private val textColorDark = R.color.white //深色主题时的字体颜色

    private val textColorLight = R.color.gray_33 //浅色主题时的字体颜色

    private val initedViews by lazy { mutableListOf<ViewGroup>() }

    private val mLeftContainer by lazy {
        LinearLayout(context).apply {
            id = idLeft
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
                height = dp2px(toolbarHeight)
                startToStart = LayoutParams.PARENT_ID
            }
            initedViews.add(this)
            this@ToolbarView.addView(this)
        }
    }

    private val mRightContainer by lazy {
        LinearLayout(context).apply {
            id = idRight
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
                height = dp2px(toolbarHeight)
                endToEnd = LayoutParams.PARENT_ID
            }
            initedViews.add(this)
            this@ToolbarView.addView(this)
        }
    }

    private val mCenterContainer by lazy {
        FrameLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
                id = idCenter
                height = dp2px(toolbarHeight)
                startToStart = LayoutParams.PARENT_ID
                endToEnd = LayoutParams.PARENT_ID
            }
            initedViews.add(this)
            this@ToolbarView.addView(this)
        }
    }

    private val mDivider by lazy {
        Barrier(context).apply {
            id = idBarrier
            referencedIds = intArrayOf(idLeft, idRight, idCenter)
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            type = 3
            this@ToolbarView.addView(this)
        }

        View(context).apply {
            id = idDivider
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dimen(R.dimen.dimen_05)).apply {
                topToBottom = idBarrier
            }
            this@ToolbarView.addView(this)
        }
    }

    private val mExtendContainer by lazy {
        FrameLayout(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                id = idExtend
                topToBottom = idDivider
            }
            initedViews.add(this)
            this@ToolbarView.addView(this)
        }
    }

    //亮色主题，字体为gray_33
    var isLight = true

    @DrawableRes
    var background: Int = R.drawable.bg_item_white
        set(value) {
            setBackground(drawable(value))
        }

    var isImmerse: Boolean = true
        set(value) {
            field = value
            setPadding(0, 0, 0, 0)
        }

    init {
        id = idParent
        isImmerse = if (context is Activity) ScreenUtils.isImmerseStatus(context) else true
        divider()
        background = R.color.white
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun attachToolbar(root: View?) {
        if (parent == null) {
            root?.let {
                when (it) {
                    is LinearLayout -> it.addView(this, 0)
                    is FrameLayout -> it.addView(this)
                    is ConstraintLayout -> {
                        (layoutParams as LayoutParams).topToTop = LayoutParams.PARENT_ID
                        for (i in 0 until it.childCount) {
                            it.getChildAt(i)?.let { child ->
                                child.layoutParams.run {
                                    if (this is LayoutParams && topToTop == LayoutParams.PARENT_ID) {
                                        topToBottom = this@ToolbarView.id
                                        topToTop = -1
                                    }
                                }
                            }
                        }
                        it.addView(this)
                    }
                }
            }
        }
    }

    /**
     * toolbar居中布局
     */
    fun center(s: String, @ColorRes color: Int = if (isLight) textColorLight else textColorDark, padRect: Rect = Rect(0, 0, 0, 0), isParentMatch: Boolean = false, onClick: ((view: View) -> Unit)? = null): TextView {
        return TextView(context).apply {
            setTextColor(context.color(color))
            textSize = 17f
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.strokeWidth = 1.0f
            ellipsize = TextUtils.TruncateAt.END
            padRect.run { setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom)) }
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(toolbarHeight)).apply {
                gravity = Gravity.CENTER
            }
            gravity = Gravity.CENTER
            text = s
            maxLines = 1
            maxWidth = dp2px(200)
            filters = arrayOf(InputFilter.LengthFilter(15))
            center<TextView>(this, isParentMatch, null, onClick)
        }
    }

    fun <T : View> center(v: View, isParentMatch: Boolean = false, margRect: Rect? = Rect(0, 0, 0, 0), onClick: ((view: View) -> Unit)? = null): T {

        if (isParentMatch) {
            (mCenterContainer.layoutParams as LayoutParams).run {
                mLeftContainer
                mRightContainer
                width = 0
                startToStart = -1
                startToEnd = idLeft
                endToEnd = -1
                endToStart = idRight
            }
        }

        return v.apply {
            onClick?.run {
                setOnClickListener { invoke(it) }
            }
            mCenterContainer.addView(this)

            margRect?.run {
                (layoutParams as FrameLayout.LayoutParams).run {
                    setMargins(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom))
                }
            }
        } as T
    }

    fun <T : View> center(@LayoutRes layout: Int, isParentMatch: Boolean = false, margRect: Rect? = Rect(0, 0, 0, 0), onClick: ((view: View) -> Unit)? = null): T {
        return center(inflate(context, layout, null), isParentMatch, margRect, onClick)
    }

    /**
     * toolbar居左布局
     */
    fun left(@DrawableRes id: Int, rect: Rect? = Rect(15, 15, 10, 15), onClick: ((view: View) -> Unit)? = null): ImageView {
        return ImageView(context).apply {
            rect?.run { setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom)) }
            setImageResource(id)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(toolbarHeight))
            left(this, onClick)
        }
    }

    fun left(s: String, @ColorRes color: Int = if (isLight) textColorLight else textColorDark, rect: Rect = Rect(15, 0, 0, 0), onClick: ((view: View) -> Unit)? = null): TextView {
        return TextView(context).apply {
            setTextColor(context.color(color))
            textSize = 16f
            rect.run { setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom)) }
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(toolbarHeight))
            gravity = Gravity.CENTER
            text = s
            left(this, onClick)
        }
    }

    fun left(v: View, onClick: ((view: View) -> Unit)? = null): View {
        return v.apply {
            onClick?.run {
                setOnClickListener { invoke(it) }
            }
            mLeftContainer.addView(this)
        }
    }

    /**
     * toolbar居右布局
     */
    fun right(@DrawableRes id: Int, rect: Rect? = Rect(10, 12, 15, 12), onClick: ((view: View) -> Unit)? = null): ImageView {
        return ImageView(context).apply {
            rect?.run { setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom)) }
            setImageResource(id)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(toolbarHeight))
            right(this, onClick)
        }
    }

    fun right(s: String, @ColorRes color: Int = if (isLight) textColorLight else textColorDark, asBtn: Boolean = false,
              rect: Rect = Rect(10, 0, 15, 0), onClick: ((view: View) -> Unit)? = null): TextView {

        return TextView(context).apply {
            if (!asBtn) {
                setTextColor(context.color(color))
                textSize = 16f
                rect.run { setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom)) }
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(toolbarHeight))
            } else {
                wrapTextToBtn(this)
            }

            gravity = Gravity.CENTER
            text = s
            right(this, onClick)
        }
    }

    private fun wrapTextToBtn(textView: TextView) {

        textView.run {
            setTextColor(context.color(R.color.white))
            textSize = 14f
            setPadding(dp2px(12), dp2px(4), dp2px(12), dp2px(4))
            background = context.drawable(R.drawable.btn_main_theme)
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                height = dp2px(28)
                marginEnd = dp2px(15)
                gravity = Gravity.CENTER_VERTICAL
            }
        }
    }

    fun right(v: View, onClick: ((view: View) -> Unit)? = null): View {
        return v.apply {
            onClick?.run {
                setOnClickListener { invoke(it) }
            }
            mRightContainer.addView(this, 0)
        }
    }

    /**
     * toolbar分割线布局
     */
    fun divider(@DimenRes height: Int = R.dimen.dimen_05, @ColorRes color: Int = if (isLight) R.color.gray_e3 else R.color.white) {
        mDivider.run {
            if (height in setOf(0, -1)) {
                visibility = View.GONE
                layoutParams.height = 0
            } else {
                visibility = View.VISIBLE
                layoutParams.height = dimen(height)
                setBackgroundColor(context.color(color))
            }
        }
    }

    /**
     * toolbar底部扩展布局
     */
    fun <T : View> extend(v: T, size: Pair<Int, Int>? = null, @DrawableRes background: Int? = null, @DrawableRes parentBackground: Int? = R.drawable.bg_item_white_ef_line,
                          parentSize: Pair<Int, Int>? = null, margRect: Rect? = Rect(0, 0, 0, 0)): T {

        return v.apply {

            mExtendContainer.addView(this)

            background?.let { this.background = context.drawable(it) }

            parentBackground?.let { mExtendContainer.background = context.drawable(it) }

            size?.run {
                layoutParams.width = if (first > 0) dp2px(first) else first
                layoutParams.height = if (second > 0) dp2px(second) else second
            }

            parentSize?.run {
                mExtendContainer.layoutParams.width = if (first > 0) dp2px(first) else first
                mExtendContainer.layoutParams.height = if (second > 0) dp2px(second) else second
            }

            margRect?.run {
                (layoutParams as FrameLayout.LayoutParams).run {
                    setMargins(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom))
                }
            }
        }
    }

    fun <T : View> extend(@LayoutRes layout: Int, size: Pair<Int, Int>? = null, @DrawableRes background: Int? = null, @DrawableRes parentBackground: Int? = null,
                          parentSize: Pair<Int, Int>? = null, margRect: Rect? = Rect(0, 0, 0, 0)): T {
        return extend(inflate(context, layout, null) as T, size, background, parentBackground, parentSize, margRect)
    }

    /**
     * 一个activity放多个fragment时需要重置布局
     */
    fun reset(view: View? = null) {
        initedViews.forEach { if (view == null || view == it) it.removeAllViews() }
    }
}