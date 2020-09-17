package com.ftofs.ft_login.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.fastjson.JSON
import com.ftofs.ft_login.BR
import com.ftofs.ft_login.R
import com.ftofs.ft_login.databinding.OneStepLoginLayoutBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.ToastUtil
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.TokenResultListener
import com.mobile.auth.gatewayauth.model.TokenRet
import com.wzq.mvvmsmart.utils.KLog

class OneStepLoginFragment:BaseTwantFragmentMVVM<OneStepLoginLayoutBinding,HistoryLoginViewModel>(){
    private  var mPhoneNumberAuthHelper:PhoneNumberAuthHelper?=null
    private val mTokenResultListener by lazy {  object : TokenResultListener {
        override fun onTokenSuccess(s: String) {
            ToastUtil.success(context,"獲取成功")
            var tokenRet: TokenRet? = null
            try {
                tokenRet = JSON.parseObject(s, TokenRet::class.java)
                if (ResultCode.CODE_START_AUTHPAGE_SUCCESS == tokenRet.code) {
                    Log.i("TAG", "唤起授权页成功：$s")
                }
                if (ResultCode.CODE_GET_TOKEN_SUCCESS == tokenRet.code) {
                    Log.i("TAG", "获取token成功：$s")
//                    getResultWithToken(tokenRet.token)
//                    mUIConfig.release()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onTokenFailed(s: String) {
            Log.e("OneKeyLoginActivity.TAG", "获取token失败：$s")
            var tokenRet: TokenRet? = null
            mPhoneNumberAuthHelper?.quitLoginPage()
            try {
                tokenRet = JSON.parseObject(s, TokenRet::class.java)
                if (ResultCode.CODE_ERROR_USER_CANCEL == tokenRet?.code) {
                    //模拟的是必须登录 否则直接退出app的场景
                    activity?.finish()
                } else {
//                    Toast.makeText(getApplicationContext(), "一键登录失败切换到其他登录方式", Toast.LENGTH_SHORT).show()
//                    val pIntent = Intent(this@OneKeyLoginActivity, MessageActivity::class.java)
//                    startActivityForResult(pIntent, 1002)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    }
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.one_step_login_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {

        sdkInit()
        binding.tvOneStep.setOnClickListener{
            oneKeyLogin()
        }
//        oneKeyLogin()
    }
    fun sdkInit() {
        //2初始化sdk實例

        mPhoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(context, mTokenResultListener).apply {
            SLog.info("獲取字符穿：")

            //3設置sdk密匙
            setAuthSDKInfo(getString(R.string.one_key_secret_keys))
        }

    }

    /**
     * 进入app就需要登录的场景使用
     */
    private fun oneKeyLogin() {
        //4检测终端⽹络环境是否⽀持⼀键登录或者号码认证，根据回调结果确定是否可以使
        //⽤⼀键登录功能
        mPhoneNumberAuthHelper?.takeIf { it.checkEnvAvailable() }.let {
            KLog.e("進行一鍵登錄")

            getLoginToken(5000)
        }
    }

    /**
     * 拉起授权页
     * @param timeout 超时时间
     */
    fun getLoginToken(timeout: Int) {
        mPhoneNumberAuthHelper!!.getLoginToken(context, timeout)
//        showLoadingDialog("正在唤起授权页")
    }
}