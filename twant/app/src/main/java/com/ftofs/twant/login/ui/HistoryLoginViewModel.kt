package com.ftofs.twant.login.ui

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.model.LoginInfo
import com.ftofs.twant.util.Util
import com.ftofs.twant.widget.HwLoadingPopup
import com.gzp.lib_common.base.BaseViewModel
import com.gzp.lib_common.model.User
import com.lxj.xpopup.core.BasePopupView

class HistoryLoginViewModel(application: Application) :BaseViewModel(application){
    val loginLiveData = MutableLiveData<User>()

    private val repository by lazy {object : BaseRepository(){} }
    val successLoginInfo by lazy { MutableLiveData<LoginInfo>() }
    val msgError by lazy { MutableLiveData<String>() }


    fun login(user: User){
        launch(
            stateLiveData,
                {repository.run { simpleGet(api.getLogin(user.mobile!!,user.password!!,"android")) }},
                {//success
                    loginLiveData.value=user
                    successLoginInfo.postValue(it)
                     },
                {msgError.postValue(it.error)}
        )
    }
}
