package com.ftofs.twant.di

import com.ftofs.lib_net.MRequest
import org.koin.dsl.module

val api = MRequest.getInstance().service
val koinModule = module {
    factory { api }
}