package com.ftofs.twant.kotlin.testnet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ftofs.twant.R
import com.ftofs.twant.databinding.FragmentTestNetBinding
import com.ftofs.twant.kotlin.BaseTwantFragmentMVVM
import com.wzq.mvvmsmart.utils.KLog
import com.ftofs.twant.BR

/**
 * Create Date：2019/01/25
 * Description：RecycleView多布局实现
 */
class TestNetFragment : BaseTwantFragmentMVVM<FragmentTestNetBinding, TestNetViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_test_net
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewObservable() {
        super.initViewObservable()
        binding.button.setOnClickListener { v: View? ->
            KLog.e("发起请求")
            viewModel.doPostServerNews() // 请求网络数据;
        }
        viewModel.liveData.observe(this, Observer {
            if (it.isNotEmpty()) {
                binding.tvJson.text = it[0].news_summary
            }
        })
    }

    override fun onBackPressedSupport(): Boolean {
        hideSoftInputPop()
        return true
    }
}