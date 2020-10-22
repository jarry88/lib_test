package com.ftofs.twant.login.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.model.AuthCodeInfo
import com.ftofs.lib_net.model.CommonInfo
import com.ftofs.lib_net.model.LoginInfo
import com.ftofs.twant.constant.SPField
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.Util
import com.orhanobut.hawk.Hawk

class MessageLoginViewModel(application: Application):BaseViewModel(application) {
    val authCodeInfo by lazy { MutableLiveData<AuthCodeInfo>() }
    var getMessageSuccess=false
    private val repository by lazy { object :BaseRepository(){} }
    val msgError by lazy { MutableLiveData(String()) }
    val successLoginInfo by lazy { MutableLiveData<LoginInfo>() }
    val commonInfo by lazy { MutableLiveData<CommonInfo>() }
    fun getMessageLogin(mobile:String, smsAuthCode:String, recommendNumber:String?=null) {
        val queryParams = mapOf(
                "mobile" to mobile,
                "smsAuthCode" to smsAuthCode,
                "clientType" to "android",
                "invite" to Hawk.get(SPField.FIELD_REGISTER_REFEREE,"")
        ).apply {
            recommendNumber?.let {
                plus("recommendNumber" to recommendNumber)
                plus("clientUuid" to com.ftofs.twant.util.Util.getUUID())
            }
        }
        SLog.info("messageLogin$queryParams")
        launch(stateLiveData,
                {repository.run { simpleGet(api.getMessage(
                        queryParams
                )) }},
                {
                    //success
                    successLoginInfo.postValue(it)
                    stateLiveData.postSuccess()
                },
                {  msgError.postValue(it.error)
                }
        )
    }

    fun getSmsAuthCode(mobile: String) {
        launch(stateLiveData,
                {repository.run { simpleGet(api.getSmsCodeSend(mobile)) }},
                {   authCodeInfo.postValue(it)},
                {SLog.info(it.toString())
                    errorMessage="驗證碼獲取失敗"},
                final = {}
        )
    }

    fun getFpdSmsAuthCode(mobile: String) {
        launch(stateLiveData,
                {repository.run { simpleGet(api.getLoginFindPwd(mobile)) }},
                {   authCodeInfo.postValue(it)},
                { errorMessage="驗證碼獲取失敗"},
                final = {}
        )
    }

    fun getSetPwd(params:Map<String,String?>) {
        launch(stateLiveData,
                {repository.run { simpleGet(api.getPwdSet(params)) }},
                {   commonInfo.postValue(it)},
                { errorMessage=it.error},
                final = {}
        )
    }

}
