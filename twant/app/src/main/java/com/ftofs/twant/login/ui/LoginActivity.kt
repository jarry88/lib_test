package com.ftofs.twant.login.ui

import android.content.Intent
import cn.snailpad.easyjson.EasyJSONObject
import com.facebook.CallbackManager
import com.ftofs.lib_common_ui.switchTranslucentMode
import com.ftofs.twant.R
import com.ftofs.twant.TwantApplication.Companion.get
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.constant.EBMessageType
import com.ftofs.twant.constant.LoginType
import com.ftofs.twant.databinding.ActivityLoginBinding
import com.ftofs.twant.entity.EBMessage
import com.ftofs.twant.fragment.BindMobileFragment
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.MBaseActivity
import com.gzp.lib_common.base.callback.SimpleCallBack
import com.gzp.lib_common.model.User
import com.gzp.lib_common.utils.SLog
import com.lxj.xpopup.core.BasePopupView
import com.tencent.mm.opensdk.modelmsg.SendAuth
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LoginActivity : MBaseActivity<LoginViewModel, ActivityLoginBinding>(), SimpleCallBack {

    val callbackManager by lazy { CallbackManager.Factory.create() }
    lateinit var mLoadingPopup: BasePopupView
    fun showLoading(){
        mLoadingPopup.show()

    }
    override fun initData() {
            SLog.info("啓動activiy")
    //        TODO("Not yet implemented")
        }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEBMessage(message: EBMessage) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_WEIXIN_LOGIN) {
            val code = message.data as String
            SLog.info("接受到了衛星信息$code")
            mViewModel.getWXLoginStepOne(code)
        }
    }
        override fun initView() {
            setContentView(R.layout.activity_login)
            mLoadingPopup = Util.createLoadingPopup(this)

            EventBus.getDefault().register(this)

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
                        ?: MessageFragment("", false, true))
            }
            mViewModel.weChatInfo.observe(this){
                SLog.info("檢測到了wechatInfo")
                mLoadingPopup.dismiss()
                if (it.isBind == Constant.FALSE_INT) {
                    SLog.info("進入綁定頁")

                    start(BindMobileFragment.newInstance(BindMobileFragment.BIND_TYPE_WEIXIN, it.accessToken, it.accessToken))}
                else// 未綁定
                {
                    SLog.info("未進入綁定頁")
                    com.ftofs.twant.util.User.onNewLoginSuccess(it.memberId
                            ?: 0, LoginType.WEIXIN, it)
                    ToastUtil.success(this, "微信登入成功")
                    finish()
                }
            }
            mViewModel.faceBookInfo.observe(this){
                if (it.isBind == Constant.FALSE_INT) start(BindMobileFragment.newInstance(BindMobileFragment.BIND_TYPE_FACEBOOK, mViewModel.faceBookAccessToken?.token, mViewModel.faceBookAccessToken?.userId))
                else// 未綁定
                {
                    com.ftofs.twant.util.User.onNewLoginSuccess(it.memberId
                            ?: 0, LoginType.FACEBOOK, it)

                    ToastUtil.success(this, "Facebook登入成功")
                    finish()
                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        mLoadingPopup.dismiss()
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
        showLoading()
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

    fun getHistoryFragment():HistoryLoginFragment? {
        return findFragment(HistoryLoginFragment::class.java)
    }

//    }
}