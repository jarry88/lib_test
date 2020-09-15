package com.gzp.lib_common.service.login

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.template.IProvider
import com.gzp.lib_common.model.User

interface LoginService:IProvider {
    fun isLogin(): Boolean
    fun start(context: Context):LiveData<User>
}