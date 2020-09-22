package com.ftofs.ft_login

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.ftofs.ft_login.ui.LoginActivity
import com.ftofs.ft_login.utils.ExecutorManager
import com.ftofs.lib_common_ui.createLoadingPopup
import com.ftofs.lib_net.BaseRepository
import com.gzp.lib_common.constant.Result
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.ToastUtil
import com.gzp.lib_common.utils.Util.dip2px
import com.gzp.lib_common.utils.Util.findActivity
import com.gzp.lib_common.utils.getBaseApplication
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.util.XPopupUtils
import com.mobile.auth.gatewayauth.*
import com.mobile.auth.gatewayauth.model.TokenRet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


object OneStepLogin:CoroutineScope{
    private var loadingPopup: BasePopupView?=null
    private val TAG: String = this::class.java.simpleName
    private var sdkAvailable = true
    private val repository by lazy { object : BaseRepository(){} }
    private lateinit var mContext:Context
    fun login(aliYunToken: String) {
        launch {
            try {
                val re=repository.run{simpleGet(api.getLoginOne(aliYunToken, "android"))}
                when (re) {
                    is Result.Success -> {
                        SLog.info("数据加载成功${re.datas.nickName}")
                        ToastUtil.success(mContext,"登入成功")
                        getResultWithToken(aliYunToken)
                    }
                    else -> {
                        goLoginActivity()
                        SLog.info("数据加载失败%s ", if (re is Result.DataError) re.datas.toString() else " ")

//                    errorMessage=re

                    }
                }
            } catch (e: Throwable) {
                SLog.info(e.toString())
            }finally {

            }
        }
    }

