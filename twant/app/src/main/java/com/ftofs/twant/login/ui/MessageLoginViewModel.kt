package com.ftofs.twant.login.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.model.AuthCodeInfo
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.utils.Util

class MessageLoginViewModel(application: Application):BaseViewModel(application) {
    val authCodeInfo by lazy { MutableLiveData<AuthCodeInfo>() }

    private val repository by lazy { object :BaseRepository(){} }
    fun getMessageLogin(mobile:String, smsAuthCode:String, recommendNumber:String?=null) {
        val queryParams = mapOf(
                "mobile" to mobile,
                "smsAuthCode" to smsAuthCode,
                "clientType" to "android"
        ).apply {
            recommendNumber?.let {
                plus("recommendNumber" to recommendNumber)
                plus("clientUuid" to Util.getUUID())
            }
        }
        launch(stateLiveData,
                {repository.run { simpleGet(api.getMessage(
                        queryParams
                )) }},
                {
                    //success
                    stateLiveData.postSuccess()
                }
        )
    }

    fun getSmsAuthCode(mobile: String) {
        launch(stateLiveData,
                {repository.run { simpleGet(api.getLoginFindPwd(mobile)) }},
                { stateLiveData.postSuccess() },
                final = {}
        )
    }

}
