package com.ftofs.ft_login.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Message
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import com.alibaba.fastjson.JSON
import com.ftofs.ft_login.R
import com.ftofs.ft_login.databinding.ActivityLoginBinding
import com.ftofs.ft_login.utils.ExecutorManager
import com.ftofs.lib_common_ui.switchTranslucentMode
import com.gzp.lib_common.base.MBaseActivity
import com.gzp.lib_common.base.callback.SimpleCallBack
import com.gzp.lib_common.model.User
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.ToastUtil
import com.mobile.auth.gatewayauth.*
import com.mobile.auth.gatewayauth.model.TokenRet
import com.qmuiteam.qmui.util.QMUIDisplayHelper.dp2px
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog


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