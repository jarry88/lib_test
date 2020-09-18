package com.gzp.lib_common.service.login.wrap

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.gzp.lib_common.model.User
import com.gzp.lib_common.service.ConstantsPath
import com.gzp.lib_common.service.login.LoginService

object LoginServiceImplWrap {

    @Autowired(name = ConstantsPath.LOGIN_SERVICE_PATH)
    lateinit var service: LoginService
    init {
        ARouter.getInstance().inject(this)
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