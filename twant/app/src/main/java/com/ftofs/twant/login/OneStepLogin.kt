package com.ftofs.twant.login

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import cn.snailpad.easyjson.EasyJSONException
import cn.snailpad.easyjson.EasyJSONObject
import com.alibaba.fastjson.JSON
import com.ftofs.lib_net.BaseRepository
import com.ftofs.twant.R
import com.ftofs.twant.api.Api
import com.ftofs.twant.api.UICallback
import com.ftofs.twant.constant.EBMessageType
import com.ftofs.twant.constant.LoginType
import com.ftofs.twant.entity.EBMessage
import com.ftofs.twant.fragment.H5GameFragment.ARTICLE_ID_TERMS_OF_PRIVATE
import com.ftofs.twant.fragment.H5GameFragment.ARTICLE_ID_TERMS_OF_SERVICE
import com.ftofs.twant.login.ui.LoginActivity
import com.ftofs.twant.login.ui.MessageFragment
import com.ftofs.twant.login.utils.ExecutorManager
import com.ftofs.twant.tangram.SloganView
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.gzp.lib_common.constant.Result
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.ToastUtil
import com.gzp.lib_common.utils.Util.dip2px
import com.gzp.lib_common.utils.Util.findActivity
import com.lxj.xpopup.core.BasePopupView
import com.mobile.auth.gatewayauth.*
import com.mobile.auth.gatewayauth.model.TokenRet
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate
import kotlinx.android.synthetic.main.password_find_layout.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import java.io.IOException


object OneStepLogin{
    private var fromHistory: Boolean=false
    private var lastClickTime=0.toLong()
    private var loadingPopup: BasePopupView?=null
    private val TAG: String = this::class.java.simpleName
    private var sdkAvailable = true
    private val repository by lazy { object : BaseRepository(){} }
    private lateinit var mContext:Context
    fun login(aliYunToken: String) {

        SLog.info(User.getToken().plus(aliYunToken))
        Api.postUI("/v2/mobile/loginOne", EasyJSONObject.generate("aliYunToken", aliYunToken, "clientType", "android"), object : UICallback() {
            override fun onFailure(call: Call, e: IOException) {
                ToastUtil.showNetworkError(mContext, e)
                mPhoneNumberAuthHelper.hideLoginLoading()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, responseStr: String) {
                SLog.info("responeStr[%s]", responseStr)
                mPhoneNumberAuthHelper.hideLoginLoading()

                val responseObj = EasyJSONObject.parse<EasyJSONObject>(responseStr)
                if (ToastUtil.checkError(mContext, responseObj)) {
                    return
                }
                try {


                    findActivity(mContext)?.apply {
                        if (this is LoginActivity)  finish()
                        else if(fromHistory)finish()
                        SLog.info( "是不是歷史登錄頁 %s",this is LoginActivity)
                    }
                    EBMessage.postMessage(EBMessageType.LOGIN_SUCCESS_TOAST, null)
                    //不是用賬號密碼登錄 的都消除記錄
                    UserManager.removeUser()
                    getResultWithToken(aliYunToken)

                    User.onLoginSuccess(responseObj.getInt("datas.memberId"), LoginType.MOBILE, responseObj)
                } catch (e: EasyJSONException) {
                    SLog.info("Error!message[%s], trace[%s]", e.message, Log.getStackTraceString(e))
                    mPhoneNumberAuthHelper.hideLoginLoading()
                    Thread {
                        Looper.prepare()
                        Toast.makeText(mContext, "一鍵登入失敗", Toast.LENGTH_SHORT).show()
                        Looper.loop()
                    }.start()
//                    goLoginActivity()
                }
            }

        })
    }

    private val mPhoneNumberAuthHelper by lazy { PhoneNumberAuthHelper.getInstance(mContext, mCheckListener) }
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

    fun start(context: Context,fromHistory:Boolean=false) {
        mContext =context
        SLog.info("加載中")
        this.fromHistory=fromHistory
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
//        login2(token)

    }

    fun login2(aliYunToken: String) = runBlocking{
            launch {
                try {
                    SLog.info("aliYunToken  $aliYunToken")
                    when (val re = repository.run { simpleGet(api.getLoginOne(aliYunToken, "android")) }) {
                        is Result.Success -> {
                            SLog.info("数据加载成功${re.datas.nickName}")
                            getResultWithToken(aliYunToken)
                            findActivity(mContext)?.apply {
                                if (this is LoginActivity)  finish()
                                SLog.info( "是不是歷史登錄頁 %s",this is LoginActivity)
                            }
                            EBMessage.postMessage(EBMessageType.LOGIN_SUCCESS_TOAST, null)

//                            Thread {
//                                Looper.prepare()
//                                Toast.makeText(mContext, "登入成功", Toast.LENGTH_SHORT).show()
//                                Looper.loop()
//                            }.start()
                            User.onNewLoginSuccess(re.datas.memberId!!, LoginType.MOBILE, re.datas)

                            SLog.info("登錄成功")
                        }
                        else -> {
                            goLoginActivity()
                            SLog.info("数据加载失败%s ", if (re is Result.DataError) re.datas.toString() else " ")
                        }
                    }
                } catch (e: Throwable) {
                    SLog.info(e.toString())

                    goLoginActivity()
                    mPhoneNumberAuthHelper?.quitLoginPage()
                } finally {

                }
            }
    }

