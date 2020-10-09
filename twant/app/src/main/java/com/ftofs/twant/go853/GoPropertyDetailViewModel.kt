package com.ftofs.twant.go853

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ftofs.lib_net.BaseRepository
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseViewModel

class GoPropertyDetailViewModel(application: Application):BaseViewModel(application) {
    val currPropertyInfo by lazy { MutableLiveData<PropertyVo>() }
    val repository by lazy { object :BaseRepository(){} }
    fun getPropertyDetail(pid: Int) {//獲取房產詳情
        launch(stateLiveData, {
          repository.run { simpleGet(api.getPropertyInfo(pid)) }
        }, {
            currPropertyInfo.postValue(it)
        })
    }
}
