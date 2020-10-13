package com.ftofs.twant.go853

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.GoIntermediaryListFragmentBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

class GoIntermediaryListFragment @JvmOverloads constructor(private val uid: Int?=null) : BaseTwantFragmentMVVM<GoIntermediaryListFragmentBinding,GoHouseViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.go_intermediary_list_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.title.apply {
            setLeftLayoutClickListener() { onBackPressedSupport() }
            setLeftImageResource(R.drawable.icon_back)
        }
        viewModel.currUid=uid
        binding.refreshLayout.setOnRefreshListener {
            viewModel.getUserPropertyList()
        }
    }

    override fun initViewObservable() {
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){

        }
    }

}