    /**
     * 配置竖屏样式
     */
    private fun configLoginTokenPort() {
        mPhoneNumberAuthHelper!!.addAuthRegistViewConfig("login_info", AuthRegisterViewConfig.Builder()
                .setView(initDynamicView())
                .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
                .setCustomInterface { mPhoneNumberAuthHelper!!.quitLoginPage() }.build())
//        mPhoneNumberAuthHelper!!.addAuthRegisterXmlConfig(AuthRegisterXmlConfig.Builder().setLayout(
//                R.layout.custom_one_stet_layout,object :AbstractPnsViewDelegate(){
//            override fun onViewCreated(p0: View?) {
//                findViewById(R.id.tv_test).setOnClickListener { ToastUtil.success(context,"點擊") }
//            }
//
//        }
//        ).build())
        var authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        if (Build.VERSION.SDK_INT == 26) {
            authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND
        }
        val urlService =BaseRepository().getBase().toString().plus("/article/info_h5/${ARTICLE_ID_TERMS_OF_SERVICE}")// 服務協議
        val urlPrivate =BaseRepository().getBase().toString().plus("/article/info_h5/${ARTICLE_ID_TERMS_OF_PRIVATE}")// 隱私協議
        mPhoneNumberAuthHelper!!.setAuthUIConfig(AuthUIConfig.Builder()
                .setAppPrivacyOne("《服務協議》", urlService)
                .setAppPrivacyTwo("《私隱條款》",urlPrivate)
                .setAppPrivacyColor(mContext.getColor(R.color.tw_grey_CBCB),mContext.getColor(R.color.tw_blue))
                .setNavTextSize(18)
                .setSwitchAccTextSize(16)
                .setSwitchAccTextColor(mContext.getColor(R.color.tw_blue))
                .setSwitchAccText("其他手機號碼登入")
                .setNumberSize(26)
                .setNumFieldOffsetY(104)
                .setLoadingImgPath("img_loading")
                .setSwitchOffsetY(246)
                .setLogBtnOffsetY(182)
                .setPrivacyState(false)
                .setCheckboxHidden(true)
                .setSloganHidden(true)
                .setStatusBarColor(Color.WHITE)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LOW_PROFILE)
                .setNavText("登入後享受更多服務")
                .setNavTextColor(mContext.getColor(R.color.black))
                .setNavColor(mContext.getColor(R.color.white))
                .setNavReturnImgPath(if(fromHistory)"icon_back" else "ic_baseline_close_24")
                .setLightColor(true)
                .setLogBtnBackgroundPath("blue_button")
                .setLogBtnText("本機號碼一鍵登入")
                .setLogBtnWidth(271)
                .setLogBtnHeight(44)
                .setLogBtnTextSize(16)
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
        val mLayoutParams2 = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        mLayoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
        mLayoutParams2.setMargins(0, 0, 0, 0)
        switchTV.setPadding(dip2px(mContext, 13f),dip2px(mContext, 13f),dip2px(mContext, 7f),dip2px(mContext, 13f))
        switchTV.text = mContext.getString(R.string.tv_login_info)
        switchTV.setTextColor(mContext.getColor(R.color.tw_blue))
        switchTV.setBackgroundColor(mContext.getColor(R.color.bg_blue))
        switchTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.0f)
        switchTV.layoutParams = mLayoutParams2
        switchTV.visibility=View.VISIBLE
        return switchTV
    }
    private val mTokenResultListener by lazy {
        object : TokenResultListener {
            override fun onTokenSuccess(s: String) {
                SLog.info("獲取成功")
                EBMessage.postMessage(EBMessageType.LOADING_POPUP_DISMISS, null)

                var tokenRet: TokenRet? = null
                try {
                    loadingPopup?.dismiss()
                    tokenRet = JSON.parseObject(s, TokenRet::class.java)
                    if (ResultCode.CODE_START_AUTHPAGE_SUCCESS == tokenRet.code) {
                        Log.i("TAG", "唤起授权页成功：$s")
                    }
                    if (ResultCode.CODE_GET_TOKEN_SUCCESS == tokenRet.code) {
                        Log.i("TAG", "获取token成功：$s")
                        SLog.info("拿到阿里token")
                        actionOneKeyLogin(tokenRet.token)
//                    mUIConfig.release()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onTokenFailed(s: String) {
                //其他方式進入這裏
                EBMessage.postMessage(EBMessageType.LOADING_POPUP_DISMISS, null)

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
                        tokenRet?.apply {
//                            Thread {
//                                Looper.prepare()
//                                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
//                                Looper.loop()
//                            }.start()
                            SLog.info(msg)
                        }
                        SLog.info("token 獲取失敗，前往登錄頁")
//                        ToastUtil.error(this@LoginActivity, "一键登录失败切换到其他登录方式")
//                        start(MessageFragment(""))
                        if(isFastClick()) goLoginActivity()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    fun isFastClick(): Boolean {
        var flag = false
        val curClickTime = System.currentTimeMillis()
        if (curClickTime -lastClickTime >= 810) {
            flag = true
        }
        lastClickTime = curClickTime
        return flag
    }
    private fun goLoginActivity() {
        if (UserManager.getUser() != null && findActivity(mContext) is LoginActivity) {
            (findActivity(mContext) as LoginActivity).getHistoryFragment()?.start(MessageFragment("", sdkAvailable))
                    ?:mContext.startActivity(Intent(mContext, LoginActivity::class.java)).apply {findActivity(mContext)?.finish() }

        } else {
            mContext.startActivity(Intent(mContext, LoginActivity::class.java))
        }
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