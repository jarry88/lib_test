package com.ftofs.twant.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.TestBlackFragmentBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.smart.base.BaseFragmentMVVM
import com.gzp.lib_common.smart.utils.KLog
import com.gzp.lib_common.smart.utils.ToastUtils
import com.gzp.lib_common.utils.ToastUtil

class BlackTestFragment :BaseTwantFragmentMVVM<TestBlackFragmentBinding,TestViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.test_black_fragment
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.button1.setOnClickListener {
            viewModel.getData()
        }
        binding.button2.setOnClickListener {
            ToastUtil.success(context,"打开")
            KLog.init(true)
        }
    }
}