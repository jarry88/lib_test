package com.ftofs.twant.login.ui

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.constant.EBMessageType
import com.ftofs.twant.constant.LoginType
import com.ftofs.twant.databinding.LayoutHistoryLoginBinding
import com.ftofs.twant.entity.EBMessage
import com.ftofs.twant.fragment.H5GameFragment
import com.ftofs.twant.login.OneStepLogin
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.model.User
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.ToastUtil
import com.lxj.xpopup.core.BasePopupView
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HistoryLoginFragment(private val historyUser: User):BaseTwantFragmentMVVM<LayoutHistoryLoginBinding, HistoryLoginViewModel>(){
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.layout_history_login
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    lateinit var mLoadingPopup: BasePopupView
    fun showLoading(){
        mLoadingPopup.show()

    }
    private val aViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEBMessage(message: EBMessage) {
        if (message.messageType == EBMessageType.LOADING_POPUP_DISMISS ) {
            SLog.info("接受到關閉的消息")

        }
    }

    override fun onSupportInvisible() {
        super.onSupportInvisible()
        mLoadingPopup.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
    override fun initData() {
        mLoadingPopup= Util.createLoadingPopup(requireContext())
        binding.vo=historyUser
        EventBus.getDefault().register(this)
        EBMessage.postMessage(EBMessageType.LOADING_POPUP_DISMISS,null)

        binding.title.setLeftLayoutClickListener{onBackPressedSupport()}
        binding.llOtherLogin.setOnClickListener {
//
//            (activity as LoginActivity).onBackPressedSupport()
//            com.ftofs.twant.login.UserManager.start(requireContext())
            showLoading()
            OneStepLogin.start(requireContext(),true)
        }
        binding.btnOneStep.setOnClickListener {loginAction() }
        //原始跳轉方法 僅供參考，廢棄代碼
//        binding.tvOneStep.setOnClickListener { parentFragmentManager.beginTransaction().addToBackStack("OneStepLoginFragment").replace(R.id.container,OneStepLoginFragment()).commit() }
        binding.btnViewTos.apply { setOnClickListener {  start(H5GameFragment.newInstance(H5GameFragment.ARTICLE_ID_TERMS_OF_SERVICE, text.toString())) } }
        binding.btnViewPrivateTerms.apply {
            setOnClickListener { start(H5GameFragment.newInstance(H5GameFragment.ARTICLE_ID_TERMS_OF_PRIVATE, text.toString())) }
        }
    }

    private fun loginAction() {
//        (activity as LoginActivity).viewModel<LoginViewModel>().value
        viewModel.login(historyUser)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)

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
        viewModel.apply {
            successLoginInfo.observe(this@HistoryLoginFragment){
                ToastUtil.success(context, "登入成功")
                viewModel.loginLiveData.value?.let {a->
                    SLog.info("保存用戶信息")
                    com.ftofs.twant.login.UserManager.saveUser(a)
                }

                com.ftofs.twant.util.User.onNewLoginSuccess(it.memberId!!, LoginType.MOBILE, it)
                hideSoftInput()

                SLog.info("登錄成功")
                Util.getMemberToken(_mActivity)
                (activity as LoginActivity).onBackPressedSupport()
            }
            msgError.observe(this@HistoryLoginFragment){if(it.isNotEmpty()) {ToastUtil.error(context, it)
                com.ftofs.twant.login.UserManager.removeUser()
                (activity as LoginActivity).onBackPressedSupport()
                OneStepLogin.start(requireContext(),false)
//                (activity as LoginActivity).ch
            }}
        }
        aViewModel.apply {

            successLoginInfo.observe(this@HistoryLoginFragment){
                ToastUtil.success(context, "登入成功")
                com.ftofs.twant.login.UserManager.saveUser(aViewModel.loginLiveData.value)
                com.ftofs.twant.util.User.onNewLoginSuccess(it.memberId!!, LoginType.MOBILE, it)
                hideSoftInput()

                SLog.info("登錄成功")
                com.ftofs.twant.util.Util.getMemberToken(_mActivity)
                (activity as LoginActivity).onBackPressedSupport()
            }
            msgError.observe(this@HistoryLoginFragment){if(it.isNotEmpty()) ToastUtil.error(context, it)}

        //登陆成功才会保存账号信息

    }
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when (it) {
                StateLiveData.StateEnum.Success -> {
                    mLoadingPopup?.dismiss()
                    KLog.e("数据获取成功--关闭loading")
                }
                else -> {
                    mLoadingPopup?.dismiss()
                    KLog.e("其他状态--关闭loading")
//                    loadingUtil?.hideLoading()
                }
            }
        })
    }


}