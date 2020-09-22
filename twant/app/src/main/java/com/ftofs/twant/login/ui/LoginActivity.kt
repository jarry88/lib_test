package com.ftofs.twant.login.ui

import com.ftofs.twant.R
import com.ftofs.twant.databinding.ActivityLoginBinding
import com.ftofs.lib_common_ui.switchTranslucentMode
import com.gzp.lib_common.base.MBaseActivity
import com.gzp.lib_common.base.callback.SimpleCallBack
import com.gzp.lib_common.model.User
import com.gzp.lib_common.utils.SLog


class LoginActivity : MBaseActivity<LoginViewModel, ActivityLoginBinding>(), SimpleCallBack {


        override fun initData() {
    //        TODO("Not yet implemented")
        }
        override fun initView() {
            setContentView(R.layout.activity_login)
            switchTranslucentMode(this, false)

//            mUIType = intent.getSerializableExtra(THEME_KEY) as Constant.UI_TYPE
//
//            mTvResult = findViewById<TextView>(R.id.tv_result)
//            mLoginBtn = findViewById<Button>(R.id.btn_login)

            //如果用户点击登录的时候，checkEnvAvailable和accelerateLoginPage接口还没有完成，不需要等待直接拉起授权页
            val user=intent.getParcelableExtra<User>("user")
            if (user != null) {
                SLog.info("有歷史數據")
                loadRootFragment(R.id.container, findFragment(HistoryLoginFragment::class.java)
                        ?: HistoryLoginFragment(user))
            } else {
                loadRootFragment(R.id.container ,findFragment(MessageFragment::class.java)
                            ?: MessageFragment("11"))
            }

        }

        override fun getLayoutResId(): Int {
            return R.layout.activity_login
        }

        override fun onCall() {
            onBackPressedSupport()
        }


//    }
}