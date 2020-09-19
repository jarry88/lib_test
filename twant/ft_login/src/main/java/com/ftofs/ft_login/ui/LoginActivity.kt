package com.ftofs.ft_login.ui

import com.ftofs.ft_login.R
import com.ftofs.ft_login.databinding.ActivityLoginBinding
import com.ftofs.lib_common_ui.switchTranslucentMode
import com.gzp.lib_common.base.MBaseActivity
import com.gzp.lib_common.base.callback.SimpleCallBack
import com.gzp.lib_common.model.User
import com.gzp.lib_common.utils.SLog


class LoginActivity : MBaseActivity<LoginViewModel, ActivityLoginBinding>(), SimpleCallBack {
    override fun initData() {
    }

    override fun initView() {
        setContentView(R.layout.activity_login)
        switchTranslucentMode(this,false)
        intent.getParcelableExtra<User>("user")?.let {
            SLog.info("有歷史數據")
            loadRootFragment(R.id.container, findFragment(HistoryLoginFragment::class.java)?:HistoryLoginFragment(it))

        }?:loadRootFragment(R.id.container,findFragment(OneStepLoginFragment::class.java)?:OneStepLoginFragment())
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
