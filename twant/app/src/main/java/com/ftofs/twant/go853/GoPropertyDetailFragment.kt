package com.ftofs.twant.go853

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.GoHouseListFragmentBinding
import com.ftofs.twant.databinding.GoPropertyDetailFragmentBinding
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

class GoPropertyDetailFragment @JvmOverloads constructor(private val pid: Int=-1,private val propertyVo: PropertyVo?=null) : BaseTwantFragmentMVVM<GoPropertyDetailFragmentBinding,GoHouseViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.go_property_detail_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        if(pid==-1)hideSoftInputPop()
        binding.title.apply {
            text ="房產詳情頁"
            setLeftImageResource(R.drawable.icon_back)
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
//        binding.banner.apply {
////            mei
//        }
        propertyVo?.let{
            viewModel.currPropertyInfo.postValue(it)
        }?: viewModel.getPropertyDetail(pid)
        binding.btnAboutOwner.setOnClickListener{
            Util.startFragment(GoIntermediaryListFragment(viewModel.currPropertyInfo.value?.uid))
        }
        binding.btnMobile.setOnClickListener{
            viewModel.currPropertyInfo.value?.mobile?.let {
                Util.callPhone(activity,it)
            }
        }
    }

    override fun initViewObservable() {
        viewModel.currPropertyInfo.observe(this){
            binding.vo=it
        }
    }
}
