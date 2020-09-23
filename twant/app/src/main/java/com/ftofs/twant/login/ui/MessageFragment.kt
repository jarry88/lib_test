package com.ftofs.twant.login.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.MessageLoginLayoutBinding
import com.ftofs.lib_common_ui.entity.ListPopupItem
import com.ftofs.lib_common_ui.popup.ListPopup
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.base.callback.OnSelectedListener
import com.gzp.lib_common.constant.PopupType
import com.gzp.lib_common.utils.SLog
import com.lxj.xpopup.XPopup
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog
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
                background=resources.getDrawable(com.ftofs.lib_common_ui.R.drawable.bg_refresh_captcha,null)
                text="重新獲取" }

        }
    } }
    private var promotionCodeVisible=false
    private var selectedMobileZoneIndex=0
    private val aViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java)
        }

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
        SLog.info(listOf<Int>().isNullOrEmpty().toString())
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
        aViewModel?.apply {this.getMobileAreaZoneList() }
        binding.thirdLoginContainer.apply {

        }
    }


    override fun initViewObservable() {
        aViewModel.mobileZoneList.observe(this, { binding.etPhoneView.mobileList = it
            if(it.isNotEmpty()) canSendSMS=true
        })
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when (it) {
                StateLiveData.StateEnum.Loading -> {
//                    loadingUtil?.showLoading("加载中..")


                    KLog.e("请求数据中--显示loading")
                }
                StateLiveData.StateEnum.Success -> {
                    onBackPressedSupport()
                    KLog.e("数据获取成功--关闭loading")
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
