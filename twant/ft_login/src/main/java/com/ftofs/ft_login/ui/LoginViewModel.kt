package com.ftofs.ft_login.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.model.MobileZone
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.model.User
import com.gzp.lib_common.service.AppService
import com.gzp.lib_common.utils.BaseContext

class LoginViewModel(application: Application=BaseContext.instance.getContext() as Application) :BaseViewModel(application) {
    private val repository by lazy { object :BaseRepository(){} }
    private val loginLiveData = MutableLiveData<User>()
    val mobileZoneList by lazy {MutableLiveData<List<MobileZone>>() }

    fun add(){}
    fun login(aliYunToken:String) {
        launch(stateLiveData,
                {repository.run { simpleGet(api.getLoginOne(aliYunToken,"android")) }},
                {stateLiveData.postSuccess()}
        )
    }
    fun getMobileAreaZoneList() {
        //這裏後續要塞到綫程池處理
        launch(stateLiveData,
                {repository.run {
                    simpleGet(api.getMobileZoneList()) }},
                {mobileZoneList.postValue(it.adminMobileAreaList)},
                final = {}
        )
    }
}
