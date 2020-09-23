package com.ftofs.twant.login.ui

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.ftofs.twant.config.Config
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.constant.EBMessageType
import com.ftofs.twant.constant.LoginType
import com.ftofs.twant.constant.UmengAnalyticsActionName
import com.ftofs.twant.databinding.MessageLoginLayoutBinding
import com.ftofs.twant.entity.EBMessage
import com.ftofs.twant.fragment.BindMobileFragment
import com.ftofs.twant.fragment.H5GameFragment
import com.ftofs.twant.util.LogUtil
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.ftofs.twant.widget.HwLoadingPopup
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.base.callback.OnSelectedListener
import com.gzp.lib_common.constant.PopupType
import com.gzp.lib_common.utils.SLog
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.umeng.analytics.MobclickAgent
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class MessageFragment(val mobile: String, val sdkAvailable: Boolean = true, private val firstPage: Boolean = false) : BaseTwantFragmentMVVM<MessageLoginLayoutBinding, MessageLoginViewModel>(),OnSelectedListener {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.message_login_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    private var canSendSMS= false
    private var  mLoadingPopup: BasePopupView?=null
    fun showLoading(){
        if (mLoadingPopup == null) {
            mLoadingPopup= Util.createLoadingPopup(requireContext())
        }
        mLoadingPopup?.show()

    }
    private var promotionCodeVisible=false
    private var selectedMobileZoneIndex=0
    private val aViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun initData() {
        EBMessage.postMessage(EBMessageType.LOADING_POPUP_DISMISS,null)
        binding.title.performClick()
        binding.title.apply {
            setLeftImageResource(if (firstPage) R.drawable.ic_baseline_close_24 else R.drawable.icon_back)
            setLeftLayoutClickListener{onBackPressedSupport()}
//            setRightLayoutClickListener{}
        }
        binding.rlCaptchaContainer.btnCaptchaView?.apply {
            mListener={messageAction()}
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

    override fun onSupportVisible() {
        super.onSupportVisible()
        SLog.info("重新監視model")
//        initViewObservable()
    }

    override fun initViewObservable() {
        aViewModel.logUtilInfo.observe(this, { LogUtil.uploadAppLog("/loginconnect/facebook/login", it, "", "") })
        aViewModel.faceBookInfo.observe(this, {
            if (it.isBind == Constant.FALSE_INT) start(BindMobileFragment.newInstance(BindMobileFragment.BIND_TYPE_FACEBOOK, aViewModel.faceBookAccessToken?.token, aViewModel.faceBookAccessToken?.userId))
            else// 未綁定
            {
                User.onNewLoginSuccess(it.memberId
                        ?: 0, LoginType.FACEBOOK, it)

                ToastUtil.success(_mActivity, "Facebook登入成功")
                (activity as LoginActivity).onBackPressedSupport()
            }
        })
        aViewModel.weChatInfo.observe(this){
            SLog.info("檢測到了wechatInfo")
            if (it.isBind == Constant.FALSE_INT) {
                SLog.info("進入綁定頁")

                start(BindMobileFragment.newInstance(BindMobileFragment.BIND_TYPE_WEIXIN, aViewModel.weChatInfo.value?.accessToken, aViewModel.weChatInfo.value?.accessToken))}
            else// 未綁定
            {
                SLog.info("未進入綁定頁")
                User.onNewLoginSuccess(it.memberId
                        ?: 0, LoginType.WEIXIN, it)
                ToastUtil.success(_mActivity, "微信登入成功")
                (activity as LoginActivity).onBackPressedSupport()
            }
        }
        aViewModel.successLoginInfo.observe(this){
                com.gzp.lib_common.utils.ToastUtil.success(context, "登入成功")
                com.ftofs.twant.login.UserManager.saveUser(aViewModel.loginLiveData.value)
                User.onNewLoginSuccess(it.memberId!!, LoginType.MOBILE,it)
                hideSoftInput()

                SLog.info("登錄成功")
                com.ftofs.twant.util.Util.getMemberToken(_mActivity)
                (activity as LoginActivity).onBackPressedSupport()
        }
        aViewModel.mobileZoneList.observe(this, {
            mLoadingPopup?.dismiss()
            binding.etPhoneView.apply {
                it.forEach { a -> SLog.info(a.toString()) }
                mobileList = it
                setZoneIndex(0)
            }
            if (it.isNotEmpty()) canSendSMS = true
        })
        viewModel.authCodeInfo.observe(this){
            binding.tvSmsCodeHint.text= String.format(getString(R.string.text_find_password_info), binding.etPhoneView.getPhone(), it.authCodeValidTime)
            binding.rlCaptchaContainer.btnCaptchaView?.apply {
                mLoadingPopup?.dismiss()
                ToastUtil.success(context, "獲取驗證碼成功")
                if(it.authCodeResendTime!=null&&countTime.toInt()!=it.authCodeResendTime)countTime=it.authCodeResendTime!!.toLong() }
            if (it.isRegister == Constant.FALSE_INT) {
                binding.llRecommend.visibility=View.VISIBLE
            }
        }
        viewModel.msgError.observe(this){
            if(it.isNotEmpty())     ToastUtil.error(context,it) }
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when (it) {
                StateLiveData.StateEnum.Success -> {
//                    onBackPressedSupport()
                    mLoadingPopup?.dismiss()

                    KLog.e("数据获取成功--关闭loading")
                }
                StateLiveData.StateEnum.Error -> {
                    SLog.info("獲取數據失敗")
                    ToastUtil.error(context, viewModel.errorMessage)
                    mLoadingPopup?.dismiss()

                }
                else -> {
                    mLoadingPopup?.dismiss()
                    KLog.e("其他状态--关闭loading")
//                    loadingUtil?.hideLoading()
                }
            }
        })
    }

    private fun passwordAction() {

        start(PasswordLoginFragment(binding.etPhoneView.getEditMobile(), binding.etPhoneView.getIndex()?.let { selectedMobileZoneIndex }))
    }

    private fun loginAction() {//短信登錄
        if (binding.etPhoneView.isRight && binding.rlCaptchaContainer.isRight) {

            viewModel.getMessageLogin(binding.etPhoneView.getPhone(), binding.rlCaptchaContainer.getCaptcha()
                    ?: "", binding.etPromotionCode.text.toString())
        }
        binding.etPhoneView.showError()
        binding.rlCaptchaContainer.showError()

    }

    private fun messageAction():Boolean {
        binding.etPhoneView.apply {
            if (isRight) {
                viewModel.getSmsAuthCode(getPhone())
            }
            showError()
            return isRight
        }
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
