package com.ftofs.twant.login.ui

import android.content.Intent
import cn.snailpad.easyjson.EasyJSONObject
import com.facebook.CallbackManager
import com.ftofs.lib_common_ui.switchTranslucentMode
import com.ftofs.twant.R
import com.ftofs.twant.TwantApplication.Companion.get
import com.ftofs.twant.databinding.ActivityLoginBinding
import com.gzp.lib_common.base.MBaseActivity
import com.gzp.lib_common.base.callback.SimpleCallBack
import com.gzp.lib_common.model.User
import com.gzp.lib_common.utils.SLog
import com.tencent.mm.opensdk.modelmsg.SendAuth


class LoginActivity : MBaseActivity<LoginViewModel, ActivityLoginBinding>(), SimpleCallBack {

        val callbackManager by lazy { CallbackManager.Factory.create() }

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
                loadRootFragment(R.id.container, findFragment(MessageFragment::class.java)
                        ?: MessageFragment("11"))
            }

        }

        override fun getLayoutResId(): Int {
            return R.layout.activity_login
        }

        override fun onCall() {
            onBackPressedSupport()
        }

    /**
     * 打开微信授权界面
     * @param usage 微信授權用于什么用途
     */
    fun doWeixinLogin(usage: Int) {
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = EasyJSONObject.generate(
                "timestamp", System.currentTimeMillis(),
                "usage", usage).toString()
        get().wxApi!!.sendReq(req)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
    }

//    }
}