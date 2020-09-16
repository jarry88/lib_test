package com.ftofs.ft_login.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import com.ftofs.ft_login.R
import com.ftofs.ft_login.databinding.ActivityLoginBinding
import com.ftofs.ft_login.databinding.ActivityTestBlackBinding
import com.gzp.lib_common.base.FixFragmentNavigator
import com.gzp.lib_common.base.MBaseActivity


class LoginActivity :MBaseActivity<LoginViewModel, ActivityLoginBinding>(){

    private lateinit var navController: NavController
    override fun initData() {
    }

    override fun initView() {

    }

    override fun getLayoutResId(): Int {
       return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //添加自定义的FixFragmentNavigator
    }
    private fun addFragment(fragment: Fragment, tag: String) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.replace(R.id.container, fragment, tag)
        beginTransaction.commit()
    }

//    override fun initVariableId(): Int {
//        return BR.viewModel
//    }
}
