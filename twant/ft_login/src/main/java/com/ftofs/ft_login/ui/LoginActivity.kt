package com.ftofs.ft_login.ui

import com.ftofs.ft_login.R
import com.ftofs.ft_login.databinding.ActivityTestBlackBinding
import com.gzp.lib_common.base.MBaseActivity


class LoginActivity :MBaseActivity<LoginViewModel, ActivityTestBlackBinding>(){
    override fun initData() {
    }

    override fun initView() {

    }

    override fun getLayoutResId(): Int {
       return R.layout.activity_test_black
    }
//    override fun initContentView(savedInstanceState: Bundle?): Int {
//        return R.layout.activity_test_black
//    }
//
//    override fun initVariableId(): Int {
//        return BR.viewModel
//    }



}