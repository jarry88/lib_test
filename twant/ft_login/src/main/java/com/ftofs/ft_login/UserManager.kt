package com.ftofs.ft_login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gzp.lib_common.model.User
import com.tencent.mmkv.MMKV
import com.ftofs.ft_login.ui.LoginActivity
import com.alibaba.fastjson.JSON
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.Util
import com.wzq.mvvmsmart.utils.KLog

object UserManager {
    private const val USER_DATA: String = "user_data"
    private var mmkv: MMKV = MMKV.defaultMMKV()

    private val liveData = MutableLiveData<User>()

    fun getLoginLiveData(): MutableLiveData<User> {
        return liveData
    }
    fun getUser(): User? {
        return mmkv.decodeParcelable(USER_DATA, User::class.java)
    }
    fun saveUser(user: User?) {
        mmkv.encode(USER_DATA, user)
        if (liveData.hasObservers()) {
            liveData.postValue(user)
        }
    }

    fun isLogin(): Boolean {
        return getUser() != null

    }
    fun removeUser() {
        mmkv.encode(USER_DATA, "")
//        if (liveData.hasObservers()) {
//            liveData.postValue(getUser())
//        }
    }

    fun start(context: Context): LiveData<User> {
        val r = Util.random(6)

        val intent=
            Intent(context,LoginActivity::class.java).apply {if(r)putExtra("user",User("00853,61234567","abc123456",2,"1","1",1,"s","dd"))}
        context.startActivity(intent)
        return liveData
    }
}