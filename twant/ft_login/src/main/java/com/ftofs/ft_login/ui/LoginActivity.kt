package com.ftofs.ft_login.ui

import android.media.MediaRouter
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.ftofs.ft_login.R
import com.ftofs.ft_login.databinding.ActivityLoginBinding
import com.gzp.lib_common.base.MBaseActivity
import com.gzp.lib_common.base.callback.SimpleCallBack
import com.gzp.lib_common.utils.SLog


class LoginActivity :MBaseActivity<LoginViewModel, ActivityLoginBinding>(), SimpleCallBack {
    private lateinit var navController: NavController
    override fun initData() {
    }

    override fun initView() {
        loadRootFragment(R.id.container, findFragment(HistoryLoginFragment::class.java)?:HistoryLoginFragment(this))

    }

    override fun getLayoutResId(): Int {
       return R.layout.activity_login
    }

    override fun onCall() {
         onBackPressedSupport()
    }

//
//    private fun addFragment(fragment: Fragment, tag: String) {
//        val beginTransaction = supportFragmentManager.beginTransaction()
//        beginTransaction.replace(R.id.container, fragment, tag)
//        beginTransaction.commit()
//    }
}
