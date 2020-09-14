package com.ftofs.twant.kotlin.testnet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.FragmentTestNetBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.wzq.mvvmsmart.utils.KLog

/**
 * Create Date：2019/01/25
 * Description：RecycleView多布局实现
 */
class TestNetFragment : BaseTwantFragmentMVVM<FragmentTestNetBinding, TestNetViewModel>() {
    private var count:Int = 0
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_test_net
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initViewObservable() {
        binding.button.setOnClickListener { _: View? ->
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
    override fun onSupportVisible() {
        super.onSupportVisible()
//        binding.btnToB.postDelayed({
//            count++
//            SLog.info("由A啓動B[%d]",count)
//            binding.btnToB.performClick()
//        }, 100)
    }
}