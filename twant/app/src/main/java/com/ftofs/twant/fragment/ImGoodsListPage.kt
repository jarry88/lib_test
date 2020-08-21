package com.ftofs.twant.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.FragmentImGoodsPageBinding
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.bean.ImGoodsSearch
import com.ftofs.twant.kotlin.ui.ImGoodsSearch.ImGoodsEnum
import com.ftofs.twant.log.SLog
import com.ftofs.twant.viewmodel.ImGoodsPageModel

class ImGoodsListPage(type:ImGoodsEnum) :BaseTwantFragmentMVVM<FragmentImGoodsPageBinding, ImGoodsPageModel>(){
    val type=type
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_im_goods_page
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        when (type) {
            ImGoodsEnum.RECOMMEND ,ImGoodsEnum.OWNER->{
                binding.toolBar.visibility= View.VISIBLE
                binding.rvStoreLabel.visibility=View.VISIBLE
            }
            else -> SLog.info("others")
        }
        binding.viewModel=viewModel
        binding.tvTest.text="s"
        viewModel.searchType.value=type.searchType
    }

    override fun initViewObservable() {
//        viewModel.showSearch.observe(this, Observer {if (it) binding.toolBar.visibility =})
        viewModel.searchType.observe(this, Observer { viewModel.getImGoodsSearch() })
    }
}
