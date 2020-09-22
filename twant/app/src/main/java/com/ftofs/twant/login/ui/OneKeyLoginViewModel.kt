package com.ftofs.twant.login.ui

import android.app.Application
import com.ftofs.lib_net.BaseRepository
import com.gzp.lib_common.base.BaseViewModel

class OneKeyLoginViewModel(application: Application):BaseViewModel(application) {
    private val repository by lazy { object :BaseRepository(){} }
    fun login(aliYunToken:String) {
        launch(stateLiveData,
                {repository.run { simpleGet(api.getLoginOne(aliYunToken,"android")) }},
                {stateLiveData.postSuccess()}
        )
    }

}
