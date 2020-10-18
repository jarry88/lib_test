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
import com.ftofs.twant.databinding.StoreInfoWighetBinding
import com.ftofs.twant.dsl.*
import com.ftofs.twant.dsl.customer.factoryAdapter

class StoreInfoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) ,LifecycleObserver{
    var currPage =0
    var hasMore =true
    var fragment:Fragment?=null
    val inflater =context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val mBinding =DataBindingUtil.inflate<StoreInfoWighetBinding>(inflater,R.layout.store_info_wighet,this,true)
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

                }
            }
        }
    }
}