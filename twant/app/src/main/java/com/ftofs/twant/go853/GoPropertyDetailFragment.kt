package com.ftofs.twant.go853

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.GoPropertyDetailFragmentBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

class GoPropertyDetailFragment @JvmOverloads constructor(private val pid: Int=-1) : BaseTwantFragmentMVVM<GoPropertyDetailFragmentBinding,GoHouseViewModel>() {
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
        binding.banner.apply {
//            mei
        }
        viewModel.getPropertyDetail(pid)
    }

    override fun initViewObservable() {
        viewModel.currPropertyInfo.observe(this){
            binding.vo=it
        }
    }
}
