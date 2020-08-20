package com.ftofs.twant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.FragmentImGoodsPageBinding
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.viewmodel.ImGoodsPageModel

class ImGoodsListPage(val title: String) :BaseTwantFragmentMVVM<FragmentImGoodsPageBinding, ImGoodsPageModel>(){

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_im_goods_page
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.viewModel=viewModel
        binding.tvTest.text="s"
        viewModel.changeSearch()
    }

    override fun initViewObservable() {
//        viewModel.showSearch.observe(this, Observer {if (it) binding.toolBar.visibility =})
    }
}
