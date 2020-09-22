package com.ftofs.twant.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cn.snailpad.easyjson.EasyJSONObject
import com.ftofs.ft_login.BR
import com.ftofs.lib_common_ui.createLoadingPopup
import com.ftofs.twant.R
import com.ftofs.twant.constant.LoginType
import com.ftofs.twant.databinding.PasswordLoginLayoutBinding
import com.ftofs.twant.util.User
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.ToastUtil

class PasswordLoginFragment(val mobile: String) : BaseTwantFragmentMVVM<PasswordLoginLayoutBinding, PasswordLoginViewModel>() {
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
            mLoading.show()
            aViewModel.passwordLogin(binding.etPhoneView.getPhone(), binding.etPassword.text.toString())
        }
        binding.btnPasswordFind.setOnClickListener { start(PassWordFindFragment(binding.etPhoneView.getPhone())) }
        binding.etPhoneView.setPhone(mobile)
        aViewModel.mobileZoneList.value?.let { if(it.isNotEmpty()) binding.etPhoneView.apply {
            it.forEach{a->SLog.info(a.toString())}
            mobileList=it
            setZoneIndex(0)
        }else aViewModel.getMobileAreaZoneList() }?:aViewModel.getMobileAreaZoneList()
    }

    override fun initViewObservable() {
        aViewModel.apply {
            mobileZoneList.observe(this@PasswordLoginFragment){
                binding.etPhoneView.apply {
                    mobileList=it
                    setZoneIndex(0)
                }
            }
            stateLiveData.stateEnumMutableLiveData.observe(this@PasswordLoginFragment){
                mLoading.dismiss()
            }
            successLoginInfo.observe(this@PasswordLoginFragment){
                ToastUtil.success(context, "登入成功")
                com.ftofs.twant.login.UserManager.saveUser(aViewModel.loginLiveData.value)
                User.onLoginSuccess(it.memberId!!, LoginType.MOBILE, EasyJSONObject.generate("datas",it))
                hideSoftInput()

                SLog.info("登錄成功")
                com.ftofs.twant.util.Util.getMemberToken(_mActivity)
                onBackPressedSupport()
            }
            mobileZoneList.observe(this@PasswordLoginFragment, {
                binding.etPhoneView.apply {
                    it.forEach{a->SLog.info(a.toString())}
                    mobileList=it
                    setZoneIndex(0)
                }
            })
        }
            //登陆成功才会保存账号信息

    }

    /**
     * 保存賬號密碼到db
     */
    fun savePasswordInfo(){
    }

}
