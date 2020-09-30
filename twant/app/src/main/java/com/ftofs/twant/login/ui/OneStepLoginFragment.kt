package com.ftofs.twant.login.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.alibaba.fastjson.JSON
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.OneStepLoginLayoutBinding
import com.ftofs.twant.login.utils.ExecutorManager
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.ToastUtil
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.TokenResultListener
import com.mobile.auth.gatewayauth.model.TokenRet
import com.gzp.lib_common.smart.event.StateLiveData
import com.gzp.lib_common.smart.utils.KLog

class OneStepLoginFragment:BaseTwantFragmentMVVM<OneStepLoginLayoutBinding, OneKeyLoginViewModel>(){

    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.one_step_login_layout
    }
    private  var mPhoneNumberAuthHelper:PhoneNumberAuthHelper?=null
    private val mTokenResultListener by lazy {  object : TokenResultListener {
        override fun onTokenSuccess(s: String) {
            ToastUtil.success(context,"獲取成功")
            SLog.info("獲取成功")
            try {
                val tokenRet = JSON.parseObject(s, TokenRet::class.java)
                if (ResultCode.CODE_START_AUTHPAGE_SUCCESS == tokenRet.code) {
                    Log.i("TAG", "唤起授权页成功：$s")
                }
                if (ResultCode.CODE_GET_TOKEN_SUCCESS == tokenRet.code) {
                    Log.i("TAG", "获取token成功：$s")
                    actionOneKeyLogin(tokenRet.token)
//                    mUIConfig.release()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onTokenFailed(s: String) {
            SLog.info("獲取失败$s")

            Log.e("OneKeyLoginActivity.TAG", "获取token失败：$s")
            mPhoneNumberAuthHelper?.quitLoginPage()
            try {
                val tokenRet = JSON.parseObject(s, TokenRet::class.java)
                if (ResultCode.CODE_ERROR_USER_CANCEL == tokenRet?.code) {
                    //模拟的是必须登录 否则直接退出app的场景
                    activity?.finish()
                } else {
                    ToastUtil.error(context, "一键登录失败切换到其他登录方式")
                    start(MessageFragment(""))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    }

    private fun actionOneKeyLogin(token:String) {
        viewModel.login(token)
        getResultWithToken(token)

    }

    /**
     * 拿到token ，成功后跳出阿里登錄頁
     */
    private fun getResultWithToken(token: String?) {
        token?.let {
            ExecutorManager.run{
                (activity as LoginActivity).runOnUiThread{
//                    mTvResult.setText("登陆成功：$result")
//                    SLog.info(MockRequest.getPhoneNumber(it))
//                    ToastUtil.success(context, MockRequest.getPhoneNumber(it))
                    mPhoneNumberAuthHelper!!.quitLoginPage()
                }
            }
        }
    }


    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {

        sdkInit()
        oneKeyLogin()
        binding.tvOneStep.setOnClickListener{
            oneKeyLogin()
        }
//        oneKeyLogin()
    }
    private fun sdkInit() {
        //2初始化sdk實例
        mPhoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(context, mTokenResultListener).apply {
            //3設置sdk密匙 已經在app啓動時進行了設置
        }

    }

    /**
     * 进入app就需要登录的场景使用
     */
    private fun oneKeyLogin() {
        //4检测终端⽹络环境是否⽀持⼀键登录或者号码认证，根据回调结果确定是否可以使
        //⽤⼀键登录功能
        mPhoneNumberAuthHelper?.takeIf { it.checkEnvAvailable() }?.let {
            KLog.e("進行一鍵登錄")
            SLog.info("進行一鍵登錄")
            //拉起授权页 timeout 超时时间
            it.getLoginToken(context,5000)
        }?:start(MessageFragment("",false,true))//否則直接拉起驗證碼登錄頁
    }

    override fun onBackPressedSupport(): Boolean {
        if (parentFragmentManager.backStackEntryCount <= 1) {
            hideSoftInput()
            (activity as LoginActivity).onBackPressedSupport()
        } else {
            hideSoftInputPop()
        }
        return true
    }
    override fun initViewObservable() {
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when(it){
                StateLiveData.StateEnum.Loading -> {
//                    loadingUtil?.showLoading("加载中.."

                    KLog.e("请求数据中--显示loading")
                }
                StateLiveData.StateEnum.Success -> {
                    SLog.info("進行一鍵登錄,後端返回成功")

                    onBackPressedSupport()
                    KLog.e("数据获取成功--关闭loading")
                }
                else -> {

                    KLog.e("其他状态--关闭loading")
//                    loadingUtil?.hideLoading()
                }
            }
        })
    }
}
