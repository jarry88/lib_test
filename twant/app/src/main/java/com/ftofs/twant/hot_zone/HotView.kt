package com.ftofs.twant.hot_zone

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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
import com.ftofs.twant.adapter.BaseBindAdapter
import com.ftofs.twant.dsl.*
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.login.Title
import com.ftofs.twant.util.ToastUtil
import com.gzp.lib_common.utils.SLog
import kotlinx.coroutines.delay
//實現單張熱區圖邏輯的UI控件
class HotView @JvmOverloads constructor(context: Context,attrs:AttributeSet?=null,defStyleAttr:Int=0,private val hotZoneVo: HotZoneVo):FrameLayout(context,attrs,defStyleAttr) {
    private val endText= MutableLiveData<String>()
    private val hotZoneInfo =MutableLiveData<HotZoneInfo>()
    private val contentView by lazy {
        ConstraintLayout {
            layout_width = match_parent
            layout_height= match_parent
            onClick= {
                SLog.info("點擊了"+it.run { "x :$x, $rotationX,y:$y" })
            }
            onTouchEvent={v,e->
                when (e.action) {
                    MotionEvent.ACTION_DOWN->{SLog.info("點擊了"+e.run { "x :$x,$xPrecision,\n$rawX,y: $y,$yPrecision,\n$rawY" })
                        true
                    }
                    MotionEvent.ACTION_UP ->v.performClick()
                    else ->performClick()
                }
            }
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
                layout_width= match_parent
                layout_height= match_parent
                onTouchEvent={v,e->
                    when (e.action) {
                        MotionEvent.ACTION_DOWN->{SLog.info("點擊了"+e.run { "x :$x,$xPrecision,\n$rawX,y: $y,$yPrecision,\n$rawY" })
                            true
                        }
                        MotionEvent.ACTION_UP ->v.performClick()
                        else ->performClick()
                    }
                }
                bindLiveData= liveDataBinder(hotZoneInfo){
                    action ={
                        SLog.info("觀測到之變")
                        (it as? HotZoneInfo)?.let{ h ->
//                            text =s
                        }
                    }
                }
            }
            Title(context
            ).apply {
                layout_width = match_parent
                layout_height= wrap_content
                top_toTopOf= parent_id

                layout_id= "title"
                bindLiveData= liveDataBinder(hotZoneInfo){
                    action ={
                        SLog.info("觀測到之變")
                        (it as? HotZoneInfo)?.let{ h ->
                            text =h.hotName
                        }
                    }
                }
            }
        }
    }
    init {
        contentView
    }


}