package com.ftofs.ft_login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.MainThread
import com.ftofs.ft_login.BR
import com.ftofs.ft_login.R
import com.ftofs.ft_login.databinding.LayoutHistoryLoginBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog

class HistoryLoginFragment:BaseTwantFragmentMVVM<LayoutHistoryLoginBinding,HistoryLoginViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.layout_history_login
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.tvOneStep.setOnClickListener { parentFragmentManager.beginTransaction().addToBackStack("OneStepLoginFragment").replace(R.id.container,OneStepLoginFragment()).commit() }
        binding.tvTwoStep.setOnClickListener { start(OneStepLoginFragment()) }
    }

    override fun onBackPressedSupport(): Boolean {
        SLog.info("here")
        parentFragment?.activity?.onBackPressed()
        hideSoftInputPop()
        return true
    }


}