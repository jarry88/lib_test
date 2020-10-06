package com.ftofs.twant.hot_zone

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.model.HotZoneVo
import com.ftofs.twant.R
import com.ftofs.twant.dsl.*
import com.ftofs.twant.util.Util
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog

//實現單張熱區圖邏輯的UI控件
class HotView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):FrameLayout(context, attrs, defStyleAttr) {

    private var clickX: Float=0f
    private var clickY: Float=0f
    private var xP: Float=0f
    private var yP: Float=0f
    private var mImageView:ImageView?=null
    private val endText= MutableLiveData<String>()
    val hotZoneVo  =MutableLiveData<HotZoneVo>()
    private val contentView by lazy {
        LinearLayout {
            layout_width = match_parent
            layout_height= wrap_content
            margin_top = 10
            onClick= {
                SLog.info("點擊了" + it.run { "x :$x,y:$y,\n rotationX $rotationX,,rotationY:$rotationY  \n  " })
            }
            gravity = gravity_center
            orientation= vertical
//            onTouchEvent={v,e->
//                when (e.action) {
//                    MotionEvent.ACTION_DOWN->{SLog.info("點擊了"+e.run { "x :$x,$xPrecision,\n$rawX,y: $y,$yPrecision,\n$rawY" })
//                        clickX=e.rawX
//                        clickY=e.rawY
//                        true
//                    }
//                    MotionEvent.ACTION_UP ->v.performClick()
//                    else ->performClick()
//                }
//            }
            ImageView {
                background = ColorDrawable(resources.getColor(R.color.black))
                layout_width= match_parent
                layout_height= wrap_content
                scaleType = scale_fit_xy
                //保持比例
                adjustViewBounds =true
//                onClick={
//                    onClickAction(clickX,clickY)
//                }

                onTouchEvent={ v, e->
                    when (e.action) {
                        MotionEvent.ACTION_UP -> {
                            updateP(width, height)
                            clickX = x
                            clickY = y
                            SLog.info("點擊了" + e.run { "x :$x,y: $y" })
                            e.run { onClickAction(x, y) }
//                            performClick()
                            true
                        }
                        else ->true
                    }
                }
                src=R.drawable.activity_discount_label_bg
                bindLiveData= liveDataBinder(hotZoneVo){
                    action ={
                        SLog.info("觀測到之變")
                        (it as? HotZoneVo)?.let{ h ->
                            imageUrl=h.url

                            layout_height=h.run { originalHeight?.toInt()?:0*(originalWidth?.toInt()?:0)/Util.getScreenDimension(BaseContext.instance.getContext()).first  }
//                            SLog.info("點擊了:sumx :$width,sumy: $height" )

                        }
                    }
                }
                mImageView=this@ImageView
//                top_toTopOf = parent_id
            }

        }
    }

    private fun updateP(width: Int, height: Int) {
        val h= hotZoneVo.value
        h?.originalWidth?.let { it ->
            xP=width/it.toFloat()
        }
        h?.originalHeight?.let { it ->
            yP=height/it.toFloat()
        }
//        SLog.info("更新了xP $xP,yP $yP")
    }

    private fun onClickAction(imgX: Float, imgY: Float) {
        SLog.info("點擊了" + ",clickX :${imgX / xP}, imgY: ${imgY / yP}")

        hotZoneVo.value?.apply {
            try {
                hotZoneList?.forEach {
                    SLog.info(it.linkType + it.x + " " + it.width + "y," + it.y + " " + it.height)
                    if (during(imgX, it.x?.toFloat() ?: 0f, it.width?.toFloat()
                                    ?: 0f, xP) && during(imgY, it.y?.toFloat()
                                    ?: 0f, it.height?.toFloat() ?: 0f, yP)) {
                        Util.onLinkTypeAction(it.linkType, it.linkValue)
                        return
                    } else {
                        SLog.info("  dd")
                    }

                }
            }catch (e: Exception){
            SLog.info("%s", e.toString())
        }
        }
    }
//rawX是不是在区间范围内
    private fun during(rawX: Float, x: Float, width: Float, p: Float)=(rawX/p).run {
    SLog.info(this.toString() + "  ${x}+ ${x + width}")
    this>=x&&this<=x+width
}

    init {
        contentView
    }


}