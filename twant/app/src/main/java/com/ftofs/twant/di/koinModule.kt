package com.ftofs.twant.di

import com.ftofs.twant.login.di.loginViewModelModule
import com.ftofs.lib_net.MRequest

val api = MRequest.getInstance().service
val koinModule = listOf (
        loginViewModelModule
)