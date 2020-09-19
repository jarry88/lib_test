package com.ftofs.ft_login.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gzp.lib_common.model.User

class LoginViewModel :ViewModel() {
    private val loginLiveData = MutableLiveData<User>()
}
