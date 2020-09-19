package com.ftofs.ft_login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ftofs.ft_login.BR
import com.ftofs.ft_login.R
import com.ftofs.ft_login.databinding.LayoutHistoryLoginBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.base.callback.SimpleCallBack
import com.gzp.lib_common.model.User
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryLoginFragment(private val historyUser: User):BaseTwantFragmentMVVM<LayoutHistoryLoginBinding,HistoryLoginViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.layout_history_login
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.tvOneStep.setOnClickListener { parentFragmentManager.beginTransaction().addToBackStack("OneStepLoginFragment").replace(R.id.container,OneStepLoginFragment()).commit() }
        binding.tvTwoStep.setOnClickListener { start(OneStepLoginFragment()) }
        binding.btnLogin.setOnClickListener {loginAction() }
    }

    private fun loginAction() {
//        (activity as LoginActivity).viewModel<LoginViewModel>().value
        viewModel.login(historyUser)
    }

    override fun onBackPressedSupport(): Boolean {
        if (parentFragmentManager.backStackEntryCount <= 1) {
//            call.onCall()
            (activity as LoginActivity).onBackPressedSupport()
        }else{
            hideSoftInputPop()
        }
        return true
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