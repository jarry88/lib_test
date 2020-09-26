package com.ftofs.twant.hot_zone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.FragmentHotzoneBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

class HotZoneFragment(private val hotId:Int) :BaseTwantFragmentMVVM<FragmentHotzoneBinding,HotZoneViewModel> (){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_hotzone
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        viewModel.getHotZoneData(hotId)
    }
}