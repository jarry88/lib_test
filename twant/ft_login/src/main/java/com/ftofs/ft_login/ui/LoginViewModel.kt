package com.ftofs.ft_login.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ftofs.lib_net.BaseRepository
import com.gzp.lib_common.model.User

class LoginViewModel :ViewModel() {
    private val repository by lazy { object :BaseRepository(){} }
    private val loginLiveData = MutableLiveData<User>()
    fun add(){}
}
