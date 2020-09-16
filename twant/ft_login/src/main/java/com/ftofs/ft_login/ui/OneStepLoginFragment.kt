package com.ftofs.ft_login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.ft_login.R
import com.ftofs.ft_login.BR
import com.ftofs.ft_login.databinding.LayoutHistoryLoginBinding
import com.ftofs.ft_login.databinding.OneStepLoginLayoutBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM

class OneStepLoginFragment:BaseTwantFragmentMVVM<OneStepLoginLayoutBinding,HistoryLoginViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.one_step_login_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

}