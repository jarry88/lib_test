package com.ftofs.twant.hot_zone

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.icu.lang.UCharacter
import android.icu.text.CaseMap
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.bumptech.glide.Glide
import com.ftofs.lib_net.model.HotZone
import com.ftofs.lib_net.model.HotZoneInfo
import com.ftofs.lib_net.model.HotZoneVo
import com.ftofs.twant.R
import com.ftofs.twant.adapter.BaseBindAdapter
import com.ftofs.twant.dsl.*
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.login.Title
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.UiUtil
import com.ftofs.twant.util.Util
import com.gzp.lib_common.smart.utils.KLog
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog
import com.qmuiteam.qmui.kotlin.onClick
import kotlinx.android.synthetic.main.seller_edit_features_layout.view.*
import kotlinx.coroutines.delay
//實現單張熱區圖邏輯的UI控件
class HotView @JvmOverloads constructor(context: Context,attrs:AttributeSet?=null,defStyleAttr:Int=0):FrameLayout(context,attrs,defStyleAttr) {

    private var clickX: Float=0f
    private var clickY: Float=0f
    private val endText= MutableLiveData<String>()
    val hotZoneVo  =MutableLiveData<HotZoneVo>()
    private val contentView by lazy {
        LinearLayout {
            layout_width = match_parent
            layout_height= wrap_content
            onClick= {
                SLog.info("點擊了"+it.run { "x :$x, $rotationX,y:$y" })
            }
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
            TextView {
                layout_width= wrap_content
                layout_height= wrap_content
                text="commit"
                textSize=30f
                padding=10
                shape {
                    shape= GradientDrawable.RECTANGLE
                    cornerRadius=10f

                }
                top_toTopOf= parent_id
                onClick = { ToastUtil.success(context,"here")
                    val a=liveData { delay(5000)
                        emit("延時5秒后")
                        endText.postValue("更新后的值")
                    }
                    a.value.apply {
                        SLog.info("延時a秒后")
                        endText.postValue("更新值")

                    }
//                    Thread{
//                        endText.postValue("10")
//                    }
                }
            }
            ImageView {
                background = ColorDrawable(resources.getColor(R.color.black))
                layout_width= match_parent
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
                            SLog.info("點擊了"+e.run { "x :$rawX,y: $rawY" })
                            clickX=e.rawX
                            clickY=e.rawY
                            onClickAction(e.rawX,e.rawY)
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
                            h.originalHeight?.let {
//                                ll_height=2*it.toInt()*(h.originalWidth?.toInt()?:0)/Util.getScreenDimension(BaseContext.instance.getContext()).first
                                ll_height=it.toInt()


//                                ll_width=
                            }
                            h.originalWidth?.let {
//                                ll_height=2*it.toInt()*(h.originalWidth?.toInt()?:0)/Util.getScreenDimension(BaseContext.instance.getContext()).first
                                ll_width=it.toInt()


//                                ll_width=
                            }
                        }
                    }
                }
//                top_toTopOf = parent_id
            }

        }
    }

    private fun onClickAction(rawX: Float=clickX, rawY: Float=clickY) {
        SLog.info("  ")
        hotZoneVo.value?.apply {
            hotZoneList?.filter {
                during(rawX,x,width.toFloat())&&during(rawY,y,height.toFloat())
            }?.first()?.apply {
                SLog.info(linkType)
                Util.onLinkTypeAction(linkType,linkValue)
            }
        }
    }
//rawX是不是在区间范围内
    private fun during(rawX: Float, x: Float, width: Float)=rawX>=x&&rawX<=x+width

    init {
        contentView
    }


}