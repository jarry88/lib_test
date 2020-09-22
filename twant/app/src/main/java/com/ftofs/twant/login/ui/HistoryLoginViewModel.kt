package com.ftofs.twant.login.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.model.User

class HistoryLoginViewModel(application: Application) :BaseViewModel(application){
    val loginLiveData = MutableLiveData<User>()
    private val repository by lazy {object : BaseRepository(){} }

    fun login(user: User){
        launch(
            stateLiveData,
                {repository.run { simpleGet(api.getLogin(user.mobile!!,user.password!!,"android")) }},
                {//success
                     }
        )
    }
}
