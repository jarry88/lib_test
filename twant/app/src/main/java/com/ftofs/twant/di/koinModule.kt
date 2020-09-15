package com.ftofs.twant.di

import com.ftofs.ft_login.di.loginViewModelModule
import com.ftofs.lib_net.MRequest
import org.koin.dsl.module

val api = MRequest.getInstance().service
val koinModule = listOf (
        loginViewModelModule
)