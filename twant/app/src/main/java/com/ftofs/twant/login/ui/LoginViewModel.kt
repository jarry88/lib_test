package com.ftofs.twant.login.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.facebook.AccessToken
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.model.LoginInfo
import com.ftofs.lib_net.model.MobileZone
import com.ftofs.twant.TwantApplication
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.tangram.SloganView
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.model.User
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog

class LoginViewModel(application: Application = BaseContext.instance.getContext() as Application) :BaseViewModel(application) {
    var faceBookAccessToken: AccessToken?=null
    private val repository by lazy { object :BaseRepository(){} }
    val mobileZoneList by lazy {MutableLiveData<List<MobileZone>>() }
    val faceBookInfo by lazy { MutableLiveData<LoginInfo>() }
    val weChatInfo by lazy { MutableLiveData<LoginInfo>() }
    val logUtilInfo = MutableLiveData(String())
    val msgError = MutableLiveData(String())
    val successLoginInfo by lazy { MutableLiveData<LoginInfo>() }
    val loginLiveData = MutableLiveData<User>()
    fun add(){}
    fun login(aliYunToken: String) {//阿里云一键登陆
        launch(stateLiveData,
                { repository.run { simpleGet(api.getLoginOne(aliYunToken, "android")) } },
                { //finally 默认发送成功
                }
        )
    }
    fun passwordLogin(mobile:String,password:String){//账号密码登陆
        launch(stateLiveData,
            {   SLog.info("前往调取密码登陆接口")
                repository.run { simpleGet(api.passwordLogin(mapOf("mobile" to mobile,"password" to password,Constant.CLIENT_TYPE_PAIR))) }},
            {loginLiveData.value=User(mobile,password,it.memberId?:0,it.memberToken?:"",it.nickName?:"",0,it.memberToken?:"",it.memberVo?.avatar)
                successLoginInfo.value =it},
                {msgError.postValue(it.error)}
        )
    }
    fun getMobileAreaZoneList() {
        //這裏後續要塞到綫程池處理
        launch(stateLiveData,
                {
                    repository.run {
                        simpleGet(api.getMobileZoneList())
                    }
                },
                { mobileZoneList.postValue(it.adminMobileAreaList) },
                final = {}
        )
    }

    fun doFacebookLogin(accessToken: String?, userId: String?) {
        val clientType=Constant.CLIENT_TYPE_ANDROID
        val params= mapOf("accessToken" to (accessToken?:""),"userId" to (userId?:""),Constant.CLIENT_TYPE_PAIR)
        launch(stateLiveData,
                { repository.run { simpleGet(api.doFaceLogin(accessToken?:"",userId?:"",clientType)) }},
                {faceBookInfo.postValue(it) },
                error = {logUtilInfo.postValue(params.toString())},
                others = {logUtilInfo.postValue(params.toString())},
                final = {}
        )
    }

    fun getWXLoginStepOne(code: String) {
        val params = mapOf("code" to code,Constant.CLIENT_TYPE_PAIR)
        launch(stateLiveData,
                {repository.run { simpleGet(api.getWXLoginStepOne(params)) }},
                {   SLog.info("獲取微信登錄")

                    weChatInfo.value=it},
                final = {}
        )
    }
}
