package com.gzp.lib_common.service.login.wrap

import android.content.Context
import androidx.lifecycle.LiveData
import com.github.richardwrq.krouter.annotation.Inject
import com.github.richardwrq.krouter.annotation.Provider
import com.github.richardwrq.krouter.api.core.KRouter
import com.gzp.lib_common.model.User
import com.gzp.lib_common.service.ConstantsPath
import com.gzp.lib_common.service.login.LoginService

object LoginServiceImplWrap {

    @Inject(name =ConstantsPath.LOGIN_SERVICE_PATH)
    lateinit var service: LoginService
    init {
        KRouter.inject(this)
    }

    fun isLogin():Boolean{
        return service.isLogin()
    }
    fun start(context: Context): LiveData<User> {
        return service.start(context)
    }
    fun getLiveData():LiveData<User>{
        return service.getLiveData()
    }
}