package com.gzp.lib_common.service.login

import com.alibaba.android.arouter.facade.template.IProvider

interface LoginService:IProvider {
    fun isLogin(): Boolean
}