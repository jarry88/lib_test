package com.ftofs.twant.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.PasswordFindLayoutBinding
import com.ftofs.twant.util.ToastUtil
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog


class PassWordFindFragment(val mobile:String) : BaseTwantFragmentMVVM<PasswordFindLayoutBinding, MessageLoginViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.password_find_layout
    }
    override fun initVariableId(): Int {
        return BR.viewModel
    }
    val aViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    override fun initData() {
        binding.title.apply {
            setLeftLayoutClickListener{onBackPressedSupport()}
//            setRightLayoutClickListener{}
        }
        binding.findPasswordInfo.text= String.format(getString(R.string.text_find_password_info),mobile,10).replaceFirst(",","-")+"密碼為8～20位的，支持數字、字母、符號。"
        binding.rlCaptchaContainer.btnCaptchaView?.apply {
            mListener={pwdMessageAction()}
            postDelayed({performClick()},200)
        }
        binding.btnFindPassword.setOnClickListener {
            if (binding.rlCaptchaContainer.isRight) {
                viewModel.getSetPwd(mapOf("mobile" to mobile,"authCode" to binding.rlCaptchaContainer.getCaptcha(),"memberPwd" to binding.etPassword.text?.toString()))
            }
            binding.rlCaptchaContainer.showError()
        }
    }

    private fun pwdMessageAction(): Boolean {
        viewModel.getFpdSmsAuthCode(mobile)
        return true
    }

    override fun initViewObservable() {
        viewModel.msgError.observe(this){               if(it.isNotEmpty())     ToastUtil.error(context,it) }
        viewModel.commonInfo.observe(this){
            if (it.success.isEmpty()) {
                if (it.error.isNotEmpty()) ToastUtil.error(context, it.error)
            } else {
                ToastUtil.success(context,it.success)
                onBackPressedSupport()
            }
        }
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            when (it) {
                StateLiveData.StateEnum.Success -> {
//                    onBackPressedSupport()

                    KLog.e("数据获取成功--关闭loading")
                }
                StateLiveData.StateEnum.Error -> {
                    SLog.info("獲取數據失敗")
                    ToastUtil.error(context, viewModel.errorMessage)

                }
                else -> {
                    KLog.e("其他状态--关闭loading")
//                    loadingUtil?.hideLoading()
                }
            }
        }
    }
}
