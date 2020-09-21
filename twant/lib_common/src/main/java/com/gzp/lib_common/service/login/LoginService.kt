package com.gzp.lib_common.service.login

import android.content.Context
import android.os.UserManager
import androidx.lifecycle.LiveData
import com.github.richardwrq.krouter.api.interfaces.IProvider
import com.gzp.lib_common.model.User

interface LoginService: IProvider {
    fun isLogin(): Boolean
    fun start(context: Context):LiveData<User>

    fun getLiveData(): LiveData<User>
}