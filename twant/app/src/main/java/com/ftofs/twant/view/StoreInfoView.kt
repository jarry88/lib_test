package com.ftofs.twant.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.ftofs.lib_net.model.CouponDetailVo
import com.ftofs.lib_net.model.Store
import com.ftofs.twant.R
import com.ftofs.twant.databinding.StoreInfoWighetBinding
import com.ftofs.twant.entity.StoreMapInfo
import com.ftofs.twant.fragment.ShopMainFragment
import com.ftofs.twant.fragment.StoreHomeFragment
import com.ftofs.twant.util.Util
import com.ftofs.twant.widget.AmapPopup
import com.gzp.lib_common.utils.SLog
import com.lxj.xpopup.XPopup

class StoreInfoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) ,LifecycleObserver{
    var currPage =0
    var hasMore =true
    var fragment:Fragment?=null
    val inflater =context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val mBinding =DataBindingUtil.inflate<StoreInfoWighetBinding>(inflater, R.layout.store_info_wighet, this, true)
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartList)
        mBinding.btnContact.setOnClickListener {
            mBinding.vo?.contact?.takeIf { it.isNotEmpty() }?.let {
                Util.dialPhone(Util.findActivity(context), it)
            }
        }
        mBinding.btnPosition.setOnClickListener {
            mBinding.vo?.let {
                XPopup.Builder(context) // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(AmapPopup(Util.findActivity(context), StoreMapInfo(
                                it.lnt?:0.0,it.lat?:0.0,it.name,it.address,it.contact
                        )))
                        .show()
            }
        }
        mBinding.root.setOnClickListener {
            mBinding.vo?.let {
                Util.startFragment(ShopMainFragment.newInstance(it.platformStoreId.apply { SLog.info("storeId $it") }))
            }

        }
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
    fun observable(vo: LiveData<CouponDetailVo?>){
        getLifecycleOwner()?.let {
            vo.observe(it){ t->
                t?.let { mBinding.vo=t.store
                }
            }
        }
    }
    fun updateVo(vo: Store?)=vo?.let { mBinding.vo=it }
}