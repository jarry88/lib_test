package com.ftofs.ft_login.di

import com.ftofs.ft_login.ui.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginViewModelModule = module {
    viewModel {
        LoginViewModel()
    }
}
