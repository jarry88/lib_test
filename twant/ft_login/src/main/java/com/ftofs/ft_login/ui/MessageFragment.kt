package com.ftofs.ft_login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ftofs.ft_login.R
import com.ftofs.ft_login.BR
import com.ftofs.ft_login.databinding.MessageLoginLayoutBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import org.koin.android.ext.android.bind

class MessageFragment(val mobile:String) : BaseTwantFragmentMVVM<MessageLoginLayoutBinding,MessageLoginViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.message_login_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.btnGetMessage.setOnClickListener { messageAction() }
        binding.btnLogin.setOnClickListener { loginAction() }
        binding.btnPasswordLogin.setOnClickListener{passwordAction()}
    }

    private fun passwordAction() {
        start(PasswordLoginFragment(binding.etNumber.text.toString()))
    }

    private fun loginAction() {
        viewModel.getMessageLogin(mobile,binding.etNumber.text.toString(),binding.etRecommendNumber.text.toString())
    }

    private fun messageAction() {
        viewModel.getSmsAuthCode(mobile)
    }
    override fun initViewObservable() {
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when(it){
                StateLiveData.StateEnum.Loading -> {
//                    loadingUtil?.showLoading("加载中..")


                    KLog.e("请求数据中--显示loading")
                }
                StateLiveData.StateEnum.Success -> {
                    onBackPressedSupport()
                    KLog.e("数据获取成功--关闭loading")
                }
                StateLiveData.StateEnum.Idle -> {


                    KLog.e("空闲状态--关闭loading")
//                    loadingUtil?.hideLoading()
                }
                StateLiveData.StateEnum.NoData -> {


                    KLog.e("空闲状态--关闭loading")
                }
                else -> {

                    KLog.e("其他状态--关闭loading")
//                    loadingUtil?.hideLoading()
                }
            }
        })
}
}
