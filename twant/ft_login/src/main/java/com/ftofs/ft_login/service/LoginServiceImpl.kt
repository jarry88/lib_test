package com.ftofs.ft_login.service

import android.content.Context
import androidx.lifecycle.LiveData
import com.alibaba.android.arouter.facade.annotation.Route
import com.ftofs.ft_login.UserManager
import com.gzp.lib_common.model.User
import com.gzp.lib_common.service.ConstantsPath
import com.gzp.lib_common.service.login.LoginService

/**
 * Create by liwen on 2020/5/27
 */
@Route(path = ConstantsPath.LOGIN_SERVICE_PATH)
class LoginServiceImpl : LoginService {

    override fun isLogin(): Boolean {
        return UserManager.isLogin()
    }

    override fun start(context: Context): LiveData<User> {
        return UserManager.start(context)
    }
    override fun getLiveData(): LiveData<User> {
        return UserManager.getLoginLiveData()
    }

    override fun init(context: Context?) {

    }
}