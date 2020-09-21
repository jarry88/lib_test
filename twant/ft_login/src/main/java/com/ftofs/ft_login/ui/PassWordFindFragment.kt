package com.ftofs.ft_login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ftofs.ft_login.R
import com.ftofs.ft_login.BR
import com.ftofs.ft_login.databinding.PasswordFindLayoutBinding
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import org.koin.androidx.viewmodel.ext.android.getViewModel


class PassWordFindFragment : BaseTwantFragmentMVVM<PasswordFindLayoutBinding,PasswordFindViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.password_find_layout
    }
    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        val a=activity
        activity?.getViewModel<LoginViewModel>()?.add()
    }
}
