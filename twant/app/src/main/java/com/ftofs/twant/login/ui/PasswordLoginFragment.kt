package com.ftofs.twant.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cn.snailpad.easyjson.EasyJSONObject
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.Login
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.ftofs.ft_login.BR
import com.ftofs.lib_common_ui.createLoadingPopup
import com.ftofs.twant.R
import com.ftofs.twant.TwantApplication
import com.ftofs.twant.config.Config
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.constant.LoginType
import com.ftofs.twant.constant.UmengAnalyticsActionName
import com.ftofs.twant.databinding.PasswordLoginLayoutBinding
import com.ftofs.twant.fragment.BindMobileFragment
import com.ftofs.twant.util.LogUtil
import com.ftofs.twant.util.User
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.base.callback.OnSelectedListener
import com.gzp.lib_common.constant.PopupType
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.ToastUtil
import com.umeng.analytics.MobclickAgent
import java.util.ArrayList

class PasswordLoginFragment(val mobile: String,var selectedMobileZoneIndex:Int?) : BaseTwantFragmentMVVM<PasswordLoginLayoutBinding, PasswordLoginViewModel>() ,OnSelectedListener{
    private val aViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    private val mLoading  by lazy { createLoadingPopup(requireContext()) }

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.password_login_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.title.apply {
            setLeftImageResource(R.drawable.icon_back)
            setLeftLayoutClickListener{onBackPressedSupport()}
        }
        binding.btnPasswordLogin.setOnClickListener {
            SLog.info("执行登陆")
            if(binding.etPhoneView.isRight){
                mLoading.show()
                aViewModel.passwordLogin(binding.etPhoneView.getPhone(), binding.etPassword.text.toString())
            }
            binding.etPhoneView.showError()
        }
        binding.btnPasswordFind.setOnClickListener {
            if(binding.etPhoneView.isRight){
            start(PassWordFindFragment(binding.etPhoneView.getPhone()))
            binding.etPhoneView.showError()
                }
        }
        binding.etPhoneView.setPhone(mobile)
        binding.thirdLoginContainer.apply {
            setBtnWeChat{
                if (!TwantApplication.get().wxApi!!.isWXAppInstalled) { // 未安裝微信
                    com.ftofs.twant.util.ToastUtil.error(_mActivity, getString(R.string.weixin_not_installed_hint))
                } else {
                    if (Config.PROD) {
                        MobclickAgent.onEvent(TwantApplication.get(), UmengAnalyticsActionName.WECHAT_LOGIN)
                    }
                    (_mActivity as LoginActivity).doWeixinLogin(Constant.WEIXIN_AUTH_USAGE_LOGIN)
                }
            }
            setFaceBook{
                if (Config.PROD) {
                    MobclickAgent.onEvent(TwantApplication.get(), UmengAnalyticsActionName.FACEBOOK_LOGIN)
                }
                val permissions: MutableList<String> = ArrayList()
                permissions.add("email")
                LoginManager.getInstance().logInWithReadPermissions(this@PasswordLoginFragment, permissions)
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
                com.ftofs.twant.util.ToastUtil.error(_mActivity, "登錄失敗")
                SLog.info("onError, exception[%s]", exception.toString())
            }
        })
        aViewModel.mobileZoneList.value?.let { if(it.isNotEmpty()) binding.etPhoneView.apply {
            it.forEach{a->SLog.info(a.toString())}
            mobileList=it
            setZoneIndex(selectedMobileZoneIndex?:0)
        }else aViewModel.getMobileAreaZoneList() }?:aViewModel.getMobileAreaZoneList()



    }

    override fun initViewObservable() {
        aViewModel.logUtilInfo.observe(this, { LogUtil.uploadAppLog("/loginconnect/facebook/login", it, "", "") })
        aViewModel.faceBookInfo.observe(this, {
            if (it.isBind == Constant.FALSE_INT) start(BindMobileFragment.newInstance(BindMobileFragment.BIND_TYPE_FACEBOOK, aViewModel.faceBookAccessToken?.token, aViewModel.faceBookAccessToken?.userId))
            else// 未綁定
            {
                User.onLoginSuccess(it.memberId
                        ?: 0, LoginType.FACEBOOK, EasyJSONObject.generate("datas", it))
                hideSoftInputPop()
                com.ftofs.twant.util.ToastUtil.success(_mActivity, "Facebook登入成功")
            }
        })
        aViewModel.weChatInfo.observe(this){
            if (it.isBind == Constant.FALSE_INT) {
                SLog.info("進入綁定頁")

                start(BindMobileFragment.newInstance(BindMobileFragment.BIND_TYPE_WEIXIN, aViewModel.weChatInfo.value?.accessToken, aViewModel.weChatInfo.value?.accessToken))}
            else// 未綁定
            {
                SLog.info("未進入綁定頁")
                User.onNewLoginSuccess(it.memberId
                        ?: 0, LoginType.WEIXIN, it)
                com.ftofs.twant.util.ToastUtil.success(_mActivity, "微信登入成功")
                (activity as LoginActivity).onBackPressedSupport()
            }
        }
        aViewModel.apply {
            mobileZoneList.observe(this@PasswordLoginFragment){
                binding.etPhoneView.apply {
                    mobileList=it
                    setZoneIndex(selectedMobileZoneIndex?:0)
                }
            }
            stateLiveData.stateEnumMutableLiveData.observe(this@PasswordLoginFragment){
                mLoading.dismiss()
            }
            successLoginInfo.observe(this@PasswordLoginFragment){
                ToastUtil.success(context, "登入成功")
                com.ftofs.twant.login.UserManager.saveUser(aViewModel.loginLiveData.value)
                User.onNewLoginSuccess(it.memberId!!, LoginType.MOBILE,it)
                SLog.info("登錄成功")
                com.ftofs.twant.util.Util.getMemberToken(_mActivity)
                hideSoftInputPop()
                (activity as LoginActivity).onBackPressedSupport()
            }
            mobileZoneList.observe(this@PasswordLoginFragment, {
                binding.etPhoneView.apply {
                    it.forEach{a->SLog.info(a.toString())}
                    mobileList=it
                    setZoneIndex(selectedMobileZoneIndex?:0)
                }
            })
            msgError.observe(this@PasswordLoginFragment){if(it.isNotEmpty())ToastUtil.error(context,it)}
        }
            //登陆成功才会保存账号信息

    }
    override fun onSelected(type: PopupType?, id: Int, extra: Any?) {
        binding.etPhoneView.mobileList.takeIf { it.isNotEmpty() }?.let {
            selectedMobileZoneIndex=id
            binding.etPhoneView.setZoneIndex(id)
        }
    }

    /**
     * 保存賬號密碼到db
     */
    fun savePasswordInfo(){
    }

}
