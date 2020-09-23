package com.ftofs.twant.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.databinding.PasswordFindLayoutBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import org.koin.androidx.viewmodel.ext.android.getViewModel


class PassWordFindFragment(val number:String) : BaseTwantFragmentMVVM<PasswordFindLayoutBinding, PasswordFindViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.password_find_layout
    }
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        val a=activity
        binding.findPasswordInfo.text= String.format(getString(R.string.text_find_password_info),number)
        activity?.getViewModel<LoginViewModel>()?.add()
    }
}
