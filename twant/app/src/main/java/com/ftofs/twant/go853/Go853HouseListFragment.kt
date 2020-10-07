package com.ftofs.twant.go853

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.GoHouseListFragmentBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

class Go853HouseListFragment :BaseTwantFragmentMVVM<GoHouseListFragmentBinding,GoHouseViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.go_house_list_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.title.apply {
            text ="房產"
            setLeftImageResource(R.drawable.icon_back)
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        binding.banner.apply {
            setBackgroundResource(R.drawable.go_banner)
        }
    }


}