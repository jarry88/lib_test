package com.ftofs.twant.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.ftofs.twant.R
import com.ftofs.twant.databinding.SmartListViewBinding
import com.ftofs.twant.dsl.customer.factoryAdapter
const val DEFAULT_SIZE = 200

const val  DEFAULT_RADIUS = 20

const val  TYPE_ROUND = 1
const val TYPE_RECT = 2

class CircleView@JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
)  : View (context,attrs,defStyleAttr){




    var mSize= DEFAULT_SIZE

    var mResourceId :Int=0

    var mType=0

    val mPaint by lazy { with(Paint()){
        setLayerType(LAYER_TYPE_HARDWARE, null)
        isAntiAlias=true

        style=Paint.Style.FILL
        this
    } }
    val mSrcBitmap by lazy { BitmapFactory.decodeResource(resources,mResourceId,BitmapFactory.Options().let {
        it.inJustDecodeBounds=true
        BitmapFactory.decodeResource(resources,mResourceId,it)
        it.inSampleSize=calcSampleSize(it,mSize,mSize)
        it.inJustDecodeBounds=false
        it
    }) }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView)
        mResourceId = typedArray.getResourceId(R.styleable.CircleView_src, R.mipmap.ic_launcher)
        mType = typedArray.getInt(R.styleable.CircleView_type, TYPE_ROUND)
        typedArray.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mSize=Math.min(measuredWidth,heightMeasureSpec)
        setMeasuredDimension(mSize,mSize)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        //绘制要显示的图像
        //重置Xfermode
        mPaint.xfermode = null
    }




    /**
     * 计算缩放比例
     *
     * @param option
     * @param width
     * @param height
     * @return
     */
    fun calcSampleSize( option:BitmapFactory.Options,  width:Int, height:Int):Int {
        var originWidth = option.outWidth
        var originHeight = option.outHeight
        var sampleSize = 1
        while ( originWidth.shr(1) > width && originHeight.shr(1) > height) {
        sampleSize = sampleSize.shl(1)
    }
        return sampleSize
    }
}