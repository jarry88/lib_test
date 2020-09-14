package com.gzp.lib_common.service.login.wrap

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
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
}