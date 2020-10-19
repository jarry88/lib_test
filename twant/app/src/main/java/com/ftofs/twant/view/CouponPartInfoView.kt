package com.ftofs.twant.view

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ftofs.lib_net.model.CouponDetailVo
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CouponInfoWighetBinding
import com.ftofs.twant.databinding.SmartListViewBinding
import com.ftofs.twant.dsl.*
import com.ftofs.twant.dsl.customer.factoryAdapter

class CouponPartInfoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) ,LifecycleObserver{
    var currPage =0
    var hasMore =true
    var fragment:Fragment?=null
    val inflater =context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val mBinding =DataBindingUtil.inflate<CouponInfoWighetBinding>(inflater,R.layout.coupon_info_wighet,this,true)
    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartList)
//        typedArray.getString(R.styleable.Title_text_title)?.let {
//            text=it
//        }
//        //不設置的時候是顯示的
//        typedArray.getBoolean(R.styleable.Title_login_info, false).takeIf { it }?.let {
//            rootView.findViewById<View>(R.id.tv_info)?.visibility= View.VISIBLE
//        }
//        typedArray.getBoolean(R.attr.login_info,false).let {
//            if(it) rootView.findViewById<TextView>(R.id.tv_info)?.visibility= VISIBLE
//        }
        typedArray.recycle()

    }
    //如果控件依附于fragment则调用这个方法，传入fragment
    fun attachFragment(fragment: Fragment) {
        this.fragment = fragment
    }
    //这个方法用于返回LifecycleOwner
    private fun getLifecycleOwner(): LifecycleOwner? {
        if (fragment == null) {
            if (context is LifecycleOwner) {
                return context as LifecycleOwner
            }
        } else {
            if (fragment is LifecycleOwner) {
                return fragment
            }
        }
        return null
    }
    fun observable(vo:LiveData<CouponDetailVo?>){
        getLifecycleOwner()?.let {
            vo.observe(it){t->
                t?.let { mBinding.vo=t
                    mBinding.llPkgContainer.apply {
                        removeAllViews()
                        it.pkgList.forEach {pkg ->
                            addView(LinearLayout {
                                layout_width= match_parent
                                layout_height= wrap_content
                                center_vertical =true
                                TextView {
                                    layout_width= wrap_content
                                    layout_height= wrap_content
                                    margin_start=13
                                    textStyle= bold
                                    colorId=R.color.tw_black
                                    textSize=16f
                                    text="•"
                                }
                                TextView {
                                    layout_width= wrap_content
                                    layout_height= wrap_content
                                    layout_gravity = gravity_center_vertical
                                    text = pkg.title
                                    textSize =14f
                                    textStyle= bold
                                    setTextColor(resources.getColor(R.color.tw_black))
                                    id=R.id.item_title
                                    margin_start=4

                                    isSingleLine =true
                                }

                            }.let { r ->( r.parent as? ViewGroup)?.removeView(r)
                                r })

                            pkg.childItems.forEach {child ->
                                addView(LinearLayout {
                                    layout_height = wrap_content
                                    layout_width= wrap_content
                                    orientation = horizontal
                                    padding=6
                                    padding_start=28
                                    padding_end=14
                                    TextView {
                                        colorId =R.color.tw_black
                                        layout_height= wrap_content
                                        layout_width= wrap_content
                                        textSize=14f
                                        text = SpannableStringBuilder("${child.title}  ${child.unit}").also { s->
                                            s.setSpan(ForegroundColorSpan(resources.getColor(R.color.tw_grey_666)),s.length-child.unit.length,s.length-1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                        }
                                    }
                                    View{
                                        layout_height= wrap_content
                                        layout_width=0
                                        weight=1f
                                    }
                                    TextView {
                                        layout_height= wrap_content
                                        layout_width= wrap_content
                                        textSize=15f
                                        text= SpannableStringBuilder("$${child.price}").also { s->
                                            s.setSpan(AbsoluteSizeSpan(11,true),0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                                        }
                                        textStyle= bold
                                        colorId=R.color.tw_black
                                    }

                                }.let { r ->( r.parent as? ViewGroup)?.removeView(r)
                                    r })
                            }
                        }
                    }
                }
            }
        }
    }
}