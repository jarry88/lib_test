package com.ftofs.twant.hot_zone

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.core.view.marginTop
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.model.HotZoneVo
import com.ftofs.twant.R
import com.ftofs.twant.dsl.*
import com.ftofs.twant.util.Util
import com.gzp.lib_common.utils.SLog
import java.lang.Exception

//實現單張熱區圖邏輯的UI控件
class HotView @JvmOverloads constructor(context: Context,attrs:AttributeSet?=null,defStyleAttr:Int=0):FrameLayout(context,attrs,defStyleAttr) {

    private var clickX: Float=0f
    private var clickY: Float=0f
    private var xP: Float=0f
    private var yP: Float=0f
    private val endText= MutableLiveData<String>()
    val hotZoneVo  =MutableLiveData<HotZoneVo>()
    private val contentView by lazy {
        LinearLayout {
            layout_width = match_parent
            layout_height= wrap_content
            margin_top = 10
            onClick= {
                SLog.info("點擊了"+it.run { "x :$x, $rotationX,y:$y" })
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
                layout_width= wrap_content
                layout_height= wrap_content
                scaleType = scale_fit_xy
                //保持比例
                adjustViewBounds =true
                onClick={
                    onClickAction()
                }

                onTouchEvent={v,e->
                    when (e.action) {
                        MotionEvent.ACTION_UP -> {
                            updateP(width,height)
                            clickX = e.rawX - this@HotView.x
                            clickY = e.rawY - this@HotView.y - marginTop
                            SLog.info("點擊了" + e.run { "x :$rawX,y: $rawY" })
                            SLog.info("點擊了" + e.run { "x :$x,y: $y" })
                            onClickAction(e.rawX, e.rawY)
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
                            SLog.info("點擊了:sumx :$width,sumy: $height" )
//                            h.originalHeight?.let {
////                                ll_height=2*it.toInt()*(h.originalWidth?.toInt()?:0)/Util.getScreenDimension(BaseContext.instance.getContext()).first
//                                ll_height=it.toInt()
//
//
////                                ll_width=
//                            }
//                            h.originalWidth?.let {
////                                ll_height=2*it.toInt()*(h.originalWidth?.toInt()?:0)/Util.getScreenDimension(BaseContext.instance.getContext()).first
//                                ll_width=it.toInt()
//
//
////                                ll_width=
//                            }
                        }
                    }
                }
//                top_toTopOf = parent_id
            }

        }
    }

    private fun updateP(width:Int,height:Int) {
        val h= hotZoneVo.value
        h?.originalWidth?.let { it ->
            xP=width/it.toFloat()
        }
        h?.originalHeight?.let { it ->
            yP=height/it.toFloat()
        }
    }

    private fun onClickAction(rawX: Float=clickX, rawY: Float=clickY) {
        SLog.info("點擊了"+"clickX :${clickX/xP},clickY: ${clickY/yP}" )

        hotZoneVo.value?.apply {
            try {
                hotZoneList?.forEach { it.toString() }
                SLog.info("  ${hotZoneList?.size}")
            hotZoneList?.filter {

                    during(rawX,x,it.width?.toFloat()?:0f,xP)&&during(rawY,y,it.height?.toFloat()?:0f,yP)

            }?.apply { forEach{it.toString()} }?.firstOrNull()?.apply {
                SLog.info(linkType+" x${x},xw ${x?.toFloat()?:0f+width!!.toFloat()},y $y,h,$height")
                Util.onLinkTypeAction(linkType,linkValue)
            }
            }catch (e:Exception){
            SLog.info("%s",e.toString())
        }
        }
    }
//rawX是不是在区间范围内
    private fun during(rawX: Float, x: Float, width: Float,p:Float)=(rawX/p).run {
    SLog.info(this.toString()+"${x}+ ${x+width}")
    this>=x&&this<=x+width
}

    init {
        contentView
    }


}