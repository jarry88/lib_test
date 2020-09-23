package com.ftofs.twant.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gzp.lib_common.model.User
import com.tencent.mmkv.MMKV
import com.ftofs.twant.login.ui.LoginActivity
import com.gzp.lib_common.utils.SLog

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
        getUser()?.let {
            SLog.info("跳轉至啓動頁")
            val intent=
                    Intent(context, LoginActivity::class.java).apply { putExtra("user",it)
                        SLog.info(it.toString())
                    }
            context.startActivity(intent)
        }?: OneStepLogin.start(context).apply { SLog.info("沒有歷史登錄跳轉至啓動頁")
        }

        return liveData
    }
}