package com.ftofs.twant.login.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.snailpad.easyjson.EasyJSONObject
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.ftofs.lib_common_ui.entity.ListPopupItem
import com.ftofs.lib_common_ui.popup.ListPopup
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.TwantApplication.Companion.get
import com.ftofs.twant.activity.MainActivity
import com.ftofs.twant.config.Config
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.constant.LoginType
import com.ftofs.twant.constant.UmengAnalyticsActionName
import com.ftofs.twant.databinding.MessageLoginLayoutBinding
import com.ftofs.twant.fragment.BindMobileFragment
import com.ftofs.twant.fragment.H5GameFragment
import com.ftofs.twant.util.LogUtil
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.base.callback.OnSelectedListener
import com.gzp.lib_common.constant.PopupType
import com.gzp.lib_common.utils.SLog
import com.lxj.xpopup.XPopup
import com.umeng.analytics.MobclickAgent
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import java.util.*
import kotlin.math.roundToInt

class MessageFragment(val mobile: String, val sdkAvailable: Boolean = true, private val firstPage: Boolean = false) : BaseTwantFragmentMVVM<MessageLoginLayoutBinding, MessageLoginViewModel>(),OnSelectedListener {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.message_login_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    private var canSendSMS= false
    private val countDownTimer by lazy {object :CountDownTimer(60 * 1000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            binding.btnRefreshCaptcha.text= (millisUntilFinished / 1000).toDouble().roundToInt().toString().plus("s后重試")
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onFinish() {
            binding.btnRefreshCaptcha.apply {
                background=resources.getDrawable(com.ftofs.lib_common_ui.R.drawable.bg_refresh_captcha, null)
                text="重新獲取"
                canSendSMS=true
            }

        }
    } }
    private var promotionCodeVisible=false
    private var selectedMobileZoneIndex=0
    private val aViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initData() {
        binding.title.apply {
            setLeftImageResource(if (firstPage) R.drawable.ic_baseline_close_24 else R.drawable.icon_back)
            setLeftLayoutClickListener{onBackPressedSupport()}
//            setRightLayoutClickListener{}
        }
        binding.btnRefreshCaptcha.apply {
            setOnClickListener {
                if(canSendSMS) {
                    this.background=resources.getDrawable(com.ftofs.lib_common_ui.R.drawable.bg_grey_captcha)
                    canSendSMS =false
                    messageAction()
                    countDownTimer.start()
                } }
        }
        binding.btnMessageLogin.setOnClickListener { loginAction() }
        binding.btnPasswordLogin.setOnClickListener{passwordAction()}
        binding.btnSwitchPromotionCodeVisibility.setOnClickListener {
            promotionCodeVisible = !promotionCodeVisible
            binding.icPromotionCodeVisibility.setImageResource(if (promotionCodeVisible) R.drawable.ic_baseline_arrow_drop_up_24 else R.drawable.ic_baseline_arrow_drop_down_24)
            binding.rlPromotionCodeContainer.visibility=(if (promotionCodeVisible) View.VISIBLE else View.GONE)
        }
        binding.etPhoneView.apply {
            setZoneSelect{
                mobileList.takeUnless { it.isNullOrEmpty() }?.let {
                    hideSoftInput()
                    XPopup.Builder(_mActivity) // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(ListPopup(_mActivity, resources.getString(R.string.mobile_zone_text),
                                    PopupType.MOBILE_ZONE,
                                    it.map { m -> ListPopupItem(m.areaId, m.areaName, null) }, selectedMobileZoneIndex, this@MessageFragment))
                            .show()
                }
            }
        }
        binding.btnViewTos.apply {
            setOnClickListener { start(H5GameFragment.newInstance(H5GameFragment.ARTICLE_ID_TERMS_OF_SERVICE, text.toString())) }
        }
        binding.btnViewPrivateTerms.apply {
            setOnClickListener { start(H5GameFragment.newInstance(H5GameFragment.ARTICLE_ID_TERMS_OF_PRIVATE, text.toString())) }
        }
        binding.thirdLoginContainer.apply {
            setBtnWeChat{
                if (!get().wxApi!!.isWXAppInstalled) { // 未安裝微信
                    ToastUtil.error(_mActivity, getString(R.string.weixin_not_installed_hint))
                } else {
                    if (Config.PROD) {
                        MobclickAgent.onEvent(get(), UmengAnalyticsActionName.WECHAT_LOGIN)
                    }
                    (_mActivity as LoginActivity).doWeixinLogin(Constant.WEIXIN_AUTH_USAGE_LOGIN)
                }
            }
            setFaceBook{
                if (Config.PROD) {
                    MobclickAgent.onEvent(get(), UmengAnalyticsActionName.FACEBOOK_LOGIN)
                }
                val permissions: MutableList<String> = ArrayList()
                permissions.add("email")
                LoginManager.getInstance().logInWithReadPermissions(this@MessageFragment, permissions)
            }


        }
        // 回调
        LoginManager.getInstance().registerCallback((_mActivity as LoginActivity).callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                SLog.info("onSuccess")
                val accessToken = loginResult.accessToken
                aViewModel.faceBookAccessToken = accessToken
                val token = accessToken.token
                val userId = accessToken.userId
                SLog.info("token[%s], userId[%s]", token, userId)
                aViewModel.doFacebookLogin(token, userId)
            }

            override fun onCancel() {
                // App code
                SLog.info("onCancel")
            }

            override fun onError(exception: FacebookException) {
                // App code
                ToastUtil.error(_mActivity, "登錄失敗")
                SLog.info("onError, exception[%s]", exception.toString())
            }
        })
        aViewModel?.apply {this.getMobileAreaZoneList() }
    }


    override fun initViewObservable() {
        aViewModel.logUtilInfo.observe(this, { LogUtil.uploadAppLog("/loginconnect/facebook/login", it, "", "") })
        aViewModel.faceBookInfo.observe(this, {
            if (it.isBind == Constant.FALSE_INT) start(BindMobileFragment.newInstance(BindMobileFragment.BIND_TYPE_FACEBOOK, aViewModel.faceBookAccessToken?.token, aViewModel.faceBookAccessToken?.userId))
            else// 未綁定
            {
                User.onLoginSuccess(it.memberId?:0, LoginType.FACEBOOK, EasyJSONObject.generate("datas", it))
                hideSoftInputPop()
                ToastUtil.success(_mActivity, "Facebook登入成功")
            }
        })
        aViewModel.mobileZoneList.observe(this, {
            binding.etPhoneView.apply {
                it.forEach{a->SLog.info(a.toString())}
                mobileList=it
                setZoneIndex(0)
            }
            if (it.isNotEmpty()) canSendSMS = true
        })
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when (it) {
                StateLiveData.StateEnum.Success -> {
//                    onBackPressedSupport()
                    KLog.e("数据获取成功--关闭loading")
                }
                else -> {

                    KLog.e("其他状态--关闭loading")
//                    loadingUtil?.hideLoading()
                }
            }
        })
    }

    private fun passwordAction() {
        start(PasswordLoginFragment(binding.etPhoneView.getPhone()))
    }

    private fun loginAction() {
        viewModel.getMessageLogin(mobile, binding.etPhoneView.getPhone(), binding.etPromotionCode.text.toString())
    }

    private fun messageAction() {
        viewModel.getSmsAuthCode(mobile)
    }

    override fun onBackPressedSupport(): Boolean {
        if (parentFragmentManager.backStackEntryCount <= 1) {
            (activity as LoginActivity).onBackPressedSupport()
        }
        else hideSoftInputPop()
        return true
    }

    override fun onSelected(type: PopupType?, id: Int, extra: Any?) {
        binding.etPhoneView.mobileList.takeIf { it.isNotEmpty() }?.let {
            selectedMobileZoneIndex=id
            binding.etPhoneView.setZoneIndex(id)
        }
    }
}
