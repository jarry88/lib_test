package com.ftofs.ft_login.ui

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.ftofs.ft_login.R
import com.ftofs.ft_login.databinding.ActivityLoginBinding
import com.gzp.lib_common.base.MBaseActivity
import com.gzp.lib_common.utils.SLog


class LoginActivity :MBaseActivity<LoginViewModel, ActivityLoginBinding>(){
    var lastBackPressedTime =1
    private lateinit var navController: NavController
    override fun initData() {
    }

    override fun initView() {
//        addFragment(HistoryLoginFragment(),"history")
        loadRootFragment(R.id.container, findFragment<HistoryLoginFragment>(HistoryLoginFragment::class.java)?:HistoryLoginFragment())

    }

    override fun getLayoutResId(): Int {
       return R.layout.activity_login
    }

    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
        SLog.info(supportFragmentManager.backStackEntryCount.toString())
    }


    private fun addFragment(fragment: Fragment, tag: String) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.replace(R.id.container, fragment, tag)
        beginTransaction.commit()
    }
}
