package com.ftofs.twant.login.ui

import android.app.Application
import com.ftofs.lib_net.BaseRepository
import com.ftofs.twant.constant.SPField
import com.gzp.lib_common.base.BaseViewModel
import com.orhanobut.hawk.Hawk

class OneKeyLoginViewModel(application: Application):BaseViewModel(application) {
    private val repository by lazy { object :BaseRepository(){} }
    fun login(aliYunToken:String) {
        launch(stateLiveData,
                {repository.run { simpleGet(api.getLoginOne(aliYunToken,"android", Hawk.get(SPField.FIELD_REGISTER_REFEREE,""))) }},
                {stateLiveData.postSuccess()}
        )
    }

}
