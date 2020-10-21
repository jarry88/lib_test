package com.ftofs.twant.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.ftofs.lib_net.model.CouponDetailVo
import com.ftofs.lib_net.model.CouponItemVo
import com.ftofs.lib_net.model.Store
import com.ftofs.twant.R
import com.ftofs.twant.databinding.CouponListItemWighetBinding
import com.ftofs.twant.databinding.StoreInfoWighetBinding
import com.ftofs.twant.entity.StoreMapInfo
import com.ftofs.twant.kotlin.setVisibleOrGone
import com.ftofs.twant.util.Util
import com.ftofs.twant.widget.AmapPopup
import com.lxj.xpopup.XPopup

class CouponListItemView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){
    var fragment:Fragment?=null
    val inflater =context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val mBinding =DataBindingUtil.inflate<CouponListItemWighetBinding>(inflater, R.layout.coupon_list_item_wighet, this, true)
    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CouponListItemView)
        //不設置的時候是顯示的
        typedArray.getType(R.styleable.CouponListItemView_coupon_type).let {
           mBinding.apply {
               tvBottomPrice.setVisibleOrGone(it == 2)//detail
               tvValidity.setVisibleOrGone(it == 2)
               tvEndPrice.setVisibleOrGone(it == 1)
               tvSubTitle.setVisibleOrGone(it == 1)
           }
        }
        typedArray.recycle()


    }
    //如果控件依附于fragment则调用这个方法，传入fragment
    fun attachFragment(fragment: Fragment) {
        this.fragment = fragment
    }
    fun updateVo(vo: CouponItemVo?)=vo?.let { mBinding.vo=it }
}