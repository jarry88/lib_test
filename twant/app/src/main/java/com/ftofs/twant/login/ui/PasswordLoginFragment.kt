package com.ftofs.twant.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.ft_login.BR
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.ftofs.twant.databinding.PasswordLoginLayoutBinding

class PasswordLoginFragment(mobile: String) : BaseTwantFragmentMVVM<PasswordLoginLayoutBinding, PasswordLoginViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.password_login_layout
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        binding.btnPasswordFind.setOnClickListener { start(PassWordFindFragment(binding.etPhoneView.getPhone())) }
    }
    /**
     * 保存賬號密碼到db
     */
    fun savePasswordInfo(){
    }

}