    private val mPhoneNumberAuthHelper by lazy { PhoneNumberAuthHelper.getInstance(mContext, mCheckListener)
    }
    private val mCheckListener by lazy {
        object : TokenResultListener {
            override fun onTokenSuccess(p0: String?) {
                try {
                    Log.i(TAG, "checkEnvAvailable：$p0")
                    SLog.info("checkEnvAvailable：$p0")

                    val pTokenRet = JSON.parseObject(p0, TokenRet::class.java)
                    if (ResultCode.CODE_ERROR_ENV_CHECK_SUCCESS == pTokenRet.code) accelerateLoginPage(5000)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onTokenFailed(p0: String?) {
                loadingPopup?.dismiss()

                sdkAvailable = false
                SLog.info("checkEnvAvailable：$p0")
                Log.e(TAG, "checkEnvAvailable：$p0")

            }
        }
    }
    /**
     * 拿到token ，成功后跳出阿里登錄頁
     */
    private fun getResultWithToken(token: String?) {
        token?.let {
            ExecutorManager.run{
                findActivity(mContext)?.runOnUiThread{
//                    mTvResult.setText("登陆成功：$result")
//                    SLog.info(MockRequest.getPhoneNumber(it))
//                    ToastUtil.success(context, MockRequest.getPhoneNumber(it))
                    mPhoneNumberAuthHelper!!.quitLoginPage()
                }
            }
        }
    }

    fun start(context: Context) {
        mContext=context
        createLoadingPopup(BaseContext.instance.getContext()).show()
//        getBaseApplication().mHandler?.post{"a"}
//        ToastUtil.success(context,"顯示加載")
        SLog.info("加載中")

        sdkInit()

        if (sdkAvailable) {
//                    loadRootFragment(R.id.container, mPhoneNumberAuthHelper.
            configLoginTokenPort()
            getLoginToken(context, 5000)
        } else {
            loadingPopup?.dismiss()
            //如果环境检查失败 使用其他登录方式
//                    val pIntent = Intent(this@LoginActivity, MessageActivity::class.java)
//                    startActivityForResult(pIntent, 1002)
//                    mUIConfig.release()
//            loadRootFragment(R.id.container ,findFragment(MessageFragment::class.java)
//                    ?: MessageFragment("11"))
        }
    }
    private fun sdkInit() {
        mPhoneNumberAuthHelper?.checkEnvAvailable(PhoneNumberAuthHelper.SERVICE_TYPE_LOGIN)
    }
    /**
     * 在不是一进app就需要登录的场景 建议调用此接口 加速拉起一键登录页面
     * 等到用户点击登录的时候 授权页可以秒拉
     * 预取号的成功与否不影响一键登录功能，所以不需要等待预取号的返回。
     * @param timeout
     */
    fun accelerateLoginPage(timeout: Int) {
        mPhoneNumberAuthHelper!!.accelerateLoginPage(timeout, object : PreLoginResultListener {
            override fun onTokenSuccess(s: String) {
                Log.e(TAG, "预取号成功: $s")
            }

            override fun onTokenFailed(s: String, s1: String) {
                Log.e(TAG, "预取号失败：, $s1")
            }
        })
    }
    private fun actionOneKeyLogin(token: String) {
        login(token)

    }



    /**
     * 配置竖屏样式
     */
    private fun configLoginTokenPort() {
        mPhoneNumberAuthHelper!!.addAuthRegistViewConfig("switch_acc_tv", AuthRegisterViewConfig.Builder()
                .setView(initDynamicView())
                .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
                .setCustomInterface { mPhoneNumberAuthHelper!!.quitLoginPage() }.build())
        var authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        if (Build.VERSION.SDK_INT == 26) {
            authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND
        }
        mPhoneNumberAuthHelper!!.setAuthUIConfig(AuthUIConfig.Builder()
                .setAppPrivacyOne("《自定义隐私协议》", "https://www.baidu.com")
                .setAppPrivacyColor(Color.GRAY, Color.parseColor("#002E00"))
                .setPrivacyState(false)
                .setCheckboxHidden(true)
                .setStatusBarColor(Color.TRANSPARENT)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                .setLightColor(true)
                .setAuthPageActIn("in_activity", "out_activity")
                .setAuthPageActOut("in_activity", "out_activity")
                .setVendorPrivacyPrefix("《")
                .setVendorPrivacySuffix("》")
                .setLogoImgPath("mytel_app_launcher")
                .setScreenOrientation(authPageOrientation)
                .create())
    }
    private fun initDynamicView(): View? {
        val switchTV = TextView(mContext)
        val mLayoutParams2 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, dip2px(mContext, 50f))
        mLayoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        mLayoutParams2.setMargins(0, dip2px(mContext, 450f), 0, 0)
        switchTV.text = "-----  自定义view  -----"
        switchTV.setTextColor(-0x666667)
        switchTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.0f)
        switchTV.layoutParams = mLayoutParams2
        return switchTV
    }
    private val mTokenResultListener by lazy {
        object : TokenResultListener {
            override fun onTokenSuccess(s: String) {
                SLog.info("獲取成功")
                var tokenRet: TokenRet? = null
                try {
                    loadingPopup?.dismiss()
                    tokenRet = JSON.parseObject(s, TokenRet::class.java)
                    if (ResultCode.CODE_START_AUTHPAGE_SUCCESS == tokenRet.code) {
                        Log.i("TAG", "唤起授权页成功：$s")
                    }
                    if (ResultCode.CODE_GET_TOKEN_SUCCESS == tokenRet.code) {
                        Log.i("TAG", "获取token成功：$s")
                        actionOneKeyLogin(tokenRet.token)
//                    mUIConfig.release()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onTokenFailed(s: String) {
                //其他方式進入這裏
                Log.e("OneKeyLoginActivity.TAG", "获取token失败：$s")
                var tokenRet: TokenRet? = null
                mPhoneNumberAuthHelper?.quitLoginPage()
                try {
                    tokenRet = JSON.parseObject(s, TokenRet::class.java)
                    if (ResultCode.CODE_ERROR_USER_CANCEL == tokenRet?.code) {
                        //模拟的是必须登录 否则直接退出app的场景
                        SLog.info("模拟的是必须登录 否则直接退出app的场景")
//                        finish()
                    } else {
                        SLog.info("token 獲取失敗，前往登錄頁")

//                        ToastUtil.error(this@LoginActivity, "一键登录失败切换到其他登录方式")
//                        start(MessageFragment(""))
                        goLoginActivity()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun goLoginActivity() {
        mContext.startActivity(Intent(mContext, LoginActivity::class.java))
    }


    /**
     * 拉起授权页
     * @param timeout 超时时间
     */
    fun getLoginToken(context: Context, timeout: Int) {
//            mUIConfig.configAuthPage()
        mPhoneNumberAuthHelper!!.setAuthListener(mTokenResultListener)
        mPhoneNumberAuthHelper!!.getLoginToken(context, timeout)
//            showLoadingDialog("正在唤起授权页")
    }

    override val coroutineContext: CoroutineContext
        get() = job
    private val job= Job()
//    fun initViewObservable() {
//        mViewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
//            when(it){
//                StateLiveData.StateEnum.Success -> {
//                    onBackPressedSupport()
//                    KLog.e("数据获取成功--关闭loading")
//                }
//                else -> {
//
//                    KLog.e("其他状态--关闭loading")
////                    loadingUtil?.hideLoading()
//                }
//            }
//        })
//    }
}